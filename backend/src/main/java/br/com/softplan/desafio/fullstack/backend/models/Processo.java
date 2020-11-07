package br.com.softplan.desafio.fullstack.backend.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa um processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Data
@NoArgsConstructor
public class Processo {

	private Long codigo;
	private String nome;
	private String descricao;
	private StatusProcesso status;
	private Date dataInicio;
	private Date dataTermino;
	private Usuario responsavel;
	private List<Usuario> usuarios = new ArrayList<>();
	private List<Parecer> pareceres = new ArrayList<>();

}
