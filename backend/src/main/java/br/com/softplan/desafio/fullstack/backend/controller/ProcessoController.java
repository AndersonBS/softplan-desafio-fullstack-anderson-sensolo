package br.com.softplan.desafio.fullstack.backend.controller;

import java.util.List;
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
import br.com.softplan.desafio.fullstack.backend.dto.request.ProcessoRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableProcessoResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableProcessoUsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ProcessoResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ProcessoUsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ResponsavelResponseDTO;
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
 * Controller que processa as requisições relacionadas ao processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@RestController
@RequestMapping("/api/processo")
@Tag(name = "Processo", description = "Processa as requisições relacionadas ao processo")
public class ProcessoController {

	@Autowired ParecerService parecerService;
	@Autowired ProcessoService processoService;
	@Autowired UsuarioService usuarioService;

	@GetMapping(value = "/get", produces = { "application/json" })
	@Operation(summary = "Lista os processos",
			description = "Lista os processos cadastrados com paginação [ADMINISTRADOR, TRIADOR, FINALIZADOR]", tags = { "Processo" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista com os processos retornada") })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<PageableProcessoResponseDTO> getProcessos(
			@RequestParam(defaultValue = "0") final int selectedPage, @RequestParam(defaultValue = "5") final int pageSize) {
		return ResponseEntity.ok(this.processoService.getProcessos(selectedPage, pageSize));
	}

	@GetMapping(value = "/get/{codigo}", produces = { "application/json" })
	@Operation(summary = "Busca um processo",
			description = "Busca um processo pelo código [ADMINISTRADOR, TRIADOR, FINALIZADOR]", tags = { "Processo" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Processo retornado"),
			@ApiResponse(responseCode = "400", description = "Código inválido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Processo não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ProcessoResponseDTO> getProcesso(@PathVariable final Long codigo) {
		return ResponseEntity.ok(this.processoService.getProcesso(codigo));
	}

	@DeleteMapping(value = "/delete/{codigo}", produces = { "application/json" })
	@Operation(summary = "Apaga um processo", description = "Apaga um processo pelo código [ADMINISTRADOR, TRIADOR]", tags = { "Processo" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Processo apagado"),
			@ApiResponse(responseCode = "400", description = "Código inválido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Processo não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<Void> deleteProcesso(@PathVariable final Long codigo) {
		this.processoService.deleteProcesso(codigo);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/create", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Cria um processo", description = "Cria um processo [ADMINISTRADOR, TRIADOR]", tags = { "Processo" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Processo criado"),
			@ApiResponse(responseCode = "400", description = "Processo inválido",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<ProcessoResponseDTO> createProcesso(@Valid @RequestBody final ProcessoRequestDTO processoRequestDTO) {
		return new ResponseEntity<>(this.processoService.createProcesso(processoRequestDTO), HttpStatus.CREATED);
	}

	@PutMapping(value = "/update/{codigo}", consumes = { "application/json" }, produces = { "application/json" })
	@Operation(summary = "Atualiza um processo", description = "Atualiza um processo pelo código [ADMINISTRADOR, TRIADOR]", tags = { "Processo" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Processo atualizado"),
			@ApiResponse(responseCode = "400", description = "Processo inválido",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Processo não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })

	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<ProcessoResponseDTO> updateProcesso(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final ProcessoRequestDTO processoRequestDTO) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.processoService.updateProcesso(codigo, processoRequestDTO));
	}

	@PutMapping(value = "/{codigoProcesso}/add/usuario/{codigoUsuario}", produces = { "application/json" })
	@Operation(summary = "Vincula um usuário a um processo",
			description = "Vincula um usuário a um processo para prestar parecer [ADMINISTRADOR, TRIADOR]", tags = { "Processo" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuário vinculado ao processo"),
			@ApiResponse(responseCode = "400", description = "Usuário inválido",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Processo não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<ProcessoUsuarioResponseDTO> addProcessoUsuario(@PathVariable("codigoProcesso") final long codigoProcesso,
			@PathVariable("codigoUsuario") final long codigoUsuario) {
		return new ResponseEntity<>(this.processoService.addProcessoUsuario(codigoProcesso, codigoUsuario), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{codigoProcesso}/remove/usuario/{codigoUsuario}", produces = { "application/json" })
	@Operation(summary = "Remove um usuário do processo",
			description = "Remove o vínculo de um usuário do processo [ADMINISTRADOR, TRIADOR]", tags = { "Processo" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Processo apagado"),
			@ApiResponse(responseCode = "400", description = "Código inválido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Processo não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<MensagemResponseDTO> removeProcessoUsuario(@PathVariable("codigoProcesso") final long codigoProcesso,
			@PathVariable("codigoUsuario") final long codigoUsuario) {
		this.processoService.removeProcessoUsuario(codigoProcesso, codigoUsuario);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{codigoProcesso}/get/usuarios", produces = { "application/json" })
	@Operation(summary = "Lista os usuários do processo", tags = { "Processo" },
			description = "Lista os usuários vinculados ao processo para prestar parecer com paginação [ADMINISTRADOR, TRIADOR]")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista com os usuários do processo retornada"),
			@ApiResponse(responseCode = "400", description = "Código inválido", content = @Content),
			@ApiResponse(responseCode = "404", description = "Processo não encontrado",
					content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<PageableProcessoUsuarioResponseDTO> getProcessoUsuarios(@PathVariable("codigoProcesso") final long codigoProcesso,
			@RequestParam(defaultValue = "0") final int selectedPage, @RequestParam(defaultValue = "5") final int pageSize) {
		return ResponseEntity.ok(this.processoService.getProcessoUsuarios(codigoProcesso, selectedPage, pageSize));
	}

	@GetMapping(value = "/get/responsaveis", produces = { "application/json" })
	@Operation(summary = "Lista os usuários que podem ser responsáveis por processos",
			description = "Lista os usuários que podem ser responsáveis por processos [ADMINISTRADOR, TRIADOR]", tags = { "Processo" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista com os usuários retornada") })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<List<ResponsavelResponseDTO>> getResponsaveis() {
		return ResponseEntity.ok(this.processoService.getResponsaveis());
	}

	@GetMapping(value = "/{codigoProcesso}/get/finalizadores", produces = { "application/json" })
	@Operation(summary = "Lista os usuários que podem incluir pareceres nos processos",
			description = "Lista os usuários que podem incluir pareceres nos processos [ADMINISTRADOR, TRIADOR]", tags = { "Processo" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista com os usuários retornada"),
			@ApiResponse(responseCode = "400", description = "Código inválido", content = @Content) })
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<List<ResponsavelResponseDTO>> getFinalizadores(@PathVariable("codigoProcesso") final long codigoProcesso) {
		return ResponseEntity.ok(this.processoService.getFinalizadores(codigoProcesso));
	}

}
