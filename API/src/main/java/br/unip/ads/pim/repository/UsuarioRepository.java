package br.unip.ads.pim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.unip.ads.pim.model.usuarios.TipoUsuario;
import br.unip.ads.pim.model.usuarios.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Optional<Usuario> findByEmailAndSenha(String email, String senha);
	
	//Opção 1: Buscar todos ordenados por nome usando o nome do método (Spring Data)
	Iterable<Usuario> findByOrderByNomeAsc();

	//Opção 2: Buscar todos ordenados por nome usando uma query HQL (Hibernate)
	@Query("FROM Usuario u WHERE u.tipo != 'ADM' ORDER BY u.nome ASC")
	Iterable<Usuario> buscarPorNaoAdmOrdenandoPorNome();
	
	/**
	 * Query personalizada para busca de usuários (PF e PJ) com os interesses passados como parâmetro.
	 * 
	 * @param interesses lista de interesses para busca dos usuários.
	 * 
	 * @return lista de usuários (PF e PJ) que possuem ao menos um dos interesses recebidos por parâmetro.
	 */
	@Query("SELECT DISTINCT u FROM Usuario u "
			+ "JOIN FETCH u.interesses i "
			+ "WHERE u.tipo != 'ADM' AND i.id IN (:interesses) "
			+ "ORDER BY u.nome ASC")
	Iterable<Usuario> buscarPorInteressesRelacionados(@Param("interesses") List<Long> interesses);
	
	/**
	 * Query nomeada (Spring Data) para busca de usuários (PF e PJ) sem interesses cadastrados.
	 * 
	 * @param tipo passar o valor TipoUsuario.ADM para um resultado adequado.
	 * 
	 * @return lista de usuários (PF e PJ) sem interesses cadastrados.
	 */
	Iterable<Usuario> findByInteressesIsEmptyAndTipoNot(TipoUsuario tipo);
}
