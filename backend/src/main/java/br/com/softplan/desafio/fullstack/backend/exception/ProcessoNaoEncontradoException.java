package br.com.softplan.desafio.fullstack.backend.exception;

/**
 * Exceção lançada quanto um processo não foi encontrado.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 11/07/2021
 */

public class ProcessoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1903983389413191105L;

	public ProcessoNaoEncontradoException(final String message) {
		super(message);
	}

}