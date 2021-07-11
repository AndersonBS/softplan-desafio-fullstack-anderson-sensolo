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
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableUsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.UsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller que processa as requisições relacionadas ao usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Usuário", description = "Processa as requisições relacionadas ao usuário")
public class UsuarioController {

	@Autowired UsuarioService usuarioService;

	@GetMapping(value = "/get", produces = { "application/json" })
	@Operation(summary = "Lista os usuários",
			description = "Lista os usuários cadastrados com paginação [ADMINISTRADOR]", tags = { "Usuário" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista com os usuários retornada") })
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<PageableUsuarioResponseDTO> getUsuarios(@RequestParam(defaultValue = "0") final int selectedPage,
			@RequestParam(defaultValue = "5") final int pageSize) {
		return ResponseEntity.ok(this.usuarioService.getUsuarios(selectedPage, pageSize));
	}

	@GetMapping(value = "/get/{codigo}", produces = { "application/json" })
	@Operation(summary = "Busca um usuário", description = "Busca um usuário pelo código [ADMINISTRADOR]", tags = { "Usuário" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário retornado"),
			@ApiResponse(responseCode = "400", description = "Código inválido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<UsuarioResponseDTO> getUsuario(@PathVariable final Long codigo) {
		return ResponseEntity.ok(this.usuarioService.getUsuario(codigo));
	}

	@DeleteMapping(value = "/delete/{codigo}", produces = { "application/json" })
	@Operation(summary = "Apaga um usuário", description = "Apaga um usuário pelo código [ADMINISTRADOR]", tags = { "Usuário" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Usuário apagado"),
			@ApiResponse(responseCode = "400", description = "Código inválido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<Void> deleteUsuario(@PathVariable final Long codigo) {
		this.usuarioService.deleteUsuario(codigo);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/create", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Cria um usuário", description = "Cria um usuário [ADMINISTRADOR]", tags = { "Usuário" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuário criado"),
			@ApiResponse(responseCode = "400", description = "Usuário inválido",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<UsuarioResponseDTO> createUsuario(@Valid @RequestBody final UsuarioRequestDTO usuarioRequestDTO) {
		return new ResponseEntity<>(this.usuarioService.createUsuario(usuarioRequestDTO), HttpStatus.CREATED);
	}

	@PutMapping(value = "/update/{codigo}", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Atualiza um usuário", description = "Atualiza um usuário pelo código [ADMINISTRADOR]", tags = { "Usuário" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Usuário atualizado"),
			@ApiResponse(responseCode = "400", description = "Usuário inválido",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<UsuarioResponseDTO> updateUsuario(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final UsuarioRequestDTO usuarioRequestDTO) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.usuarioService.updateUsuario(codigo, usuarioRequestDTO));
	}

}
