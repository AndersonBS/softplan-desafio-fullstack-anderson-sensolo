package br.com.softplan.desafio.fullstack.backend.models;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ENUM para representar os possíveis status de um processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Getter
@AllArgsConstructor
public enum StatusProcesso {

	NOVO("Novo") {
		@Override
		public boolean isNovo() {
			return true;
		}
	},
	AGUARDANDO_PARECER("Aguardando parecer") {
		@Override
		public boolean isAguardandoParecer() {
			return true;
		}
	},
	FINALIZADO("Finalizado") {
		@Override
		public boolean isFinalizado() {
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

	private static final Map<String, StatusProcesso> stringValueMap;
	static {
		final Map<String, StatusProcesso> tmpMap = Maps.newHashMap();
		for (final StatusProcesso statusProcesso : StatusProcesso.values()) {
			tmpMap.put(statusProcesso.getNome(), statusProcesso);
		}
		stringValueMap = ImmutableMap.copyOf(tmpMap);
	}

	public static StatusProcesso getStatusProcesso(final String nome) {
		if (!StatusProcesso.stringValueMap.containsKey(nome)) {
			return DESCONHECIDO;
		}
		return StatusProcesso.stringValueMap.get(nome);
	}

	public boolean isNovo() {
		return false;
	}

	public boolean isAguardandoParecer() {
		return false;
	}

	public boolean isFinalizado() {
		return false;
	}

	public boolean isDesconhecido() {
		return false;
	}

}
