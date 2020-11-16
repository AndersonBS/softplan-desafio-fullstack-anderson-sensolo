package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import br.com.softplan.desafio.fullstack.backend.model.Processo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar dados de processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 09/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessoResponseDTO implements Serializable {

	private static final long serialVersionUID = -147982709499514773L;

	private Long codigo;
	private String nome;
	private String descricao;
	private String status;
	private String dataInicio;
	private String dataTermino;
	private String responsavel;
	private boolean parecerPendente;

	public ProcessoResponseDTO(final Processo processo, final boolean parecerPendente) {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.codigo = processo.getCodigo();
		this.nome = processo.getNome();
		this.descricao = processo.getDescricao();
		this.status = processo.getStatus().getNome();
		this.dataInicio = simpleDateFormat.format(processo.getDataInicio());
		this.dataTermino = this.dataTermino != null ? simpleDateFormat.format(processo.getDataTermino()) : "";
		this.responsavel = processo.getResponsavel().getCodigoNome();
		this.parecerPendente = parecerPendente;
	}

}
