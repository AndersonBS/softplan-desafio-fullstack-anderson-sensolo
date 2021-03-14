package br.com.softplan.desafio.fullstack.backend.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.com.softplan.desafio.fullstack.backend.model.Parecer;

/**
 * Interface que faz o encapsulamento da lógica para acessar os dados de parecer.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

public interface ParecerRepository extends JpaRepository<Parecer, Long> {

	/**
	 * Verifica a existência do parecer por processo e autor
	 * @param codigoProcesso
	 * @param codigoAutor
	 * @return
	 */
	@Query(" select case when count(par) > 0 then true else false end " +
			"    from Parecer par " +
			"        inner join par.processo pro " +
			"        inner join par.autor aut " +
			"    where pro.codigo = :codigoProcesso " +
			"        and aut.codigo = :codigoAutor ")
	boolean existsByProcessoAndAutor(final Long codigoProcesso, final Long codigoAutor);

	/**
	 * Lista os pareceres por autor
	 * @param codigoAutor
	 * @return
	 */
	@Query(" select par from Parecer par inner join par.autor aut where aut.codigo = :codigoAutor ")
	Page<Parecer> findAllByAutor(final Long codigoAutor, final Pageable pageable);

	/**
	 * Busca um parecer pelo código e autor
	 * @param codigoAutor
	 * @param codigoParecer
	 * @return
	 */
	@Query(" select par from Parecer par inner join par.autor aut where aut.codigo = :codigoAutor and par.codigo = :codigoParecer ")
	Optional<Parecer> findByCodigoAndAutor(final Long codigoAutor, final Long codigoParecer);

}
