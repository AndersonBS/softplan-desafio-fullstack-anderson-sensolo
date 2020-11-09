package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import java.util.Date;
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
	private Date dataInicio;
	private Date dataTermino;
	private String responsavel;

	public ProcessoResponseDTO(final Processo processo) {
		this.codigo = processo.getCodigo();
		this.nome = processo.getNome();
		this.descricao = processo.getDescricao();
		this.status = processo.getStatus().getNome();
		this.dataInicio = processo.getDataInicio();
		this.dataTermino = processo.getDataTermino();
		this.responsavel = processo.getResponsavel().getCodigoNome();
	}

}
