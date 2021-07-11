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
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableParecerResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ParecerResponseDTO;
import br.com.softplan.desafio.fullstack.backend.service.ParecerService;
import br.com.softplan.desafio.fullstack.backend.service.ProcessoService;
import br.com.softplan.desafio.fullstack.backend.service.UsuarioService;

/**
 * Controller que processa as requisições relacionadas ao parecer.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@RestController
@RequestMapping("/api/parecer")
public class ParecerController {

	@Autowired ParecerService parecerService;
	@Autowired ProcessoService processoService;
	@Autowired UsuarioService usuarioService;

	/**
	 * Lista os pareceres
	 * @return
	 */
	@GetMapping("/get")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<PageableParecerResponseDTO> getPareceres(
			@RequestParam(defaultValue = "0") final int selectedPage, @RequestParam(defaultValue = "5") final int pageSize) {
		return ResponseEntity.ok(this.parecerService.getPareceres(selectedPage, pageSize));
	}

	/**
	 * Busca um parecer pelo código
	 * @param codigo
	 * @return
	 */
	@GetMapping("/get/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ParecerResponseDTO> getParecer(@PathVariable final Long codigo) {
		return ResponseEntity.ok(this.parecerService.getParecer(codigo));
	}

	/**
	 * Apaga um parecer pelo código
	 * @param codigo
	 * @return
	 */
	@DeleteMapping("/delete/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<Void> deleteParecer(@PathVariable final Long codigo) {
		this.parecerService.deleteParecer(codigo);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Cria um parecer
	 * @param parecerRequestDTO
	 * @return
	 */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ParecerResponseDTO> createParecer(@Valid @RequestBody final ParecerRequestDTO parecerRequestDTO) {
		return new ResponseEntity<>(this.parecerService.createParecer(parecerRequestDTO), HttpStatus.CREATED);
	}

	/**
	 * Atualiza um parecer pelo código
	 * @param codigo
	 * @param parecerRequestDTO
	 * @return
	 */
	@PutMapping("/update/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ParecerResponseDTO> updateParecer(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final ParecerRequestDTO parecerRequestDTO) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.parecerService.updateParecer(codigo, parecerRequestDTO));
	}

}
