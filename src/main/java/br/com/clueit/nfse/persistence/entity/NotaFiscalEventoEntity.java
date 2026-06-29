package br.com.clueit.nfse.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Histórico das transições de status (e erros/alertas da Prefeitura). */
@Entity
@Table(name = "nota_fiscal_evento")
public class NotaFiscalEventoEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "nota_id")
    public Long notaId;

    @Column(name = "status_anterior")
    public String statusAnterior;

    @Column(name = "status_novo")
    public String statusNovo;

    @Column(name = "mensagem")
    public String mensagem;

    @Column(name = "erro_codigo")
    public String erroCodigo;

    @Column(name = "erro_detalhe")
    public String erroDetalhe;

    public static void registrar(Long notaId, String anterior, String novo, String mensagem) {
        NotaFiscalEventoEntity e = new NotaFiscalEventoEntity();
        e.notaId = notaId;
        e.statusAnterior = anterior;
        e.statusNovo = novo;
        e.mensagem = mensagem;
        e.persist();
    }
}
