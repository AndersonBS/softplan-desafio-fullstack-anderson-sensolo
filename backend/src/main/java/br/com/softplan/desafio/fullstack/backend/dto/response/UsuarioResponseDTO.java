package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar dados de usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 08/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO implements Serializable {

	private static final long serialVersionUID = -6406018323031415771L;

	private Long codigo;
	private String nome;
	private String login;
	private String email;
	private String senha;
	private String permissao;
	private String dataInclusao;

	public UsuarioResponseDTO(final Usuario usuario) {
		this.codigo = usuario.getCodigo();
		this.nome = usuario.getNome();
		this.login = usuario.getLogin();
		this.email = usuario.getEmail();
		this.senha = usuario.getSenha();
		this.permissao = usuario.getPermissao().getNome();
		this.dataInclusao = new SimpleDateFormat("yyyy-MM-dd").format(usuario.getDataInclusao());
	}

}
