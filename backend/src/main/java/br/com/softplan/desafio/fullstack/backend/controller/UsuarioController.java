package br.com.softplan.desafio.fullstack.backend.controller;

import java.util.Date;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import br.com.softplan.desafio.fullstack.backend.model.PermissaoUsuario;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import br.com.softplan.desafio.fullstack.backend.repository.UsuarioRepository;

/**
 * Classe que processa as requisições relacionadas ao usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	@Autowired UsuarioRepository usuarioRepository;
	@Autowired PasswordEncoder passwordEncoder;

	/**
	 * Lista os usuários com paginação
	 * @return
	 */
	@GetMapping("/get")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<PageableUsuarioResponseDTO> getUsuarios(
			@RequestParam(defaultValue = "0") final int selectedPage, @RequestParam(defaultValue = "5") final int pageSize) {
		return ResponseEntity.ok(new PageableUsuarioResponseDTO(this.usuarioRepository.findAll(
				PageRequest.of(selectedPage, pageSize, Sort.by(new Order(Sort.Direction.DESC, "codigo"))))));
	}

	/**
	 * Busca um usuário pelo código
	 * @param codigo
	 * @return
	 */
	@GetMapping("/get/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<UsuarioResponseDTO> getUsuario(@PathVariable final Long codigo) {
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigo);
		if (usuarioOptional.isPresent()) {
			return ResponseEntity.ok(new UsuarioResponseDTO(usuarioOptional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * Apaga um usuário pelo código
	 * @param codigo
	 * @return
	 */
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

	/**
	 * Cria um usuário
	 * @param usuarioRequestDTO
	 * @return
	 */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<MensagemResponseDTO> createUsuario(@Valid @RequestBody final UsuarioRequestDTO usuarioRequestDTO) {
		// Verificação e tratamento caso o login ou e-mail já existem
		final MensagemResponseDTO mensagemDadosUnicosJaCadastrados = this.verificarDadosUnicosJaCadastrados(0L, usuarioRequestDTO);
		if (mensagemDadosUnicosJaCadastrados != null) {
			return ResponseEntity.badRequest().body(mensagemDadosUnicosJaCadastrados);
		}

		// Cria o usuário
		final Usuario usuario = new Usuario();
		usuario.setNome(usuarioRequestDTO.getNome());
		usuario.setLogin(usuarioRequestDTO.getLogin());
		usuario.setEmail(usuarioRequestDTO.getEmail());
		usuario.setSenha(this.passwordEncoder.encode(usuarioRequestDTO.getSenha()));
		usuario.setPermissao(PermissaoUsuario.getPermissaoUsuario(usuarioRequestDTO.getPermissao()));
		usuario.setDataInclusao(new Date());

		// Salva o usuário
		this.usuarioRepository.save(usuario);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Atualiza um usuário pelo código
	 * @param codigo
	 * @param usuarioRequestDTO
	 * @return
	 */
	@PutMapping("/update/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<MensagemResponseDTO> updateUsuario(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final UsuarioRequestDTO usuarioRequestDTO) {
		// Verificação e tratamento caso o usuário não tenha sido encontrado
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigo);
		if (!usuarioOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		// Verificação e tratamento caso o login ou e-mail já existem
		final MensagemResponseDTO mensagemDadosUnicosJaCadastrados = this.verificarDadosUnicosJaCadastrados(codigo, usuarioRequestDTO);
		if (mensagemDadosUnicosJaCadastrados != null) {
			return ResponseEntity.badRequest().body(mensagemDadosUnicosJaCadastrados);
		}

		// Atualiza os dados do usuário
		usuarioOptional.get().setNome(usuarioRequestDTO.getNome());
		usuarioOptional.get().setLogin(usuarioRequestDTO.getLogin());
		usuarioOptional.get().setEmail(usuarioRequestDTO.getEmail());
		if (!usuarioOptional.get().getSenha().equals(usuarioRequestDTO.getSenha())) {
			usuarioOptional.get().setSenha(this.passwordEncoder.encode(usuarioRequestDTO.getSenha()));
		}
		usuarioOptional.get().setPermissao(PermissaoUsuario.getPermissaoUsuario(usuarioRequestDTO.getPermissao()));

		// Salva o usuário
		this.usuarioRepository.save(usuarioOptional.get());
		return ResponseEntity.noContent().build();
	}

	/**
	 * Verifica a existência de outros usuários com o mesmo login ou e-mail
	 * @param codigoUsuario
	 * @param usuarioRequestDTO
	 * @return
	 */
	private MensagemResponseDTO verificarDadosUnicosJaCadastrados(final Long codigoUsuario,
			final UsuarioRequestDTO usuarioRequestDTO) {
		if (this.usuarioRepository.existsAnotherByLogin(codigoUsuario, usuarioRequestDTO.getLogin())) {
			return new MensagemResponseDTO("Já existe um usuário com este login!");
		}
		if (this.usuarioRepository.existsAnotherByEmail(codigoUsuario, usuarioRequestDTO.getEmail())) {
			return new MensagemResponseDTO("Já existe um usuário com este e-mail!");
		}
		return null;
	}

}
