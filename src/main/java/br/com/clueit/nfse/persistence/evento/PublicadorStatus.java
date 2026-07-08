package br.com.clueit.nfse.persistence.evento;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Status;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * Publica o evento {@code STATUS_ALTERADO} no tópico SNS a cada transição de status de uma nota,
 * alimentando a fila {@code nfse-admin-eventos} que o admin consome para o push em tempo real.
 *
 * <p>Centraliza a lógica (usada por todas as lambdas e pelo api-ingestao). A publicação é
 * <b>pós-commit</b>: registra uma sincronização JTA e só envia se a transação confirmar — assim o
 * admin, ao reconsultar o banco pelo ping, já enxerga o novo status (e um rollback não gera evento).</p>
 */
@ApplicationScoped
public class PublicadorStatus {

    @Inject
    SnsClient sns;

    @Inject
    TransactionSynchronizationRegistry txSync;

    @ConfigProperty(name = "nfse.sns.topic-name", defaultValue = "nfe-eventos")
    String topicName;

    @ConfigProperty(name = "nfse.sns.topic-arn")
    Optional<String> topicArnConfigurado;

    private volatile String topicArn;

    /** Agenda a publicação de STATUS_ALTERADO para depois do commit da transação corrente. */
    public void notificar(Long notaId, String tenantId) {
        if (notaId == null || txSync.getTransactionStatus() != Status.STATUS_ACTIVE) {
            return;
        }
        txSync.registerInterposedSynchronization(new Synchronization() {
            @Override
            public void beforeCompletion() {
                // nada
            }

            @Override
            public void afterCompletion(int status) {
                if (status == Status.STATUS_COMMITTED) {
                    publicar(notaId, tenantId);
                }
            }
        });
    }

    private void publicar(Long notaId, String tenantId) {
        String corpo = String.format(
                "{\"tipo\":\"STATUS_ALTERADO\",\"notaId\":%d,\"tenantId\":%s,\"timestamp\":\"%s\"}",
                notaId, jsonString(tenantId), Instant.now());
        sns.publish(b -> b
                .topicArn(resolverTopicArn())
                .message(corpo)
                .messageAttributes(Map.of("tipo", MessageAttributeValue.builder()
                        .dataType("String").stringValue("STATUS_ALTERADO").build())));
    }

    private String resolverTopicArn() {
        if (topicArn == null) {
            synchronized (this) {
                if (topicArn == null) {
                    topicArn = topicArnConfigurado
                            .filter(arn -> !arn.isBlank())
                            .orElseGet(() -> sns.createTopic(b -> b.name(topicName)).topicArn());
                }
            }
        }
        return topicArn;
    }

    /** {@code null} → JSON null; senão string entre aspas com escape mínimo. */
    private static String jsonString(String valor) {
        if (valor == null) {
            return "null";
        }
        return "\"" + valor.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
