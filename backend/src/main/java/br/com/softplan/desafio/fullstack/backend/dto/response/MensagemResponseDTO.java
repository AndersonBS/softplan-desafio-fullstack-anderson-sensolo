package br.com.softplan.desafio.fullstack.backend.dto.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para enviar mensagens.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 08/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensagemResponseDTO implements Serializable {

	private static final long serialVersionUID = -1632158570700884331L;

	private String mensagem;

}
