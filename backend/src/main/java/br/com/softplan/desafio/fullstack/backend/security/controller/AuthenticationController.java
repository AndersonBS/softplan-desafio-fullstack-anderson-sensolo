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

/**
 * Classe que processa as requisições de autenticação de usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/11/2020
 */

@RestController
@RequestMapping("/api/authenticate")
public class AuthenticationController {

	@Autowired JwtTokenUtil jwtTokenUtil;
	@Autowired JwtUserDetailsService jwtUserDetailsService;
	@Autowired AuthenticationManager authenticationManager;

	@PostMapping
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
