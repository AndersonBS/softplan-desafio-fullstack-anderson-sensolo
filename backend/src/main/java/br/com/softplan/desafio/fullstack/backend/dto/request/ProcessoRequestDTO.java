package br.com.softplan.desafio.fullstack.backend.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para receber dados de processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 09/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessoRequestDTO implements Serializable {
	
	private static final long serialVersionUID = 3866406022914592778L;

	@NotBlank
    @Size(max = 63)
	private String nome;
	
	@NotBlank
    @Size(max = 255)
	private String descricao;
	
	@NotBlank
	private Long responsavel;
	
}