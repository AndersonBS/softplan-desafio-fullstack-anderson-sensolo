package br.com.softplan.desafio.fullstack.backend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.softplan.desafio.fullstack.backend.dto.request.ParecerRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ParecerResponseDTO;
import br.com.softplan.desafio.fullstack.backend.model.Parecer;
import br.com.softplan.desafio.fullstack.backend.model.Processo;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import br.com.softplan.desafio.fullstack.backend.repository.ParecerRepository;
import br.com.softplan.desafio.fullstack.backend.repository.ProcessoRepository;
import br.com.softplan.desafio.fullstack.backend.repository.UsuarioRepository;

/**
 * Classe que processa as requisições relacionadas ao parecer.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/parecer")
public class ParecerController {

	@Autowired ParecerRepository parecerRepository;
	@Autowired ProcessoRepository processoRepository;
	@Autowired UsuarioRepository usuarioRepository;

	@GetMapping("/get")
	public ResponseEntity<List<ParecerResponseDTO>> getPareceres() {
		final List<ParecerResponseDTO> parecerResponseDTOs = new ArrayList<>();
		for (final Parecer parecer : this.parecerRepository.findAll()) {
			parecerResponseDTOs.add(new ParecerResponseDTO(parecer));
		}
		return ResponseEntity.ok(parecerResponseDTOs);
	}

	@GetMapping("/get/{codigo}")
	public ResponseEntity<ParecerResponseDTO> getParecer(@PathVariable final Long codigo) {
		final Optional<Parecer> parecerOptional = this.parecerRepository.findById(codigo);
		if (parecerOptional.isPresent()) {
			return ResponseEntity.ok(new ParecerResponseDTO(parecerOptional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/delete/{codigo}")
	public ResponseEntity<Void> deleteParecer(@PathVariable final Long codigo) {
		final Optional<Parecer> parecerOptional = this.parecerRepository.findById(codigo);
		if (parecerOptional.isPresent()) {
			this.parecerRepository.deleteById(codigo);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/create")
	public ResponseEntity<MensagemResponseDTO> createParecer(@Valid @RequestBody final ParecerRequestDTO parecerRequestDTO) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(parecerRequestDTO.getProcesso());
		final Optional<Usuario> autorOptional = this.usuarioRepository.findById(parecerRequestDTO.getAutor());
		final MensagemResponseDTO mensagemProcessoAutorNaoEncontrados =
				this.verificarExistenciaProcessoAutor(processoOptional, autorOptional);
		if (mensagemProcessoAutorNaoEncontrados != null) {
			return ResponseEntity.badRequest().body(mensagemProcessoAutorNaoEncontrados);
		}

		final Parecer parecer = new Parecer();
		parecer.setDescricao(parecerRequestDTO.getDescricao());
		parecer.setData(new Date());
		parecer.setProcesso(processoOptional.get());
		parecer.setAutor(autorOptional.get());

		this.parecerRepository.save(parecer);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/update/{codigo}")
	public ResponseEntity<MensagemResponseDTO> updateParecer(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final ParecerRequestDTO parecerRequestDTO) {
		final Optional<Parecer> parecerOptional = this.parecerRepository.findById(codigo);
		if (!parecerOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		final Optional<Processo> processoOptional = this.processoRepository.findById(parecerRequestDTO.getProcesso());
		final Optional<Usuario> autorOptional = this.usuarioRepository.findById(parecerRequestDTO.getAutor());
		final MensagemResponseDTO mensagemProcessoAutorNaoEncontrados =
				this.verificarExistenciaProcessoAutor(processoOptional, autorOptional);
		if (mensagemProcessoAutorNaoEncontrados != null) {
			return ResponseEntity.badRequest().body(mensagemProcessoAutorNaoEncontrados);
		}

		parecerOptional.get().setDescricao(parecerRequestDTO.getDescricao());
		parecerOptional.get().setData(new Date());
		parecerOptional.get().setProcesso(processoOptional.get());
		parecerOptional.get().setAutor(autorOptional.get());

		this.parecerRepository.save(parecerOptional.get());
		return ResponseEntity.noContent().build();
	}

	private MensagemResponseDTO verificarExistenciaProcessoAutor(final Optional<Processo> processoOptional,
			final Optional<Usuario> autorOptional) {
		if (!processoOptional.isPresent()) {
			return new MensagemResponseDTO("Processo não encontrado!");
		}
		if (!autorOptional.isPresent()) {
			return new MensagemResponseDTO("Autor não encontrado!");
		}
		return null;
	}

}
