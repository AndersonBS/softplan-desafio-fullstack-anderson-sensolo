package br.com.softplan.desafio.fullstack.backend.exception;

/**
 * Exceção lançada quando o e-mail já está cadastrado em outro usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 11/07/2021
 */

public class UsuarioEmailCadastradoException extends RuntimeException {

	private static final long serialVersionUID = -6996660336338016060L;

	public UsuarioEmailCadastradoException(final String message) {
		super(message);
	}

}