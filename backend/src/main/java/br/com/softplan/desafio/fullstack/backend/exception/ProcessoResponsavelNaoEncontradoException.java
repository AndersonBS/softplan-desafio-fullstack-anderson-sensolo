package br.com.softplan.desafio.fullstack.backend.exception;

/**
 * Exceção lançada quando o responsável não foi encontrado ao criar/atualizar um processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 11/07/2021
 */

public class ProcessoResponsavelNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = -541936597316831493L;

	public ProcessoResponsavelNaoEncontradoException(final String message) {
		super(message);
	}

}