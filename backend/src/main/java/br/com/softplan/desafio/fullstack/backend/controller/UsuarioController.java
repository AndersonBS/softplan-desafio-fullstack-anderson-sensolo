package br.com.softplan.desafio.fullstack.backend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.softplan.desafio.fullstack.backend.dto.request.UsuarioRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.UsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.model.PermissaoUsuario;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import br.com.softplan.desafio.fullstack.backend.repository.UsuarioRepository;

/**
 * Classe que processa as requisições relacionadas ao usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired UsuarioRepository usuarioRepository;
	@Autowired PasswordEncoder passwordEncoder;

	@GetMapping("/get")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<List<UsuarioResponseDTO>> getUsuarios() {
		final List<UsuarioResponseDTO> usuarioResponseDTOs = new ArrayList<>();
		for (final Usuario usuario : this.usuarioRepository.findAll()) {
			usuarioResponseDTOs.add(new UsuarioResponseDTO(usuario));
		}
		return ResponseEntity.ok(usuarioResponseDTOs);
	}

	@GetMapping("/get/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<UsuarioResponseDTO> getUsuario(@PathVariable final Long codigo) {
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigo);
		if (usuarioOptional.isPresent()) {
			return ResponseEntity.ok(new UsuarioResponseDTO(usuarioOptional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/delete/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<Void> deleteUsuario(@PathVariable final Long codigo) {
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigo);
		if (usuarioOptional.isPresent()) {
			this.usuarioRepository.deleteById(codigo);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<MensagemResponseDTO> createUsuario(@Valid @RequestBody final UsuarioRequestDTO usuarioRequestDTO) {
		final MensagemResponseDTO mensagemDadosUnicosJaCadastrados = this.verificarDadosUnicosJaCadastrados(usuarioRequestDTO);
		if (mensagemDadosUnicosJaCadastrados != null) {
			return ResponseEntity.badRequest().body(mensagemDadosUnicosJaCadastrados);
		}

		final Usuario usuario = new Usuario();
		usuario.setNome(usuarioRequestDTO.getNome());
		usuario.setLogin(usuarioRequestDTO.getLogin());
		usuario.setEmail(usuarioRequestDTO.getEmail());
		usuario.setSenha(this.passwordEncoder.encode(usuarioRequestDTO.getSenha()));
		usuario.setPermissao(PermissaoUsuario.getPermissaoUsuario(usuarioRequestDTO.getPermissao()));
		usuario.setDataInclusao(new Date());

		this.usuarioRepository.save(usuario);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/update/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<MensagemResponseDTO> updateUsuario(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final UsuarioRequestDTO usuarioRequestDTO) {
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigo);
		if (!usuarioOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		final MensagemResponseDTO mensagemDadosUnicosJaCadastrados = this.verificarDadosUnicosJaCadastrados(usuarioRequestDTO);
		if (mensagemDadosUnicosJaCadastrados != null) {
			return ResponseEntity.badRequest().body(mensagemDadosUnicosJaCadastrados);
		}

		usuarioOptional.get().setNome(usuarioRequestDTO.getNome());
		usuarioOptional.get().setLogin(usuarioRequestDTO.getLogin());
		usuarioOptional.get().setEmail(usuarioRequestDTO.getEmail());
		usuarioOptional.get().setSenha(this.passwordEncoder.encode(usuarioRequestDTO.getSenha()));
		usuarioOptional.get().setPermissao(PermissaoUsuario.getPermissaoUsuario(usuarioRequestDTO.getPermissao()));

		this.usuarioRepository.save(usuarioOptional.get());
		return ResponseEntity.noContent().build();
	}

	private MensagemResponseDTO verificarDadosUnicosJaCadastrados(final UsuarioRequestDTO usuarioRequestDTO) {
		if (this.usuarioRepository.existsByLogin(usuarioRequestDTO.getLogin())) {
			return new MensagemResponseDTO("Já existe um usuário com este login!");
		}
		if (this.usuarioRepository.existsByEmail(usuarioRequestDTO.getEmail())) {
			return new MensagemResponseDTO("Já existe um usuário com este e-mail!");
		}
		return null;
	}

}
