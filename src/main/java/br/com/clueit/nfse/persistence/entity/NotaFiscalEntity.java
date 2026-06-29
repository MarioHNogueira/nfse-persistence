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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "nota_fiscal")
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
    @Column(name = "payload_json")
    public String payloadJson;

    @Column(name = "data_cancelamento")
    public LocalDateTime dataCancelamento;

    @Column(name = "motivo_cancelamento")
    public String motivoCancelamento;
}
