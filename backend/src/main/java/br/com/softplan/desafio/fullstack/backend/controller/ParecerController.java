package br.com.softplan.desafio.fullstack.backend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.softplan.desafio.fullstack.backend.dto.request.ParecerRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableParecerResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ParecerResponseDTO;
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

	/**
	 * Lista os pareceres
	 * @return
	 */
	@GetMapping("/get")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<PageableParecerResponseDTO> getPareceres(
			@RequestParam(defaultValue = "0") final int selectedPage, @RequestParam(defaultValue = "5") final int pageSize) {
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
		return ResponseEntity.ok(new PageableParecerResponseDTO(parecerResponseDTOs, parecerPage));
	}

	/**
	 * Busca um parecer pelo código
	 * @param codigo
	 * @return
	 */
	@GetMapping("/get/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ParecerResponseDTO> getParecer(@PathVariable final Long codigo) {
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
			return ResponseEntity.ok(new ParecerResponseDTO(parecerOptional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * Apaga um parecer pelo código
	 * @param codigo
	 * @return
	 */
	@DeleteMapping("/delete/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	public ResponseEntity<Void> deleteParecer(@PathVariable final Long codigo) {
		final Optional<Parecer> parecerOptional = this.parecerRepository.findById(codigo);
		if (parecerOptional.isPresent()) {
			this.parecerRepository.deleteById(codigo);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * Cria um parecer
	 * @param parecerRequestDTO
	 * @return
	 */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<MensagemResponseDTO> createParecer(@Valid @RequestBody final ParecerRequestDTO parecerRequestDTO) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(parecerRequestDTO.getProcesso());
		final Optional<Usuario> autorOptional = this.usuarioRepository.findById(parecerRequestDTO.getAutor());

		// Verificação e tratamento caso o processo ou o usuário não tenham sido encontrados
		final MensagemResponseDTO mensagemProcessoAutorNaoEncontrados =
				this.verificarExistenciaProcessoAutor(processoOptional, autorOptional);
		if (mensagemProcessoAutorNaoEncontrados != null) {
			return ResponseEntity.badRequest().body(mensagemProcessoAutorNaoEncontrados);
		}

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
		return ResponseEntity.noContent().build();
	}

	/**
	 * Atualiza um parecer pelo código
	 * @param codigo
	 * @param parecerRequestDTO
	 * @return
	 */
	@PutMapping("/update/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<MensagemResponseDTO> updateParecer(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final ParecerRequestDTO parecerRequestDTO) {
		// Verificação e tratamento caso o parecer não tenha sido encontrado
		final Optional<Parecer> parecerOptional = this.parecerRepository.findById(codigo);
		if (!parecerOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		final Optional<Processo> processoOptional = this.processoRepository.findById(parecerRequestDTO.getProcesso());
		final Optional<Usuario> autorOptional = this.usuarioRepository.findById(parecerRequestDTO.getAutor());

		// Verificação e tratamento caso o processo ou o usuário não tenham sido encontrados
		final MensagemResponseDTO mensagemProcessoAutorNaoEncontrados =
				this.verificarExistenciaProcessoAutor(processoOptional, autorOptional);
		if (mensagemProcessoAutorNaoEncontrados != null) {
			return ResponseEntity.badRequest().body(mensagemProcessoAutorNaoEncontrados);
		}

		// Atualiza os dados do parecer
		parecerOptional.get().setDescricao(parecerRequestDTO.getDescricao());
		parecerOptional.get().setData(new Date());

		this.parecerRepository.save(parecerOptional.get());
		return ResponseEntity.noContent().build();
	}

	/**
	 * Verifica a existência do processo e do autor
	 * @param processoOptional
	 * @param autorOptional
	 * @return
	 */
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
