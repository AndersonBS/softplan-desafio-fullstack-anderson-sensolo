package br.com.softplan.desafio.fullstack.backend.exception;

/**
 * Exceção lançada quando o usuário não foi encontrado ao adicionar/remover seu vínculo com um processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 11/07/2021
 */

public class ProcessoUsuarioNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 7157984288122294952L;

	public ProcessoUsuarioNaoEncontradoException(final String message) {
		super(message);
	}

}