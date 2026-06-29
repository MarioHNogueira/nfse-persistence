package br.com.clueit.nfse.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Referência de arquivo no S3 (XML assinado, XML de retorno, DANFE PDF). */
@Entity
@Table(name = "nota_fiscal_arquivo")
public class NotaFiscalArquivoEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "nota_id")
    public Long notaId;

    @Column(name = "tipo")
    public String tipo; // XML_ASSINADO | XML_RETORNO | DANFE_PDF

    @Column(name = "s3_bucket")
    public String s3Bucket;

    @Column(name = "s3_key")
    public String s3Key;

    @Column(name = "content_type")
    public String contentType;

    @Column(name = "tamanho_bytes")
    public Long tamanhoBytes;
}
