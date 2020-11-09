package br.com.softplan.desafio.fullstack.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.softplan.desafio.fullstack.backend.model.Parecer;

/**
 * Interface que faz o encapsulamento da lógica para acessar os dados de parecer.
 * @author <a href="mailto:anderson.sensolo@gmail.com">Anderson B. Sensolo</a>
 * @since 07/11/2020
 */

public interface ParecerRepository extends JpaRepository<Parecer, Long> {

}
