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
import br.com.softplan.desafio.fullstack.backend.dto.request.ProcessoRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ProcessoResponseDTO;
import br.com.softplan.desafio.fullstack.backend.model.Processo;
import br.com.softplan.desafio.fullstack.backend.model.StatusProcesso;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import br.com.softplan.desafio.fullstack.backend.repository.ProcessoRepository;
import br.com.softplan.desafio.fullstack.backend.repository.UsuarioRepository;

/**
 * Classe que processa as requisições relacionadas ao processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/processo")
public class ProcessoController {

	@Autowired ProcessoRepository processoRepository;
	@Autowired UsuarioRepository usuarioRepository;

	@GetMapping("/get")
	public ResponseEntity<List<ProcessoResponseDTO>> getProcessos() {
		final List<ProcessoResponseDTO> processoResponseDTOs = new ArrayList<>();
		for (final Processo processo : this.processoRepository.findAll()) {
			processoResponseDTOs.add(new ProcessoResponseDTO(processo));
		}
		return ResponseEntity.ok(processoResponseDTOs);
	}

	@GetMapping("/get/{codigo}")
	public ResponseEntity<ProcessoResponseDTO> getProcesso(@PathVariable final Long codigo) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigo);
		if (processoOptional.isPresent()) {
			return ResponseEntity.ok(new ProcessoResponseDTO(processoOptional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/delete/{codigo}")
	public ResponseEntity<Void> deleteProcesso(@PathVariable final Long codigo) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigo);
		if (processoOptional.isPresent()) {
			this.processoRepository.deleteById(codigo);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/create")
	public ResponseEntity<MensagemResponseDTO> createProcesso(@Valid @RequestBody final ProcessoRequestDTO processoRequestDTO) {
		final Optional<Usuario> responsavelOptional = this.usuarioRepository.findById(processoRequestDTO.getResponsavel());
		if (!responsavelOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new MensagemResponseDTO("Responsável não encontrado!"));
		}

		final Processo processo = new Processo();
		processo.setNome(processoRequestDTO.getNome());
		processo.setDescricao(processoRequestDTO.getDescricao());
		processo.setStatus(StatusProcesso.NOVO);
		processo.setDataInicio(new Date());
		processo.setResponsavel(responsavelOptional.get());

		this.processoRepository.save(processo);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/update/{codigo}")
	public ResponseEntity<MensagemResponseDTO> updateProcesso(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final ProcessoRequestDTO processoRequestDTO) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigo);
		if (!processoOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		final Optional<Usuario> responsavelOptional = this.usuarioRepository.findById(processoRequestDTO.getResponsavel());
		if (!responsavelOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new MensagemResponseDTO("Responsável não encontrado!"));
		}

		processoOptional.get().setNome(processoRequestDTO.getNome());
		processoOptional.get().setDescricao(processoRequestDTO.getDescricao());
		processoOptional.get().setResponsavel(responsavelOptional.get());

		this.processoRepository.save(processoOptional.get());
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{codigoProcesso}/add/usuario/{codigoUsuario}")
	public ResponseEntity<MensagemResponseDTO> addProcessoUsuario(@PathVariable("codigoProcesso") final long codigoProcesso,
			@PathVariable("codigoUsuario") final long codigoUsuario) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigoProcesso);
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigoUsuario);
		final MensagemResponseDTO mensagemProcessoUsuarioNaoEncontrados =
				this.verificarExistenciaProcessoUsuario(processoOptional, usuarioOptional);
		if (mensagemProcessoUsuarioNaoEncontrados != null) {
			return ResponseEntity.badRequest().body(mensagemProcessoUsuarioNaoEncontrados);
		}
		if (processoOptional.get().getUsuarios().contains(usuarioOptional.get())) {
			return ResponseEntity.badRequest().body(new MensagemResponseDTO("Usuário já cadastrado no processo!"));
		}

		processoOptional.get().getUsuarios().add(usuarioOptional.get());
		this.processoRepository.save(processoOptional.get());

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{codigoProcesso}/remove/usuario/{codigoUsuario}")
	public ResponseEntity<MensagemResponseDTO> removeProcessoUsuario(@PathVariable("codigoProcesso") final long codigoProcesso,
			@PathVariable("codigoUsuario") final long codigoUsuario) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigoProcesso);
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigoUsuario);
		final MensagemResponseDTO mensagemProcessoUsuarioNaoEncontrados =
				this.verificarExistenciaProcessoUsuario(processoOptional, usuarioOptional);
		if (mensagemProcessoUsuarioNaoEncontrados != null) {
			return ResponseEntity.badRequest().body(mensagemProcessoUsuarioNaoEncontrados);
		}

		processoOptional.get().getUsuarios().remove(usuarioOptional.get());
		this.processoRepository.save(processoOptional.get());

		return ResponseEntity.noContent().build();
	}

	private MensagemResponseDTO verificarExistenciaProcessoUsuario(final Optional<Processo> processoOptional,
			final Optional<Usuario> usuarioOptional) {
		if (!processoOptional.isPresent()) {
			return new MensagemResponseDTO("Processo não encontrado!");
		}
		if (!usuarioOptional.isPresent()) {
			return new MensagemResponseDTO("Usuário não encontrado!");
		}
		return null;
	}

}
