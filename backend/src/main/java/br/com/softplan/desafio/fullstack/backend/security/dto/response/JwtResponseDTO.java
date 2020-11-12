package br.com.softplan.desafio.fullstack.backend.security.dto.response;

import java.io.Serializable;
import br.com.softplan.desafio.fullstack.backend.security.model.JwtUserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar dados de autenticação.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponseDTO implements Serializable {

	private static final long serialVersionUID = 3628674574278574192L;

	private String jwtToken;
	private Long codigo;
	private String nome;
	private String email;
	private String permissao;

	public JwtResponseDTO(final String jwtToken, final JwtUserDetails jwtUserDetails) {
		this.jwtToken = jwtToken;
		this.codigo = jwtUserDetails.getId();
		this.nome = jwtUserDetails.getNome();
		this.email = jwtUserDetails.getEmail();
		this.permissao = jwtUserDetails.getPermissao();
	}

}
