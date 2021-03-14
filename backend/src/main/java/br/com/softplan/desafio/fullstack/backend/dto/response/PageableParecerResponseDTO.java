package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import br.com.softplan.desafio.fullstack.backend.model.Parecer;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar dados de pareceres com paginação.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 14/03/2021
 */

@Data
@NoArgsConstructor
public class PageableParecerResponseDTO implements Serializable {

	private static final long serialVersionUID = -8107992935295762647L;

	private final List<ParecerResponseDTO> pareceres = new ArrayList<>();
	private Integer selectedPage;
	private Long totalElements;
	private Integer totalPages;

	public PageableParecerResponseDTO(final List<ParecerResponseDTO> pareceres, final Page<Parecer> parecerPage) {
		this.pareceres.addAll(pareceres);
		this.selectedPage = parecerPage.getNumber();
		this.totalElements = parecerPage.getTotalElements();
		this.totalPages = parecerPage.getTotalPages();
	}

}
