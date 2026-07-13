package br.com.clueit.nfse.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Escopo do agente por emitente/módulo (tabela {@code nfe_escopo}) — a feature
 * flag central do plano: SHADOW (observa e compara), ATIVO (claim+write-back)
 * ou DESLIGADO. Cutover/rollback = UPDATE nesta tabela, sem deploy.
 */
@Entity
@Table(name = "nfe_escopo")
public class NfeEscopoEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "cnpj_emitente")
    public String cnpjEmitente;

    @Column(name = "modulo")
    public String modulo;

    @Column(name = "ufs")
    public String ufs;

    @Column(name = "modo")
    public String modo;
}
