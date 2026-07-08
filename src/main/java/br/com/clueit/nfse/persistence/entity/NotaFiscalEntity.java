package br.com.clueit.nfse.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "nota_fiscal", uniqueConstraints =
        // Deduplicação por (tenant, codigo_cliente). Declarada também na migration V1; aqui é
        // inerte em produção (schema-management = none) e vale para o schema gerado nos testes.
        @UniqueConstraint(name = "uk_nota_dedup", columnNames = {"tenant_id", "codigo_cliente"}))
public class NotaFiscalEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    public TenantEntity tenant;

    @Column(name = "codigo_cliente")
    public String codigoCliente;

    @Column(name = "codigo_cliente_original")
    public String codigoClienteOriginal;

    @Column(name = "status")
    public String status;

    @Column(name = "serie")
    public String serie;

    @Column(name = "numero_rps")
    public Long numeroRps;

    @Column(name = "tipo_rps")
    public String tipoRps;

    @Column(name = "numero_nfe")
    public Long numeroNfe;

    @Column(name = "codigo_verificacao")
    public String codigoVerificacao;

    @Column(name = "data_emissao")
    public LocalDate dataEmissao;

    @Column(name = "valor_servicos")
    public BigDecimal valorServicos;

    @Column(name = "valor_total")
    public BigDecimal valorTotal;

    @Column(name = "tomador_documento")
    public String tomadorDocumento;

    // Coluna JSON do MySQL; a String (JSON válido) é parseada pelo banco.
    // Payload JSON completo da requisição; TEXT na migration. length grande p/ o schema gerado
    // (testes) refletir isso — inerte em produção (schema-management = none).
    @Column(name = "payload_json", length = 100_000)
    public String payloadJson;

    @Column(name = "data_cancelamento")
    public LocalDateTime dataCancelamento;

    @Column(name = "motivo_cancelamento")
    public String motivoCancelamento;
}
