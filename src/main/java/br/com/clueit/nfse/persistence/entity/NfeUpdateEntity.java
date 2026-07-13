package br.com.clueit.nfse.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Event log das transições de estado das emissões NFe (tabela {@code nfe_update}).
 * O {@code id} é o cursor do long-poll {@code GET /v1/updates} — o agente o
 * persiste em XXLCP_AGENT_STATE na mesma transação do write-back.
 */
@Entity
@Table(name = "nfe_update")
public class NfeUpdateEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "emissao_id")
    public Long emissaoId;

    @Column(name = "source_key")
    public String sourceKey;

    @Column(name = "estado")
    public String estado;

    @Column(name = "chave_acesso")
    public String chaveAcesso;

    @Column(name = "criado_em", insertable = false, updatable = false)
    public LocalDateTime criadoEm;

    /** Registra a transição corrente da emissão (chamar dentro da transação que a alterou). */
    public static void registrar(NfeEmissaoEntity emissao) {
        NfeUpdateEntity u = new NfeUpdateEntity();
        u.emissaoId = emissao.id;
        u.sourceKey = emissao.sourceKey;
        u.estado = emissao.estado;
        u.chaveAcesso = emissao.chaveAcesso;
        u.persist();
    }

    public static List<NfeUpdateEntity> aposCursor(long cursor, int limite) {
        return find("id > ?1 order by id", cursor).page(0, limite).list();
    }
}
