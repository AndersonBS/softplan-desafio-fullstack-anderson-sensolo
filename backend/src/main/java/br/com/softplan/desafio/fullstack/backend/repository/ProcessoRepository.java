package br.com.softplan.desafio.fullstack.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.softplan.desafio.fullstack.backend.model.Processo;

/**
 * Interface que faz o encapsulamento da lógica para acessar os dados de processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {

	/**
	 * Lista os processos pendentes de parecer por autor (somente usuário finalizador)
	 * @param codigoUsuarioFinalizador
	 * @return
	 */
	@Query(" select pro from Processo pro " +
			"    inner join pro.usuarios usu " +
			"where usu.codigo = :codigoUsuarioFinalizador " +
			"    and not exists (select par from Parecer par " +
			"        where par.processo.codigo = pro.codigo " +
			"            and par.autor.codigo = :codigoUsuarioFinalizador) ")
	List<Processo> findAllPendentesUsuarioFinalizador(final Long codigoUsuarioFinalizador);

	/**
	 * Busca um processo pendente de parecer pelo código e autor (somente usuário finalizador)
	 * @param codigoUsuarioFinalizador
	 * @param codigoProcesso
	 * @return
	 */
	@Query(" select pro from Processo pro " +
			"    inner join pro.usuarios usu " +
			"where usu.codigo = :codigoUsuarioFinalizador " +
			"    and pro.codigo = :codigoProcesso " +
			"    and not exists (select par from Parecer par " +
			"        where par.processo.codigo = pro.codigo " +
			"            and par.autor.codigo = :codigoUsuarioFinalizador) ")
	Optional<Processo> findPendenteUsuarioFinalizador(final Long codigoUsuarioFinalizador, final Long codigoProcesso);

	/**
	 * Verifica se existe o vínculo do usuário no processo
	 * @param codigoProcesso
	 * @param codigoUsuario
	 * @return
	 */
	@Query(" select case when count(pro) > 0 then true else false end " +
			"    from Processo pro " +
			"        inner join pro.usuarios pu " +
			"    where pro.codigo = :codigoProcesso " +
			"        and pu.codigo = :codigoUsuario ")
	boolean existsProcessoUsuario(final Long codigoProcesso, final Long codigoUsuario);

}
