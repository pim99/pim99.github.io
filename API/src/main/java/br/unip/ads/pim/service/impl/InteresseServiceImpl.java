package br.unip.ads.pim.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.unip.ads.pim.model.interesses.Interesse;
import br.unip.ads.pim.repository.InteresseRepository;
import br.unip.ads.pim.service.InteresseService;

@Service
public class InteresseServiceImpl extends BaseCrudService<Interesse> implements InteresseService {

	@Autowired
	private InteresseRepository repository;
	
	@Override
	protected CrudRepository<Interesse, Long> getRepository() {
		return repository;
	}

}
