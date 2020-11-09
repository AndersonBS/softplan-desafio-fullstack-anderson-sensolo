package br.com.softplan.desafio.fullstack.backend.dto.request;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para receber dados de usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 08/11/2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO implements Serializable {

	private static final long serialVersionUID = -6406018323031415771L; 

	@NotBlank
    @Size(min = 5, max = 255)
	private String nome;
	
	@NotBlank
    @Size(min = 5, max = 255)
	private String login;
	
	@NotBlank
    @Size(max = 63)
    @Email
	private String email;
	
	@NotBlank
    @Size(min = 5, max = 63)
	private String senha;
	
	@NotBlank
	private String permissao;

}
