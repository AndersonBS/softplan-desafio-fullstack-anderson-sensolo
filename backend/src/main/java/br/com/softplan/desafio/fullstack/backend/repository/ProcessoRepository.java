package br.com.softplan.desafio.fullstack.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.softplan.desafio.fullstack.backend.model.Processo;

/**
 * Interface que faz o encapsulamento da lógica para acessar os dados de processo.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {

	Optional<Processo> findByCodigo(final Long codigo);

}
