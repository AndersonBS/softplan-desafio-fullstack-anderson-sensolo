package br.com.softplan.desafio.fullstack.backend.repository;

import java.util.Optional;
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

	Optional<Usuario> findByCodigo(final Long codigo);

}
