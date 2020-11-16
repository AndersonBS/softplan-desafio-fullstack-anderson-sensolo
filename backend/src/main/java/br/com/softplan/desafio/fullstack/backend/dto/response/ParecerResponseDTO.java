package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import br.com.softplan.desafio.fullstack.backend.model.Parecer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar dados de parecer.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 09/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParecerResponseDTO implements Serializable {

	private static final long serialVersionUID = -3380921525570000459L;

	private Long codigo;
	private String descricao;
	private String data;
	private String processo;
	private String autor;

	public ParecerResponseDTO(final Parecer parecer) {
		this.codigo = parecer.getCodigo();
		this.descricao = parecer.getDescricao();
		this.data = new SimpleDateFormat("dd/MM/yyyy").format(parecer.getData());
		this.processo = parecer.getProcesso().getCodigoNome();
		this.autor = parecer.getAutor().getCodigoNome();
	}

}
