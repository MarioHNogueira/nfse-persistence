package br.com.clueit.nfse.persistence.evento;

import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Teste unitário do publicador de STATUS_ALTERADO (SNS + sincronização JTA mockados). */
class PublicadorStatusTest {

    private PublicadorStatus pub;
    private SnsClient sns;
    private TransactionSynchronizationRegistry txSync;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        pub = new PublicadorStatus();
        sns = mock(SnsClient.class);
        txSync = mock(TransactionSynchronizationRegistry.class);
        pub.sns = sns;
        pub.txSync = txSync;
        pub.topicName = "nfe-eventos";
        pub.topicArnConfigurado = Optional.of("arn:aws:sns:sa-east-1:000000000000:nfe-eventos");
        // o mock executa o consumer para o builder (e o resolverTopicArn) rodar de fato
        when(sns.publish(any(Consumer.class))).thenAnswer(inv -> {
            ((Consumer<PublishRequest.Builder>) inv.getArgument(0)).accept(PublishRequest.builder());
            return PublishResponse.builder().build();
        });
    }

    private Synchronization capturarSincronizacao() {
        ArgumentCaptor<Synchronization> captor = ArgumentCaptor.forClass(Synchronization.class);
        verify(txSync).registerInterposedSynchronization(captor.capture());
        return captor.getValue();
    }

    @Test
    @SuppressWarnings("unchecked")
    void agendaEPublicaAposCommit() {
        when(txSync.getTransactionStatus()).thenReturn(Status.STATUS_ACTIVE);

        pub.notificar(1L, "07095301000195");

        Synchronization sync = capturarSincronizacao();
        sync.beforeCompletion();                       // método vazio (cobertura)
        sync.afterCompletion(Status.STATUS_COMMITTED); // commit → publica
        verify(sns).publish(any(Consumer.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void rollbackNaoPublica() {
        when(txSync.getTransactionStatus()).thenReturn(Status.STATUS_ACTIVE);

        pub.notificar(1L, "07095301000195");

        capturarSincronizacao().afterCompletion(Status.STATUS_ROLLEDBACK);
        verify(sns, never()).publish(any(Consumer.class));
    }

    @Test
    void semTransacaoAtivaNaoAgenda() {
        when(txSync.getTransactionStatus()).thenReturn(Status.STATUS_NO_TRANSACTION);
        pub.notificar(1L, "07095301000195");
        verify(txSync, never()).registerInterposedSynchronization(any());
    }

    @Test
    void notaIdNuloNaoAgenda() {
        pub.notificar(null, "07095301000195");
        verify(txSync, never()).registerInterposedSynchronization(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void semArnCriaTopicoEToleraTenantNulo() {
        pub.topicArnConfigurado = Optional.empty();
        when(txSync.getTransactionStatus()).thenReturn(Status.STATUS_ACTIVE);
        when(sns.createTopic(any(Consumer.class)))
                .thenReturn(CreateTopicResponse.builder()
                        .topicArn("arn:aws:sns:sa-east-1:000000000000:nfe-eventos").build());

        pub.notificar(9L, null); // tenantId nulo → jsonString(null)

        capturarSincronizacao().afterCompletion(Status.STATUS_COMMITTED);
        verify(sns).createTopic(any(Consumer.class));
        verify(sns).publish(any(Consumer.class));
    }
}
