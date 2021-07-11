package br.com.softplan.desafio.fullstack.backend.service;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import br.com.softplan.desafio.fullstack.backend.dto.request.UsuarioRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableUsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.UsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.exception.UsuarioEmailCadastradoException;
import br.com.softplan.desafio.fullstack.backend.exception.UsuarioLoginCadastradoException;
import br.com.softplan.desafio.fullstack.backend.exception.UsuarioNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.model.PermissaoUsuario;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import br.com.softplan.desafio.fullstack.backend.repository.UsuarioRepository;

/**
 * Service que contem as regras de negócio relacionadas ao usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/07/2021
 */

@Service
public class UsuarioService {

	@Autowired UsuarioRepository usuarioRepository;
	@Autowired PasswordEncoder passwordEncoder;

	/**
	 * Lista os usuários com paginação
	 * @param selectedPage
	 * @param pageSize
	 * @return
	 */
	public PageableUsuarioResponseDTO getUsuarios(final int selectedPage, final int pageSize) {
		return new PageableUsuarioResponseDTO(this.usuarioRepository.findAll(
				PageRequest.of(selectedPage, pageSize, Sort.by(new Order(Sort.Direction.DESC, "codigo")))));
	}

	/**
	 * Busca um usuário pelo código
	 * @param codigo
	 * @return
	 */
	public UsuarioResponseDTO getUsuario(final Long codigo) {
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigo);
		if (usuarioOptional.isPresent()) {
			return new UsuarioResponseDTO(usuarioOptional.get());
		}
		throw new UsuarioNaoEncontradoException("Usuário não encontrado!");
	}

	/**
	 * Apaga um usuário pelo código
	 * @param codigo
	 */
	public void deleteUsuario(final Long codigo) {
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigo);
		if (usuarioOptional.isPresent()) {
			this.usuarioRepository.deleteById(codigo);
		} else {
			throw new UsuarioNaoEncontradoException("Usuário não encontrado!");
		}
	}

	/**
	 * Cria um usuário
	 * @param usuarioRequestDTO
	 * @return
	 */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public UsuarioResponseDTO createUsuario(final UsuarioRequestDTO usuarioRequestDTO) {
		// Verificação e tratamento caso o login ou e-mail já existem
		this.verificarDadosUnicosJaCadastrados(0L, usuarioRequestDTO);

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
		return new UsuarioResponseDTO(usuario);
	}

	/**
	 * Atualiza um usuário pelo código
	 * @param codigo
	 * @param usuarioRequestDTO
	 * @return
	 */
	public UsuarioResponseDTO updateUsuario(final long codigo, final UsuarioRequestDTO usuarioRequestDTO) {
		// Verificação e tratamento caso o usuário não tenha sido encontrado
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigo);
		if (!usuarioOptional.isPresent()) {
			throw new UsuarioNaoEncontradoException("Usuário não encontrado!");
		}

		// Verificação e tratamento caso o login ou e-mail já existem
		this.verificarDadosUnicosJaCadastrados(codigo, usuarioRequestDTO);

		// Atualiza os dados do usuário
		final Usuario usuario = usuarioOptional.get();
		usuario.setNome(usuarioRequestDTO.getNome());
		usuario.setLogin(usuarioRequestDTO.getLogin());
		usuario.setEmail(usuarioRequestDTO.getEmail());
		if (!usuario.getSenha().equals(usuarioRequestDTO.getSenha())) {
			usuario.setSenha(this.passwordEncoder.encode(usuarioRequestDTO.getSenha()));
		}
		usuario.setPermissao(PermissaoUsuario.getPermissaoUsuario(usuarioRequestDTO.getPermissao()));

		// Salva o usuário
		this.usuarioRepository.save(usuario);
		return new UsuarioResponseDTO(usuario);
	}

	/**
	 * Verifica a existência de outros usuários com o mesmo login ou e-mail
	 * @param codigoUsuario
	 * @param usuarioRequestDTO
	 * @return
	 */
	private MensagemResponseDTO verificarDadosUnicosJaCadastrados(final Long codigoUsuario, final UsuarioRequestDTO usuarioRequestDTO) {
		if (this.usuarioRepository.existsAnotherByLogin(codigoUsuario, usuarioRequestDTO.getLogin())) {
			throw new UsuarioLoginCadastradoException("Já existe um usuário com este login!");
		}
		if (this.usuarioRepository.existsAnotherByEmail(codigoUsuario, usuarioRequestDTO.getEmail())) {
			throw new UsuarioEmailCadastradoException("Já existe um usuário com este e-mail!");
		}
		return null;
	}

}
