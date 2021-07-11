package br.com.softplan.desafio.fullstack.backend.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.exception.UsuarioEmailCadastradoException;
import br.com.softplan.desafio.fullstack.backend.exception.UsuarioLoginCadastradoException;
import br.com.softplan.desafio.fullstack.backend.exception.UsuarioNaoEncontradoException;

/**
 * Handler para as exceções relacionadas ao usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 11/07/2021
 */

@ControllerAdvice
public class UsuarioExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = UsuarioNaoEncontradoException.class)
	protected ResponseEntity<Object> handleUsuarioNaoEncontrado(final RuntimeException ex, final WebRequest request) {
		return this.handleExceptionInternal(ex, new MensagemResponseDTO(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(value = { UsuarioLoginCadastradoException.class, UsuarioEmailCadastradoException.class })
	protected ResponseEntity<Object> handleUsuarioDadosCadastrados(final RuntimeException ex, final WebRequest request) {
		return this.handleExceptionInternal(ex, new MensagemResponseDTO(ex.getMessage()), new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

}
