package br.unip.ads.pim.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.unip.ads.pim.model.interesses.Interesse;

@Repository
public interface InteresseRepository extends CrudRepository<Interesse, Long> {
	
}
