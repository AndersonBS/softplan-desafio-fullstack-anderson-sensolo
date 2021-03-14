package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import br.com.softplan.desafio.fullstack.backend.model.Processo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar dados de processos com paginação.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 13/03/2021
 */

@Data
@NoArgsConstructor
public class PageableProcessoResponseDTO implements Serializable {

	private static final long serialVersionUID = 7694193600116120355L;

	private final List<ProcessoResponseDTO> processos = new ArrayList<>();
	private Integer selectedPage;
	private Long totalElements;
	private Integer totalPages;

	public PageableProcessoResponseDTO(final List<ProcessoResponseDTO> processos, final Page<Processo> processoPage) {
		this.processos.addAll(processos);
		this.selectedPage = processoPage.getNumber();
		this.totalElements = processoPage.getTotalElements();
		this.totalPages = processoPage.getTotalPages();
	}

}
