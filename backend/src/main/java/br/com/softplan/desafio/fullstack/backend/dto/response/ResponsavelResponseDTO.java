package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar usuários responsáveis por processos.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 15/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsavelResponseDTO implements Serializable {

	private static final long serialVersionUID = 2927720652070473786L;

	private Long codigoUsuario;
	private String codigoNomeUsuario;

	public ResponsavelResponseDTO(final Usuario usuario) {
		this.codigoUsuario = usuario.getCodigo();
		this.codigoNomeUsuario = usuario.getCodigoNome();
	}

}
