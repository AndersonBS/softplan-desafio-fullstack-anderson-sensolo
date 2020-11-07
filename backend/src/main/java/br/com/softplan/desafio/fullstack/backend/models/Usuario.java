package br.com.softplan.desafio.fullstack.backend.models;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa um usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Data
@NoArgsConstructor
public class Usuario {

	private Long codigo;
	private String nome;
	private String login;
	private String senha;
	private String email;
	private PermissaoUsuario permissao;
	private Date dataInclusao;

}
