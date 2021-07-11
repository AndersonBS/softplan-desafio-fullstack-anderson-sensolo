package br.com.softplan.desafio.fullstack.backend.exception;

/**
 * Exceção lançada quando o login já está cadastrado em outro usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 11/07/2021
 */

public class UsuarioLoginCadastradoException extends RuntimeException {

	private static final long serialVersionUID = -1925046488336365221L;

	public UsuarioLoginCadastradoException(final String message) {
		super(message);
	}

}