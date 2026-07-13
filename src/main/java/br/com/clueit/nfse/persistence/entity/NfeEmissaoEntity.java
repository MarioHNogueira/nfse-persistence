package br.com.clueit.nfse.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Emissão de NFe (tabela {@code nfe_emissao}) — fluxo de desacoplamento do old-nfe2lacompt.
 * A idempotência do agente é garantida por {@code sourceKey} (unique) + {@code payloadHash}:
 * replay do mesmo payload devolve a emissão existente; payload divergente é conflito.
 */
@Entity
@Table(name = "nfe_emissao")
public class NfeEmissaoEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "source_key")
    public String sourceKey;

    @Column(name = "cnpj_emitente")
    public String cnpjEmitente;

    @Column(name = "modo")
    public String modo;

    @Column(name = "estado")
    public String estado;

    @Column(name = "payload_json", columnDefinition = "MEDIUMTEXT")
    public String payloadJson;

    @Column(name = "payload_hash")
    public String payloadHash;

    @Column(name = "legacy_xml", columnDefinition = "MEDIUMTEXT")
    public String legacyXml;

    @Column(name = "chave_acesso")
    public String chaveAcesso;

    @Column(name = "nprot")
    public String nprot;

    @Column(name = "cstat")
    public String cstat;

    @Column(name = "xmotivo")
    public String xmotivo;

    @Column(name = "dh_recbto")
    public String dhRecbto;

    @Column(name = "recibo")
    public String recibo;

    @Column(name = "justificativa_cancel")
    public String justificativaCancel;

    @Column(name = "nprot_cancel")
    public String nprotCancel;

    @Column(name = "criado_em", insertable = false, updatable = false)
    public LocalDateTime criadoEm;

    @Column(name = "atualizado_em", insertable = false, updatable = false)
    public LocalDateTime atualizadoEm;

    public static NfeEmissaoEntity buscarPorSourceKey(String sourceKey) {
        return find("sourceKey", sourceKey).firstResult();
    }
}
