package br.com.softplan.desafio.fullstack.backend.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.exception.ParecerAutorNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.exception.ParecerNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.exception.ParecerProcessoNaoEncontradoException;

/**
 * Handler para as exceções relacionadas ao parecer.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/07/2021
 */

@ControllerAdvice
public class ParecerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = ParecerNaoEncontradoException.class)
	protected ResponseEntity<Object> handleParecerNaoEncontrado(final RuntimeException ex, final WebRequest request) {
		return this.handleExceptionInternal(ex, new MensagemResponseDTO(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(value = { ParecerProcessoNaoEncontradoException.class, ParecerAutorNaoEncontradoException.class })
	protected ResponseEntity<Object> handleParecerProcessoAutorNaoEncontrado(final RuntimeException ex, final WebRequest request) {
		return this.handleExceptionInternal(ex, new MensagemResponseDTO(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

}
