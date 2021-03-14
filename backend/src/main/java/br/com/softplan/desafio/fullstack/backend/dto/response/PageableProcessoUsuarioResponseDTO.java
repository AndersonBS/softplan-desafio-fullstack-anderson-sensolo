package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar dados de usuários vinculados à processos com paginação.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 14/03/2021
 */

@Data
@NoArgsConstructor
public class PageableProcessoUsuarioResponseDTO implements Serializable {

	private static final long serialVersionUID = 8857237818572665977L;

	private final List<ProcessoUsuarioResponseDTO> processoUsuarios = new ArrayList<>();
	private Integer selectedPage;
	private Long totalElements;
	private Integer totalPages;

	public PageableProcessoUsuarioResponseDTO(final List<ProcessoUsuarioResponseDTO> processoUsuarios, final Page<Usuario> usuarioPage) {
		this.processoUsuarios.addAll(processoUsuarios);
		this.selectedPage = usuarioPage.getNumber();
		this.totalElements = usuarioPage.getTotalElements();
		this.totalPages = usuarioPage.getTotalPages();
	}

}
