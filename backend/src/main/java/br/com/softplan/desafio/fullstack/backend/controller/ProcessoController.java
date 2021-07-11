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

/**
 * Controller que processa as requisições relacionadas ao processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@RestController
@RequestMapping("/api/processo")
public class ProcessoController {

	@Autowired ParecerService parecerService;
	@Autowired ProcessoService processoService;
	@Autowired UsuarioService usuarioService;

	/**
	 * Lista os processos
	 * @return
	 */
	@GetMapping("/get")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<PageableProcessoResponseDTO> getProcessos(
			@RequestParam(defaultValue = "0") final int selectedPage, @RequestParam(defaultValue = "5") final int pageSize) {
		return ResponseEntity.ok(this.processoService.getProcessos(selectedPage, pageSize));
	}

	/**
	 * Busca um processo pelo código
	 * @param codigo
	 * @return
	 */
	@GetMapping("/get/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ProcessoResponseDTO> getProcesso(@PathVariable final Long codigo) {
		return ResponseEntity.ok(this.processoService.getProcesso(codigo));
	}

	/**
	 * Apaga um processo pelo código
	 * @param codigo
	 * @return
	 */
	@DeleteMapping("/delete/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<Void> deleteProcesso(@PathVariable final Long codigo) {
		this.processoService.deleteProcesso(codigo);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Cria um processo
	 * @param processoRequestDTO
	 * @return
	 */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<ProcessoResponseDTO> createProcesso(@Valid @RequestBody final ProcessoRequestDTO processoRequestDTO) {
		return new ResponseEntity<>(this.processoService.createProcesso(processoRequestDTO), HttpStatus.CREATED);
	}

	/**
	 * Atualiza um processo pelo código
	 * @param codigo
	 * @param processoRequestDTO
	 * @return
	 */
	@PutMapping("/update/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<ProcessoResponseDTO> updateProcesso(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final ProcessoRequestDTO processoRequestDTO) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.processoService.updateProcesso(codigo, processoRequestDTO));
	}

	/**
	 * Vincula um usuário a um processo para prestar parecer
	 * @param codigoProcesso
	 * @param codigoUsuario
	 * @return
	 */
	@PutMapping("/{codigoProcesso}/add/usuario/{codigoUsuario}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<ProcessoUsuarioResponseDTO> addProcessoUsuario(@PathVariable("codigoProcesso") final long codigoProcesso,
			@PathVariable("codigoUsuario") final long codigoUsuario) {
		return new ResponseEntity<>(this.processoService.addProcessoUsuario(codigoProcesso, codigoUsuario), HttpStatus.CREATED);
	}

	/**
	 * Remove o vínculo de um usuário do processo
	 * @param codigoProcesso
	 * @param codigoUsuario
	 * @return
	 */
	@PutMapping("/{codigoProcesso}/remove/usuario/{codigoUsuario}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<MensagemResponseDTO> removeProcessoUsuario(@PathVariable("codigoProcesso") final long codigoProcesso,
			@PathVariable("codigoUsuario") final long codigoUsuario) {
		this.processoService.removeProcessoUsuario(codigoProcesso, codigoUsuario);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Lista os usuários vinculados ao processo para prestar parecer
	 * @param codigoProcesso
	 * @param selectedPage
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/{codigoProcesso}/get/usuarios")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<PageableProcessoUsuarioResponseDTO> getProcessoUsuarios(@PathVariable("codigoProcesso") final long codigoProcesso,
			@RequestParam(defaultValue = "0") final int selectedPage, @RequestParam(defaultValue = "5") final int pageSize) {
		return ResponseEntity.ok(this.processoService.getProcessoUsuarios(codigoProcesso, selectedPage, pageSize));
	}

	/**
	 * Lista os usuários que podem ser responsáveis por processos
	 * @return
	 */
	@GetMapping("/get/responsaveis")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<List<ResponsavelResponseDTO>> getResponsaveis() {
		return ResponseEntity.ok(this.processoService.getResponsaveis());
	}

	/**
	 * Lista os usuários que podem incluir pareceres nos processos
	 * @param codigoProcesso
	 * @return
	 */
	@GetMapping("/{codigoProcesso}/get/finalizadores")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<List<ResponsavelResponseDTO>> getFinalizadores(@PathVariable("codigoProcesso") final long codigoProcesso) {
		return ResponseEntity.ok(this.processoService.getFinalizadores(codigoProcesso));
	}

}
