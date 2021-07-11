package br.com.softplan.desafio.fullstack.backend.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import br.com.softplan.desafio.fullstack.backend.dto.request.ProcessoRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableProcessoResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableProcessoUsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ProcessoResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ProcessoUsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ResponsavelResponseDTO;
import br.com.softplan.desafio.fullstack.backend.exception.ProcessoNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.exception.ProcessoResponsavelNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.exception.ProcessoUsuarioCadastradoException;
import br.com.softplan.desafio.fullstack.backend.exception.ProcessoUsuarioNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.model.PermissaoUsuario;
import br.com.softplan.desafio.fullstack.backend.model.Processo;
import br.com.softplan.desafio.fullstack.backend.model.StatusProcesso;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import br.com.softplan.desafio.fullstack.backend.repository.ParecerRepository;
import br.com.softplan.desafio.fullstack.backend.repository.ProcessoRepository;
import br.com.softplan.desafio.fullstack.backend.repository.UsuarioRepository;
import br.com.softplan.desafio.fullstack.backend.security.model.JwtUserDetails;

/**
 * Service que contem as regras de negócio relacionadas ao processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/07/2021
 */

@Service
public class ProcessoService {

	@Autowired ProcessoRepository processoRepository;
	@Autowired UsuarioRepository usuarioRepository;
	@Autowired ParecerRepository parecerRepository;

	/**
	 * Lista os processos
	 * @param selectedPage
	 * @param pageSize
	 * @return
	 */
	public PageableProcessoResponseDTO getProcessos(final int selectedPage, final int pageSize) {
		final List<ProcessoResponseDTO> processoResponseDTOs = new ArrayList<>();
		final Page<Processo> processoPage;
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final Long codigoUsuarioAutenticado = ((JwtUserDetails) authentication.getPrincipal()).getId();
		final Pageable pageable = PageRequest.of(selectedPage, pageSize, Sort.by(new Order(Sort.Direction.DESC, "codigo")));

		// Listar processos com filtro por usuário caso seja finalizador
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(PermissaoUsuario.FINALIZADOR.name()))) {
			processoPage = this.processoRepository.findAllPendentesUsuarioFinalizador(codigoUsuarioAutenticado, pageable);
			for (final Processo processo : processoPage.getContent()) {
				// Verifica se o usuário logado deve incluir parecer no processo
				final boolean parecerPendente = !this.parecerRepository.existsByProcessoAndAutor(
						processo.getCodigo(), codigoUsuarioAutenticado) &&
						this.processoRepository.existsProcessoUsuario(processo.getCodigo(), codigoUsuarioAutenticado);
				processoResponseDTOs.add(new ProcessoResponseDTO(processo, parecerPendente));
			}
		} else {
			// Caso seja administrador ou triador pode listar todos os processos
			processoPage = this.processoRepository.findAll(PageRequest.of(selectedPage, pageSize, Sort.by(new Order(Sort.Direction.ASC, "codigo"))));
			for (final Processo processo : this.processoRepository.findAll(pageable)) {
				// Verifica se o usuário logado deve incluir parecer no processo
				final boolean parecerPendente = !this.parecerRepository.existsByProcessoAndAutor(
						processo.getCodigo(), codigoUsuarioAutenticado) &&
						this.processoRepository.existsProcessoUsuario(processo.getCodigo(), codigoUsuarioAutenticado);
				processoResponseDTOs.add(new ProcessoResponseDTO(processo, parecerPendente));
			}
		}

		return new PageableProcessoResponseDTO(processoResponseDTOs, processoPage);
	}

	/**
	 * Busca um processo pelo código
	 * @param codigo
	 * @return
	 */
	public ProcessoResponseDTO getProcesso(final Long codigo) {
		Optional<Processo> processoOptional;
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final Long codigoUsuarioAutenticado = ((JwtUserDetails) authentication.getPrincipal()).getId();

		// Buscar processo com filtro por usuário caso seja finalizador
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(PermissaoUsuario.FINALIZADOR.name()))) {
			processoOptional = this.processoRepository.findPendenteUsuarioFinalizador(codigoUsuarioAutenticado, codigo);
		} else {
			// Caso seja administrador ou triador pode buscar em todos os processos
			processoOptional = this.processoRepository.findById(codigo);
		}
		if (processoOptional.isPresent()) {
			// Verifica se o usuário logado deve incluir parecer no processo
			final boolean parecerPendente = !this.parecerRepository.existsByProcessoAndAutor(
					processoOptional.get().getCodigo(), codigoUsuarioAutenticado) &&
					this.processoRepository.existsProcessoUsuario(processoOptional.get().getCodigo(), codigoUsuarioAutenticado);
			return new ProcessoResponseDTO(processoOptional.get(), parecerPendente);
		}

		throw new ProcessoNaoEncontradoException("Processo não encontrado!");
	}

	/**
	 * Apaga um processo pelo código
	 * @param codigo
	 * @return
	 */
	public void deleteProcesso(final Long codigo) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigo);
		if (processoOptional.isPresent()) {
			this.processoRepository.deleteById(codigo);
		} else {
			throw new ProcessoNaoEncontradoException("Processo não encontrado!");
		}
	}

	/**
	 * Cria um processo
	 * @param processoRequestDTO
	 * @return
	 */
	public ProcessoResponseDTO createProcesso(final ProcessoRequestDTO processoRequestDTO) {
		final Optional<Usuario> responsavelOptional = this.usuarioRepository.findById(processoRequestDTO.getResponsavel());
		if (!responsavelOptional.isPresent()) {
			throw new ProcessoResponsavelNaoEncontradoException("Responsável não encontrado!");
		}

		// Cria e alimenta o novo processo
		final Processo processo = new Processo();
		processo.setNome(processoRequestDTO.getNome());
		processo.setDescricao(processoRequestDTO.getDescricao());
		processo.setStatus(StatusProcesso.NOVO);
		processo.setDataInicio(new Date());
		processo.setResponsavel(responsavelOptional.get());

		// Salva o processo
		this.processoRepository.save(processo);
		return new ProcessoResponseDTO(processo, false);
	}

	/**
	 * Atualiza um processo pelo código
	 * @param codigo
	 * @param processoRequestDTO
	 * @return
	 */
	public ProcessoResponseDTO updateProcesso(final long codigo, final ProcessoRequestDTO processoRequestDTO) {
		// Verificação e tratamento caso o processo não tenha sido encontrado
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigo);
		if (!processoOptional.isPresent()) {
			throw new ProcessoNaoEncontradoException("Processo não encontrado!");
		}

		// Verificação e tratamento caso o responsável não tenha sido encontrado
		final Processo processo = processoOptional.get();
		final Optional<Usuario> responsavelOptional = this.usuarioRepository.findById(processoRequestDTO.getResponsavel());
		if (!responsavelOptional.isPresent()) {
			throw new ProcessoResponsavelNaoEncontradoException("Responsável não encontrado!");
		}

		// Atualiza os dados do processo
		processo.setNome(processoRequestDTO.getNome());
		processo.setDescricao(processoRequestDTO.getDescricao());
		processo.setResponsavel(responsavelOptional.get());

		// Salva o processo
		this.processoRepository.save(processo);
		return new ProcessoResponseDTO(processo, false);
	}

	/**
	 * Vincula um usuário a um processo para prestar parecer
	 * @param codigoProcesso
	 * @param codigoUsuario
	 * @return
	 */
	public ProcessoUsuarioResponseDTO addProcessoUsuario(final long codigoProcesso, final long codigoUsuario) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigoProcesso);
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigoUsuario);

		// Verificação e tratamento caso o processo ou o usuário não tenham sido encontrados
		this.verificarExistenciaProcessoUsuario(processoOptional, usuarioOptional);

		// Proteção para não vincular o mesmo usuário duas vezes no processo
		final Processo processo = processoOptional.get();
		final Usuario usuario = usuarioOptional.get();
		if (processo.getUsuarios().contains(usuario)) {
			throw new ProcessoUsuarioCadastradoException("Usuário já cadastrado no processo!");
		}

		// Inclui o usuário no processo
		processo.getUsuarios().add(usuario);

		// Atualiza o status do processo para aguardando parecer caso o status do processo seja novo ou finalizado
		if (StatusProcesso.NOVO.equals(processo.getStatus()) || StatusProcesso.FINALIZADO.equals(processo.getStatus())) {
			processo.setStatus(StatusProcesso.AGUARDANDO_PARECER);
		}

		// Salva o processo
		this.processoRepository.save(processo);
		return new ProcessoUsuarioResponseDTO(processo, usuario);
	}

	/**
	 * Remove o vínculo de um usuário do processo
	 * @param codigoProcesso
	 * @param codigoUsuario
	 */
	public void removeProcessoUsuario(final long codigoProcesso, final long codigoUsuario) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigoProcesso);
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigoUsuario);

		// Verificação e tratamento caso o processo ou o usuário não tenham sido encontrados
		this.verificarExistenciaProcessoUsuario(processoOptional, usuarioOptional);

		// Remove o usuário do processo
		final Processo processo = processoOptional.get();
		final Usuario usuario = usuarioOptional.get();
		processo.getUsuarios().remove(usuario);

		// Salva o processo
		this.processoRepository.save(processo);
	}

	/**
	 * Verifica a existência do processo e do usuário
	 * @param processoOptional
	 * @param usuarioOptional
	 * @return
	 */
	private void verificarExistenciaProcessoUsuario(final Optional<Processo> processoOptional, final Optional<Usuario> usuarioOptional) {
		if (!processoOptional.isPresent()) {
			throw new ProcessoNaoEncontradoException("Processo não encontrado!");
		}
		if (!usuarioOptional.isPresent()) {
			throw new ProcessoUsuarioNaoEncontradoException("Usuário não encontrado!");
		}
	}

	/**
	 * Lista os usuários vinculados ao processo para prestar parecer
	 * @param codigoProcesso
	 * @param selectedPage
	 * @param pageSize
	 * @return
	 */
	public PageableProcessoUsuarioResponseDTO getProcessoUsuarios(final long codigoProcesso,
			final int selectedPage, final int pageSize) {
		// Verificação e tratamento caso o processo não tenha sido encontrado
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigoProcesso);
		if (!processoOptional.isPresent()) {
			throw new ProcessoNaoEncontradoException("Processo não encontrado!");
		}

		final Pageable pageable = PageRequest.of(selectedPage, pageSize);
		final Page<Usuario> usuarioPage = this.usuarioRepository.findByProcesso(codigoProcesso, pageable);
		final List<ProcessoUsuarioResponseDTO> processoUsuarioResponseDTOs = new ArrayList<>();
		for (final Usuario usuario : usuarioPage.getContent()) {
			processoUsuarioResponseDTOs.add(new ProcessoUsuarioResponseDTO(processoOptional.get(), usuario));
		}
		return new PageableProcessoUsuarioResponseDTO(processoUsuarioResponseDTOs, usuarioPage);
	}

	/**
	 * Lista os usuários que podem ser responsáveis por processos
	 * @return
	 */
	public List<ResponsavelResponseDTO> getResponsaveis() {
		final List<ResponsavelResponseDTO> responsavelResponseDTOs = new ArrayList<>();
		for (final Usuario usuario : this.usuarioRepository.getResponsaveis()) {
			responsavelResponseDTOs.add(new ResponsavelResponseDTO(usuario));
		}
		return responsavelResponseDTOs;
	}

	/**
	 * Lista os usuários que podem incluir pareceres nos processos
	 * @param codigoProcesso
	 * @return
	 */
	public List<ResponsavelResponseDTO> getFinalizadores(final long codigoProcesso) {
		final List<ResponsavelResponseDTO> responsavelResponseDTOs = new ArrayList<>();
		for (final Usuario usuario : this.usuarioRepository.getFinalizadores(codigoProcesso)) {
			responsavelResponseDTOs.add(new ResponsavelResponseDTO(usuario));
		}
		return responsavelResponseDTOs;
	}

}
