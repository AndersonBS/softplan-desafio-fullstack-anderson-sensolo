package br.com.softplan.desafio.fullstack.backend.models;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa um parecer de um processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Data
@NoArgsConstructor
public class Parecer {

	private Long codigo;
	private String descricao;
	private Date data;
	private Processo processo;
	private Usuario autor;

}
