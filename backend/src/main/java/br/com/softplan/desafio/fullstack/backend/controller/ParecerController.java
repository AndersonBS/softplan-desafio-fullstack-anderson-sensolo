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
import br.com.softplan.desafio.fullstack.backend.dto.request.ParecerRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableParecerResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ParecerResponseDTO;
import br.com.softplan.desafio.fullstack.backend.service.ParecerService;
import br.com.softplan.desafio.fullstack.backend.service.ProcessoService;
import br.com.softplan.desafio.fullstack.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller que processa as requisições relacionadas ao parecer.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@RestController
@RequestMapping("/api/parecer")
@Tag(name = "Parecer", description = "Processa as requisições relacionadas ao parecer")
public class ParecerController {

	@Autowired ParecerService parecerService;
	@Autowired ProcessoService processoService;
	@Autowired UsuarioService usuarioService;

	@GetMapping(value = "/get", produces = { "application/json" })
	@Operation(summary = "Lista os pareceres",
			description = "Lista os pareceres cadastrados com paginação [ADMINISTRADOR, FINALIZADOR]", tags = { "Parecer" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista com os pareceres retornada") })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<PageableParecerResponseDTO> getPareceres(@RequestParam(defaultValue = "0") final int selectedPage,
			@RequestParam(defaultValue = "5") final int pageSize) {
		return ResponseEntity.ok(this.parecerService.getPareceres(selectedPage, pageSize));
	}

	@GetMapping(value = "/get/{codigo}", produces = { "application/json" })
	@Operation(summary = "Busca um parecer", description = "Busca um parecer pelo código [ADMINISTRADOR, FINALIZADOR]", tags = { "Parecer" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Parecer retornado"),
			@ApiResponse(responseCode = "400", description = "Código inválido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Parecer não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ParecerResponseDTO> getParecer(@PathVariable final Long codigo) {
		return ResponseEntity.ok(this.parecerService.getParecer(codigo));
	}

	@DeleteMapping(value = "/delete/{codigo}", produces = { "application/json" })
	@Operation(summary = "Apaga um parecer", description = "Apaga um parecer pelo código [ADMINISTRADOR]", tags = { "Parecer" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Parecer apagado"),
			@ApiResponse(responseCode = "400", description = "Código inválido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Parecer não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<Void> deleteParecer(@PathVariable final Long codigo) {
		this.parecerService.deleteParecer(codigo);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/create", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Cria um parecer", description = "Cria um parecer [ADMINISTRADOR, FINALIZADOR]", tags = { "Parecer" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Parecer criado"),
			@ApiResponse(responseCode = "400", description = "Parecer inválido",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ParecerResponseDTO> createParecer(@Valid @RequestBody final ParecerRequestDTO parecerRequestDTO) {
		return new ResponseEntity<>(this.parecerService.createParecer(parecerRequestDTO), HttpStatus.CREATED);
	}

	@PutMapping(value = "/update/{codigo}", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Atualiza um parecer", description = "Atualiza um parecer pelo código [ADMINISTRADOR, FINALIZADOR]", tags = { "Parecer" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Parecer atualizado"),
			@ApiResponse(responseCode = "400", description = "Parecer inválido",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Parecer não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ParecerResponseDTO> updateParecer(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final ParecerRequestDTO parecerRequestDTO) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.parecerService.updateParecer(codigo, parecerRequestDTO));
	}

}
