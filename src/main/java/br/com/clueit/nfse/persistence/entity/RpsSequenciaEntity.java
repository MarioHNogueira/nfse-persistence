package br.com.clueit.nfse.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Sequência de numeração de RPS por (tenant, série). Alocação com lock pessimista. */
@Entity
@Table(name = "rps_sequencia")
public class RpsSequenciaEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "tenant_id")
    public Long tenantId;

    @Column(name = "serie")
    public String serie;

    @Column(name = "proximo_numero")
    public Long proximoNumero;
}
