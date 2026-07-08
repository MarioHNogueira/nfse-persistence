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
 * Token de API de um cliente (tabela {@code api_token}). Um tenant pode ter vários tokens.
 * Guardamos apenas o {@code tokenHash} (SHA-256 hex); o valor em claro nunca é persistido.
 */
@Entity
@Table(name = "api_token")
public class ApiTokenEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "tenant_id")
    public Long tenantId;

    @Column(name = "token_hash")
    public String tokenHash;

    @Column(name = "descricao")
    public String descricao;

    @Column(name = "ativo")
    public Boolean ativo;

    @Column(name = "criado_em")
    public LocalDateTime criadoEm;

    @Column(name = "ultimo_uso_em")
    public LocalDateTime ultimoUsoEm;

    /** Retorna um token ativo com o hash informado, ou {@code null} se não houver. */
    public static ApiTokenEntity buscarAtivoPorHash(String tokenHash) {
        return find("tokenHash = ?1 and ativo = true", tokenHash).firstResult();
    }
}
