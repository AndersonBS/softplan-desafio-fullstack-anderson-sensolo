package br.com.softplan.desafio.fullstack.backend.exception;

/**
 * Exceção lançada quanto um usuário não foi encontrado.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/07/2021
 */

public class UsuarioNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 7148444677329357618L;

	public UsuarioNaoEncontradoException(final String message) {
		super(message);
	}

}
