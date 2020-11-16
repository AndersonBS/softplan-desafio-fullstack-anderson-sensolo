package br.com.softplan.desafio.fullstack.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.softplan.desafio.fullstack.backend.model.Usuario;

/**
 * Interface que faz o encapsulamento da lógica para acessar os dados de usuário.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query(" select case when count(usu) > 0 then true else false end from Usuario usu " +
			"where usu.codigo != :codigoUsuario and usu.login = :login ")
	boolean existsAnotherByLogin(final Long codigoUsuario, final String login);

	@Query(" select case when count(usu) > 0 then true else false end from Usuario usu " +
			"where usu.codigo != :codigoUsuario and usu.email = :email ")
	boolean existsAnotherByEmail(final Long codigoUsuario, final String email);

	Optional<Usuario> findByLogin(String login);

	@Query(" select usu from Usuario usu where usu.permissao in ('Administrador', 'Triador') ")
	List<Usuario> getResponsaveis();

	@Query(" select usu from Usuario usu where usu.permissao in ('Administrador', 'Finalizador') " +
			"and not exists (select 1 from Processo pro inner join pro.usuarios pu " +
			"where pro.codigo = :codigoProcesso and pu.codigo = usu.codigo) ")
	List<Usuario> getFinalizadores(final Long codigoProcesso);

}
