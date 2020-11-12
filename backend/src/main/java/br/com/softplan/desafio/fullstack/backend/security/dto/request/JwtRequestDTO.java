package br.com.softplan.desafio.fullstack.backend.security.dto.request;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para receber dados de autenticação.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 10/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestDTO implements Serializable {
	
	private static final long serialVersionUID = -7056273359760674936L;
	
	@NotBlank
    @Size(min = 5, max = 63)
	private String username;
	
	@NotBlank
    @Size(min = 5, max = 63)
	private String password;

}
