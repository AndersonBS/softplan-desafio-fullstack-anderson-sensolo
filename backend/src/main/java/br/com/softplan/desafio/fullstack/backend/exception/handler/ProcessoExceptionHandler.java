package br.com.softplan.desafio.fullstack.backend.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.exception.ProcessoNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.exception.ProcessoResponsavelNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.exception.ProcessoUsuarioCadastradoException;
import br.com.softplan.desafio.fullstack.backend.exception.ProcessoUsuarioNaoEncontradoException;

/**
 * Handler para as exceções relacionadas ao processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 11/07/2021
 */

@ControllerAdvice
public class ProcessoExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = ProcessoNaoEncontradoException.class)
	protected ResponseEntity<Object> handleProcessoNaoEncontrado(final RuntimeException ex, final WebRequest request) {
		return this.handleExceptionInternal(ex, new MensagemResponseDTO(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(value = { ProcessoResponsavelNaoEncontradoException.class, ProcessoUsuarioNaoEncontradoException.class,
			ProcessoUsuarioCadastradoException.class })
	protected ResponseEntity<Object> handleProcessoResponsavelNaoEncontrado(final RuntimeException ex, final WebRequest request) {
		return this.handleExceptionInternal(ex, new MensagemResponseDTO(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

}
