package br.com.softplan.desafio.fullstack.backend.exception;

/**
 * Exceção lançada quando o usuário já está vinculado ao processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 11/07/2021
 */

public class ProcessoUsuarioCadastradoException extends RuntimeException {

	private static final long serialVersionUID = -7423086874636759339L;

	public ProcessoUsuarioCadastradoException(final String message) {
		super(message);
	}

}