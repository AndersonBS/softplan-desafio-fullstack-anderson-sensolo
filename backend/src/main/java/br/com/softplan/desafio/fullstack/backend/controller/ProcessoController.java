package br.com.softplan.desafio.fullstack.backend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import br.com.softplan.desafio.fullstack.backend.dto.response.ProcessoUsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.model.PermissaoUsuario;
import br.com.softplan.desafio.fullstack.backend.model.Processo;
import br.com.softplan.desafio.fullstack.backend.model.StatusProcesso;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import br.com.softplan.desafio.fullstack.backend.repository.ProcessoRepository;
import br.com.softplan.desafio.fullstack.backend.repository.UsuarioRepository;
import br.com.softplan.desafio.fullstack.backend.security.model.JwtUserDetails;

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
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<List<ProcessoResponseDTO>> getProcessos() {
		final List<ProcessoResponseDTO> processoResponseDTOs = new ArrayList<>();
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(PermissaoUsuario.FINALIZADOR.name()))) {
			for (final Processo processo : this.processoRepository.findAllPendentesUsuarioFinalizador(
					((JwtUserDetails) authentication.getPrincipal()).getId())) {
				processoResponseDTOs.add(new ProcessoResponseDTO(processo));
			}
		} else {
			for (final Processo processo : this.processoRepository.findAll()) {
				processoResponseDTOs.add(new ProcessoResponseDTO(processo));
			}
		}
		return ResponseEntity.ok(processoResponseDTOs);
	}

	@GetMapping("/get/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ProcessoResponseDTO> getProcesso(@PathVariable final Long codigo) {
		Optional<Processo> processoOptional;
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(PermissaoUsuario.FINALIZADOR.name()))) {
			processoOptional = this.processoRepository.findPendenteUsuarioFinalizador(
					((JwtUserDetails) authentication.getPrincipal()).getId(), codigo);
		} else {
			processoOptional = this.processoRepository.findById(codigo);
		}
		if (processoOptional.isPresent()) {
			return ResponseEntity.ok(new ProcessoResponseDTO(processoOptional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/delete/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<Void> deleteProcesso(@PathVariable final Long codigo) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigo);
		if (processoOptional.isPresent()) {
			this.processoRepository.deleteById(codigo);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
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
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
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
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
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
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
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

	@GetMapping("/{codigoProcesso}/get/usuarios")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<List<ProcessoUsuarioResponseDTO>> getProcessoUsuarios(@PathVariable("codigoProcesso") final long codigoProcesso) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigoProcesso);
		if (!processoOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		final List<ProcessoUsuarioResponseDTO> processoUsuarioResponseDTOs = new ArrayList<>();
		for (final Usuario usuario : processoOptional.get().getUsuarios()) {
			processoUsuarioResponseDTOs.add(new ProcessoUsuarioResponseDTO(processoOptional.get(), usuario));
		}
		return ResponseEntity.ok(processoUsuarioResponseDTOs);
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
