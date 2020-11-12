package br.com.softplan.desafio.fullstack.backend.model;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ENUM para representar as possíveis permissões de um usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Getter
@AllArgsConstructor
public enum PermissaoUsuario {

	ADMINISTRADOR("Administrador") {
		@Override
		public boolean isAdministrador() {
			return true;
		}
	},
	TRIADOR("Triador") {
		@Override
		public boolean isTriador() {
			return true;
		}
	},
	FINALIZADOR("Finalizador") {
		@Override
		public boolean isFinalizador() {
			return true;
		}
	},
	DESCONHECIDO("Desconhecido") {
		@Override
		public boolean isDesconhecido() {
			return true;
		}
	};

	private String nome;

	private static final Map<String, PermissaoUsuario> stringValueMap;
	static {
		// Utilizado HashMap para otimizar consultas. Complexidade O(1)
		final Map<String, PermissaoUsuario> tmpMap = Maps.newHashMap();
		for (final PermissaoUsuario permissaoUsuario : PermissaoUsuario.values()) {
			tmpMap.put(permissaoUsuario.getNome(), permissaoUsuario);
		}
		stringValueMap = ImmutableMap.copyOf(tmpMap);
	}

	public static PermissaoUsuario getPermissaoUsuario(final String nome) {
		if (!PermissaoUsuario.stringValueMap.containsKey(nome)) {
			return DESCONHECIDO;
		}
		return PermissaoUsuario.stringValueMap.get(nome);
	}

	public boolean isAdministrador() {
		return false;
	}

	public boolean isTriador() {
		return false;
	}

	public boolean isFinalizador() {
		return false;
	}

	public boolean isDesconhecido() {
		return false;
	}

}
