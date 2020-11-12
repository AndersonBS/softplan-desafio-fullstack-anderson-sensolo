package br.com.softplan.desafio.fullstack.backend.security.model;

import java.util.Arrays;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtUserDetails implements UserDetails {

	private static final long serialVersionUID = -7690768805790159728L;

	private Long id;
	private String username;
	private String password;
	private String nome;
	private String email;
	private String permissao;
	private List<GrantedAuthority> authorities;

	public static JwtUserDetails build(final Usuario usuario) {
		final GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(usuario.getPermissao().name());
		return new JwtUserDetails(usuario.getCodigo(), usuario.getLogin(), usuario.getSenha(),
				usuario.getNome(), usuario.getEmail(), usuario.getPermissao().getNome(), Arrays.asList(grantedAuthority));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
