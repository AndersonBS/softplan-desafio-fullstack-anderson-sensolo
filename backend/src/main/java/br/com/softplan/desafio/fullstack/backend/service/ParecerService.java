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
import br.com.softplan.desafio.fullstack.backend.dto.request.ParecerRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableParecerResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ParecerResponseDTO;
import br.com.softplan.desafio.fullstack.backend.exception.ParecerAutorNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.exception.ParecerNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.exception.ParecerProcessoNaoEncontradoException;
import br.com.softplan.desafio.fullstack.backend.model.Parecer;
import br.com.softplan.desafio.fullstack.backend.model.PermissaoUsuario;
import br.com.softplan.desafio.fullstack.backend.model.Processo;
import br.com.softplan.desafio.fullstack.backend.model.StatusProcesso;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import br.com.softplan.desafio.fullstack.backend.repository.ParecerRepository;
import br.com.softplan.desafio.fullstack.backend.repository.ProcessoRepository;
import br.com.softplan.desafio.fullstack.backend.repository.UsuarioRepository;
import br.com.softplan.desafio.fullstack.backend.security.model.JwtUserDetails;

/**
 * Service que contem as regras de negócio relacionadas ao parecer.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/07/2021
 */

@Service
public class ParecerService {

	@Autowired ParecerRepository parecerRepository;
	@Autowired ProcessoRepository processoRepository;
	@Autowired UsuarioRepository usuarioRepository;

	/**
	 * Lista os pareceres
	 * @param selectedPage
	 * @param pageSize
	 * @return
	 */
	public PageableParecerResponseDTO getPareceres(final int selectedPage, final int pageSize) {
		final List<ParecerResponseDTO> parecerResponseDTOs = new ArrayList<>();
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final Page<Parecer> parecerPage;
		final Pageable pageable = PageRequest.of(selectedPage, pageSize, Sort.by(new Order(Sort.Direction.DESC, "codigo")));

		// Listar pareceres com filtro por usuário caso seja finalizador
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(PermissaoUsuario.FINALIZADOR.name()))) {
			final Long codigoUsuarioAutenticado = ((JwtUserDetails) authentication.getPrincipal()).getId();
			parecerPage = this.parecerRepository.findAllByAutor(codigoUsuarioAutenticado, pageable);
			for (final Parecer parecer : parecerPage.getContent()) {
				parecerResponseDTOs.add(new ParecerResponseDTO(parecer));
			}
		} else {
			// Caso seja administrador pode listar todos os pareceres
			parecerPage = this.parecerRepository.findAll(pageable);
			for (final Parecer parecer : parecerPage.getContent()) {
				parecerResponseDTOs.add(new ParecerResponseDTO(parecer));
			}
		}

		return new PageableParecerResponseDTO(parecerResponseDTOs, parecerPage);
	}

	/**
	 * Busca um parecer pelo código
	 * @param codigo
	 * @return
	 */
	public ParecerResponseDTO getParecer(final Long codigo) {
		final Optional<Parecer> parecerOptional;
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Buscar parecer com filtro por usuário caso seja finalizador
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(PermissaoUsuario.FINALIZADOR.name()))) {
			final Long codigoUsuarioAutenticado = ((JwtUserDetails) authentication.getPrincipal()).getId();
			parecerOptional = this.parecerRepository.findByCodigoAndAutor(codigoUsuarioAutenticado, codigo);
		} else {
			// Caso seja administrador pode buscar em todos os pareceres
			parecerOptional = this.parecerRepository.findById(codigo);
		}
		if (parecerOptional.isPresent()) {
			return new ParecerResponseDTO(parecerOptional.get());
		}

		throw new ParecerNaoEncontradoException("Parecer não encontrado!");
	}

	/**
	 * Apaga um parecer pelo código
	 * @param codigo
	 */
	public void deleteParecer(final Long codigo) {
		final Optional<Parecer> parecerOptional = this.parecerRepository.findById(codigo);
		if (parecerOptional.isPresent()) {
			this.parecerRepository.deleteById(codigo);
		} else {
			throw new ParecerNaoEncontradoException("Parecer não encontrado!");
		}
	}

	/**
	 * Cria um parecer
	 * @param parecerRequestDTO
	 * @return
	 */
	public ParecerResponseDTO createParecer(final ParecerRequestDTO parecerRequestDTO) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(parecerRequestDTO.getProcesso());
		final Optional<Usuario> autorOptional = this.usuarioRepository.findById(parecerRequestDTO.getAutor());

		this.validarExistenciaProcessoAutor(processoOptional, autorOptional);

		// Cria e alimenta o novo parecer
		final Parecer parecer = new Parecer();
		parecer.setDescricao(parecerRequestDTO.getDescricao());
		parecer.setData(new Date());
		parecer.setProcesso(processoOptional.get());
		parecer.setAutor(autorOptional.get());

		// Finaliza o processo caso o status do processo for aguardando parecer e este novo parecer for o último
		if (StatusProcesso.AGUARDANDO_PARECER.equals(processoOptional.get().getStatus()) &&
				processoOptional.get().getUsuarios().size() == processoOptional.get().getPareceres().size() + 1) {
			processoOptional.get().setStatus(StatusProcesso.FINALIZADO);
		}

		this.parecerRepository.save(parecer);
		return new ParecerResponseDTO(parecer);
	}

	/**
	 * Atualiza um parecer pelo código
	 * @param codigo
	 * @param parecerRequestDTO
	 * @return
	 */
	public ParecerResponseDTO updateParecer(final long codigo, final ParecerRequestDTO parecerRequestDTO) {
		// Verificação e tratamento caso o parecer não tenha sido encontrado
		final Optional<Parecer> parecerOptional = this.parecerRepository.findById(codigo);
		if (!parecerOptional.isPresent()) {
			throw new ParecerNaoEncontradoException("Parecer não encontrado!");
		}

		final Parecer parecer = parecerOptional.get();
		final Optional<Processo> processoOptional = this.processoRepository.findById(parecerRequestDTO.getProcesso());
		final Optional<Usuario> autorOptional = this.usuarioRepository.findById(parecerRequestDTO.getAutor());

		// Verificação e tratamento caso o processo ou o usuário não tenham sido encontrados
		this.validarExistenciaProcessoAutor(processoOptional, autorOptional);

		// Atualiza os dados do parecer
		parecer.setDescricao(parecerRequestDTO.getDescricao());
		parecer.setData(new Date());

		this.parecerRepository.save(parecer);
		return new ParecerResponseDTO(parecer);
	}

	/**
	 * Verifica a existência do processo e do autor
	 * @param processoOptional
	 * @param autorOptional
	 * @return
	 */
	private void validarExistenciaProcessoAutor(final Optional<Processo> processoOptional, final Optional<Usuario> autorOptional) {
		if (!processoOptional.isPresent()) {
			throw new ParecerProcessoNaoEncontradoException("Processo não encontrado!");
		}
		if (!autorOptional.isPresent()) {
			throw new ParecerAutorNaoEncontradoException("Autor não encontrado!");
		}
	}

}
