package br.com.softplan.desafio.fullstack.backend.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa um usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 4264356623297647486L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CODUSUARIO", nullable = false)
	private Long codigo;
	
	@Column(name = "NOME", length = 255, nullable = false)
	private String nome;
	
	@Column(name = "LOGIN", length = 63, nullable = false)
	private String login;
	
	@Column(name = "SENHA", length = 63, nullable = false)
	private String senha;
	
	@Column(name = "EMAIL", length = 63, nullable = false)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PERMISSAO", length = 31, nullable = false)
	private PermissaoUsuario permissao;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Column(name = "DATAINCLUSAO", nullable = false)
	private Date dataInclusao;
	
	@Version
	@Column(name = "VERSAO")
	private Integer versao;
	
}
