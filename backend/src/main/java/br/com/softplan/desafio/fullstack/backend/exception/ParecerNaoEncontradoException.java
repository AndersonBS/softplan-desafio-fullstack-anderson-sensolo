package br.com.softplan.desafio.fullstack.backend.exception;

/**
 * Exceção lançada quanto um parecer não foi encontrado.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/07/2021
 */

public class ParecerNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 7157984288122294952L;

	public ParecerNaoEncontradoException(final String message) {
		super(message);
	}

}
