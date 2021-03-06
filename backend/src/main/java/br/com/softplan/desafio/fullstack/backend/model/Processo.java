package br.com.softplan.desafio.fullstack.backend.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa um processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Entity
@Table(name = "PROCESSO")
public class Processo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CODPROCESSO", nullable = false)
	private Long codigo;
	
	@Column(name = "NOME", length = 63, nullable = false)
	private String nome;
	
	@Column(name = "DESCRICAO", length = 255, nullable = false)
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 31, nullable = false)
	private StatusProcesso status;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "DATAINICIO", nullable = false)
	private Date dataInicio;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "DATATERMINO")
	private Date dataTermino;
	
	@OneToOne()
	@JoinColumn(name = "CODUSUARIO", nullable = false)
	private Usuario responsavel;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "PROCESSO_USUARIO", joinColumns = @JoinColumn(name = "CODPROCESSO"), inverseJoinColumns = @JoinColumn(name = "CODUSUARIO"))
	private List<Usuario> usuarios = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODPROCESSO")
	private List<Parecer> pareceres = new ArrayList<>();

	@Version
	@Column(name = "VERSAO")
	private Integer versao;
	
	public String getCodigoNome() {
		return String.format("%d - %s", this.codigo, this.nome);
	}
	
}
