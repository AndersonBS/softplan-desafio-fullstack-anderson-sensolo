package br.com.softplan.desafio.fullstack.backend.exception;

/**
 * Exceção lançada quando o processo não foi encontrado ao criar/atualizar um parecer.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/07/2021
 */

public class ParecerProcessoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 756880113497250661L;

	public ParecerProcessoNaoEncontradoException(final String message) {
		super(message);
	}

}