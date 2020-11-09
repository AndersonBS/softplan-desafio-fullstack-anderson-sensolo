package br.com.softplan.desafio.fullstack.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;

/**
 * Interface que faz o encapsulamento da lógica para acessar os dados de usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	boolean existsByLogin(final String login);

	boolean existsByEmail(final String email);

}
