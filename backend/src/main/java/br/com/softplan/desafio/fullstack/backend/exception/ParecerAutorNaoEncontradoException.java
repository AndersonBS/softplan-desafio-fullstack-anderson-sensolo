package br.com.softplan.desafio.fullstack.backend.exception;

/**
 * Exceção lançada quando o autor não foi encontrado ao criar/atualizar um parecer.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/07/2021
 */

public class ParecerAutorNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 3251609187505421423L;

	public ParecerAutorNaoEncontradoException(final String message) {
		super(message);
	}

}