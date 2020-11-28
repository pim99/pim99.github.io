package br.unip.ads.pim.service;

/**
 * C - Create
 * R - Read
 * U - Update
 * D - Delete
 */
public interface CrudService<T> {
	
	void inserir(T entidade);
	
	Iterable<T> buscarTodos();
	
	T buscarUm(Long id);
	
	void alterar(Long id, T entidade);
	
	void bloquearDesbloquear(Long id);
}
