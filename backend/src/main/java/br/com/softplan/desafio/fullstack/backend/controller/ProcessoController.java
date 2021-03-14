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
import br.com.softplan.desafio.fullstack.backend.dto.request.ProcessoRequestDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.MensagemResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.PageableProcessoResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ProcessoResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ProcessoUsuarioResponseDTO;
import br.com.softplan.desafio.fullstack.backend.dto.response.ResponsavelResponseDTO;
import br.com.softplan.desafio.fullstack.backend.model.PermissaoUsuario;
import br.com.softplan.desafio.fullstack.backend.model.Processo;
import br.com.softplan.desafio.fullstack.backend.model.StatusProcesso;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import br.com.softplan.desafio.fullstack.backend.repository.ParecerRepository;
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
	@Autowired ParecerRepository parecerRepository;

	/**
	 * Lista os processos
	 * @return
	 */
	@GetMapping("/get")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<PageableProcessoResponseDTO> getProcessos(
			@RequestParam(defaultValue = "0") final int selectedPage, @RequestParam(defaultValue = "5") final int pageSize) {
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
		return ResponseEntity.ok(new PageableProcessoResponseDTO(processoResponseDTOs, processoPage));
	}

	/**
	 * Busca um processo pelo código
	 * @param codigo
	 * @return
	 */
	@GetMapping("/get/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR') or hasAuthority('FINALIZADOR')")
	public ResponseEntity<ProcessoResponseDTO> getProcesso(@PathVariable final Long codigo) {
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
			return ResponseEntity.ok(new ProcessoResponseDTO(processoOptional.get(), parecerPendente));
		}
		return ResponseEntity.notFound().build();
	}

	/**
	 * Apaga um processo pelo código
	 * @param codigo
	 * @return
	 */
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

	/**
	 * Cria um processo
	 * @param processoRequestDTO
	 * @return
	 */
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<MensagemResponseDTO> createProcesso(@Valid @RequestBody final ProcessoRequestDTO processoRequestDTO) {
		final Optional<Usuario> responsavelOptional = this.usuarioRepository.findById(processoRequestDTO.getResponsavel());
		if (!responsavelOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new MensagemResponseDTO("Responsável não encontrado!"));
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
		return ResponseEntity.noContent().build();
	}

	/**
	 * Atualiza um processo pelo código
	 * @param codigo
	 * @param processoRequestDTO
	 * @return
	 */
	@PutMapping("/update/{codigo}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<MensagemResponseDTO> updateProcesso(@PathVariable("codigo") final long codigo,
			@Valid @RequestBody final ProcessoRequestDTO processoRequestDTO) {
		// Verificação e tratamento caso o processo não tenha sido encontrado
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigo);
		if (!processoOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		// Verificação e tratamento caso o responsável não tenha sido encontrado
		final Optional<Usuario> responsavelOptional = this.usuarioRepository.findById(processoRequestDTO.getResponsavel());
		if (!responsavelOptional.isPresent()) {
			return ResponseEntity.badRequest().body(new MensagemResponseDTO("Responsável não encontrado!"));
		}

		// Atualiza os dados do processo
		processoOptional.get().setNome(processoRequestDTO.getNome());
		processoOptional.get().setDescricao(processoRequestDTO.getDescricao());
		processoOptional.get().setResponsavel(responsavelOptional.get());

		// Salva o processo
		this.processoRepository.save(processoOptional.get());
		return ResponseEntity.noContent().build();
	}

	/**
	 * Vincula um usuário a um processo para prestar parecer
	 * @param codigoProcesso
	 * @param codigoUsuario
	 * @return
	 */
	@PutMapping("/{codigoProcesso}/add/usuario/{codigoUsuario}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<MensagemResponseDTO> addProcessoUsuario(@PathVariable("codigoProcesso") final long codigoProcesso,
			@PathVariable("codigoUsuario") final long codigoUsuario) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigoProcesso);
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigoUsuario);

		// Verificação e tratamento caso o processo ou o usuário não tenham sido encontrados
		final MensagemResponseDTO mensagemProcessoUsuarioNaoEncontrados =
				this.verificarExistenciaProcessoUsuario(processoOptional, usuarioOptional);
		if (mensagemProcessoUsuarioNaoEncontrados != null) {
			return ResponseEntity.badRequest().body(mensagemProcessoUsuarioNaoEncontrados);
		}

		// Proteção para não vincular o mesmo usuário duas vezes no processo
		if (processoOptional.get().getUsuarios().contains(usuarioOptional.get())) {
			return ResponseEntity.badRequest().body(new MensagemResponseDTO("Usuário já cadastrado no processo!"));
		}

		// Inclui o usuário no processo
		processoOptional.get().getUsuarios().add(usuarioOptional.get());

		// Atualiza o status do processo para aguardando parecer caso o status do processo seja novo ou finalizado
		if (StatusProcesso.NOVO.equals(processoOptional.get().getStatus()) ||
				StatusProcesso.FINALIZADO.equals(processoOptional.get().getStatus())) {
			processoOptional.get().setStatus(StatusProcesso.AGUARDANDO_PARECER);
		}

		// Salva o processo
		this.processoRepository.save(processoOptional.get());
		return ResponseEntity.noContent().build();
	}

	/**
	 * Remove o vínculo de um usuário do processo
	 * @param codigoProcesso
	 * @param codigoUsuario
	 * @return
	 */
	@PutMapping("/{codigoProcesso}/remove/usuario/{codigoUsuario}")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<MensagemResponseDTO> removeProcessoUsuario(@PathVariable("codigoProcesso") final long codigoProcesso,
			@PathVariable("codigoUsuario") final long codigoUsuario) {
		final Optional<Processo> processoOptional = this.processoRepository.findById(codigoProcesso);
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findById(codigoUsuario);

		// Verificação e tratamento caso o processo ou o usuário não tenham sido encontrados
		final MensagemResponseDTO mensagemProcessoUsuarioNaoEncontrados =
				this.verificarExistenciaProcessoUsuario(processoOptional, usuarioOptional);
		if (mensagemProcessoUsuarioNaoEncontrados != null) {
			return ResponseEntity.badRequest().body(mensagemProcessoUsuarioNaoEncontrados);
		}

		// Remove o usuário do processo
		processoOptional.get().getUsuarios().remove(usuarioOptional.get());

		// Salva o processo
		this.processoRepository.save(processoOptional.get());
		return ResponseEntity.noContent().build();
	}

	/**
	 * Lista os usuários vinculados ao processo para prestar parecer
	 * @param codigoProcesso
	 * @return
	 */
	@GetMapping("/{codigoProcesso}/get/usuarios")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<List<ProcessoUsuarioResponseDTO>> getProcessoUsuarios(@PathVariable("codigoProcesso") final long codigoProcesso) {
		// Verificação e tratamento caso o processo não tenha sido encontrado
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

	/**
	 * Lista os usuários que podem ser responsáveis por processos
	 * @return
	 */
	@GetMapping("/get/responsaveis")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<List<ResponsavelResponseDTO>> getResponsaveis() {
		final List<ResponsavelResponseDTO> responsavelResponseDTOs = new ArrayList<>();
		for (final Usuario usuario : this.usuarioRepository.getResponsaveis()) {
			responsavelResponseDTOs.add(new ResponsavelResponseDTO(usuario));
		}
		return ResponseEntity.ok(responsavelResponseDTOs);
	}

	/**
	 * Lista os usuários que podem incluir pareceres nos processos
	 * @param codigoProcesso
	 * @return
	 */
	@GetMapping("/{codigoProcesso}/get/finalizadores")
	@PreAuthorize("hasAuthority('ADMINISTRADOR') or hasAuthority('TRIADOR')")
	public ResponseEntity<List<ResponsavelResponseDTO>> getFinalizadores(@PathVariable("codigoProcesso") final long codigoProcesso) {
		final List<ResponsavelResponseDTO> responsavelResponseDTOs = new ArrayList<>();
		for (final Usuario usuario : this.usuarioRepository.getFinalizadores(codigoProcesso)) {
			responsavelResponseDTOs.add(new ResponsavelResponseDTO(usuario));
		}
		return ResponseEntity.ok(responsavelResponseDTOs);
	}

	/**
	 * Verifica a existência do processo e do usuário
	 * @param processoOptional
	 * @param usuarioOptional
	 * @return
	 */
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
