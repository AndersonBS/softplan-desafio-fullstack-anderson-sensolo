package br.com.softplan.desafio.fullstack.backend.security.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.softplan.desafio.fullstack.backend.security.dto.request.JwtRequestDTO;
import br.com.softplan.desafio.fullstack.backend.security.dto.response.JwtResponseDTO;
import br.com.softplan.desafio.fullstack.backend.security.jwt.JwtTokenUtil;
import br.com.softplan.desafio.fullstack.backend.security.model.JwtUserDetails;
import br.com.softplan.desafio.fullstack.backend.security.service.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Classe que processa as requisições de autenticação de usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/11/2020
 */

@RestController
@RequestMapping("/api/authenticate")
@Tag(name = "Autenticação", description = "Processa as requisições de autenticação dos usuários")
public class AuthenticationController {

	@Autowired JwtTokenUtil jwtTokenUtil;
	@Autowired JwtUserDetailsService jwtUserDetailsService;
	@Autowired AuthenticationManager authenticationManager;

	@PostMapping(consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Autentica um usuário", description = "Autentica um usuário no sistema", tags = { "Autenticação" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário autenticado"),
			@ApiResponse(responseCode = "404", description = "Usuário inválido", content = @Content) })
	public ResponseEntity<JwtResponseDTO> authenticate(@Valid @RequestBody final JwtRequestDTO jwtRequestDTO) {
		final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(jwtRequestDTO.getUsername());
		if (userDetails == null) {
			return ResponseEntity.notFound().build();
		}
		this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				jwtRequestDTO.getUsername(), jwtRequestDTO.getPassword()));
		final String jwtToken = this.jwtTokenUtil.generateJwtToken(userDetails);
		return ResponseEntity.ok(new JwtResponseDTO(jwtToken, (JwtUserDetails) userDetails));
	}

}
