package br.com.clueit.nfse.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tenant")
public class TenantEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "cnpj")
    public String cnpj;

    @Column(name = "razao_social")
    public String razaoSocial;

    @Column(name = "inscricao_municipal")
    public String inscricaoMunicipal;

    @Column(name = "layout_xsd")
    public String layoutXsd;

    @Column(name = "ambiente")
    public String ambiente;

    @Column(name = "serie_rps_padrao")
    public String serieRpsPadrao;

    @Column(name = "numero_rps_inicial")
    public Long numeroRpsInicial;

    @Column(name = "tipo_rps_padrao")
    public String tipoRpsPadrao;

    @Column(name = "ws_endpoint")
    public String wsEndpoint;

    @Column(name = "ativo")
    public Boolean ativo;

    public static TenantEntity buscarPorCnpj(String cnpj) {
        return find("cnpj", cnpj).firstResult();
    }

    // --- Emissor Nacional ---

    /** Modalidade de emissão do tenant: SP (default) ou NACIONAL. */
    @Column(name = "modalidade_emissao")
    public String modalidadeEmissao;

    /** Tipo de emissão quando NACIONAL: NORMAL ou DECISAO_JUDICIAL. */
    @Column(name = "tipo_emissao_nacional")
    public String tipoEmissaoNacional;
}
