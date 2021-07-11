package br.com.softplan.desafio.fullstack.backend.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.softplan.desafio.fullstack.backend.dto.request.UsuarioRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableUsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.UsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.service.UsuarioService;

/**
 * Controller que processa as requisições relacionadas ao usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	@Autowired UsuarioService usuarioService;

	/**
	 * Lista os usuários com paginação
	 * @param selectedPage
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/get")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<PageableUsuarioResponseDTO> getUsuarios(
			@RequestParam(defaultValue = "0") final int selectedPage, @RequestParam(defaultValue = "5") final int pageSize) {
		return ResponseEntity.ok(this.usuarioService.getUsuarios(selectedPage, pageSize));
	}

	/**
	 * Busca um usuário pelo código
	 * @param codigo
	 * @return
	 */
	@GetMapping("/get/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<UsuarioResponseDTO> getUsuario(@PathVariable final Long codigo) {
		return ResponseEntity.ok(this.usuarioService.getUsuario(codigo));
	}

	/**
	 * Apaga um usuário pelo código
	 * @param codigo
	 * @return
	 */
	@DeleteMapping("/delete/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<Void> deleteUsuario(@PathVariable final Long codigo) {
		this.usuarioService.deleteUsuario(codigo);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Cria um usuário
	 * @param usuarioRequestDTO
	 * @return
	 */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<UsuarioResponseDTO> createUsuario(@Valid @RequestBody final UsuarioRequestDTO usuarioRequestDTO) {
		return new ResponseEntity<>(this.usuarioService.createUsuario(usuarioRequestDTO), HttpStatus.CREATED);
	}

	/**
	 * Atualiza um usuário pelo código
	 * @param codigo
	 * @param usuarioRequestDTO
	 * @return
	 */
	@PutMapping("/update/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<UsuarioResponseDTO> updateUsuario(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final UsuarioRequestDTO usuarioRequestDTO) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.usuarioService.updateUsuario(codigo, usuarioRequestDTO));
	}

}
