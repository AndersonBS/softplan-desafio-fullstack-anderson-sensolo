package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import br.com.softplan.desafio.fullstack.backend.model.Processo;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar dados de usuários vinculados ao processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 11/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessoUsuarioResponseDTO implements Serializable {

	private static final long serialVersionUID = -4799431168040762038L;

	private String processo;
	private String usuario;

	public ProcessoUsuarioResponseDTO(final Processo processo, final Usuario usuario) {
		this.processo = processo.getCodigoNome();
		this.usuario = usuario.getCodigoNome();
	}

}
