package br.unip.ads.pim.service.impl;

import org.springframework.data.repository.CrudRepository;

import br.unip.ads.pim.model.BaseEntity;
import br.unip.ads.pim.service.CrudService;
import br.unip.ads.pim.service.exception.NegocioException;
import br.unip.ads.pim.service.exception.SemResultadoException;

public abstract class BaseCrudService<T extends BaseEntity> implements CrudService<T> {

	protected abstract CrudRepository<T, Long> getRepository();
	
	@Override
	public void inserir(T entidade) {
		getRepository().save(entidade);
	}

	@Override
	public Iterable<T> buscarTodos() {
		return getRepository().findAll();
	}

	@Override
	public T buscarUm(Long id) {
		return getRepository().findById(id).orElseThrow(() -> new SemResultadoException());
	}

	@Override
	public void alterar(Long id, T entidade) {
		T entidadeBd = buscarUm(id);
		if (entidadeBd.getId().equals(entidade.getId())) {
			getRepository().save(entidade);
		} else {
			throw new NegocioException("Os identificadores para alteração são divergentes.");
		}
	}

	@Override
	public void bloquearDesbloquear(Long id) {
		T entidadeBd = buscarUm(id);
		getRepository().delete(entidadeBd);
	}

}
