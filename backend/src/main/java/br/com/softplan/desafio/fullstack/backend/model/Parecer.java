package br.com.softplan.desafio.fullstack.backend.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa um parecer de um processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Entity
@Table(name = "PARECER")
public class Parecer implements Serializable {

	private static final long serialVersionUID = 36191721367651717L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CODPARECER", nullable = false)
	private Long codigo;
	
	@Column(name = "DESCRICAO", length = 255, nullable = false)
	private String descricao;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "DATA", nullable = false)
	private Date data;
	
	@OneToOne()
	@JoinColumn(name = "CODPROCESSO", nullable = false)
	private Processo processo;
	
	@OneToOne()
	@JoinColumn(name = "CODUSUARIO", nullable = false)
	private Usuario autor;
	
	@Version
	@Column(name = "VERSAO")
	private Integer versao;

}
