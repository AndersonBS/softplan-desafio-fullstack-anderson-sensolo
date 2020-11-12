package br.com.softplan.desafio.fullstack.backend.security.service;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import br.com.softplan.desafio.fullstack.backend.repository.UsuarioRepository;
import br.com.softplan.desafio.fullstack.backend.security.model.JwtUserDetails;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired UsuarioRepository usuarioRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) {
		final Optional<Usuario> usuarioOptional = this.usuarioRepository.findByLogin(username);
		if (usuarioOptional.isPresent()) {
			return JwtUserDetails.build(usuarioOptional.get());
		}
		return null;
	}

}
