package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar dados de usuários com paginação.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 13/03/2021
 */

@Data
@NoArgsConstructor
public class PageableUsuarioResponseDTO implements Serializable {

	private static final long serialVersionUID = 2480212332263251931L;

	private final List<UsuarioResponseDTO> usuarios = new ArrayList<>();
	private Integer selectedPage;
	private Long totalElements;
	private Integer totalPages;

	public PageableUsuarioResponseDTO(final Page<Usuario> usuarioPage) {
		for (final Usuario usuario : usuarioPage.getContent()) {
			this.usuarios.add(new UsuarioResponseDTO(usuario));
		}
		this.selectedPage = usuarioPage.getNumber();
		this.totalElements = usuarioPage.getTotalElements();
		this.totalPages = usuarioPage.getTotalPages();
	}

}
