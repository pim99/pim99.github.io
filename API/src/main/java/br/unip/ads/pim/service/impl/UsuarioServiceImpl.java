package br.unip.ads.pim.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.unip.ads.pim.model.usuarios.TipoUsuario;
import br.unip.ads.pim.model.usuarios.Usuario;
import br.unip.ads.pim.repository.UsuarioRepository;
import br.unip.ads.pim.service.UsuarioService;
import br.unip.ads.pim.service.exception.NegocioException;

@Service
public class UsuarioServiceImpl extends BaseCrudService<Usuario> implements UsuarioService {

	@Autowired
	private UsuarioRepository repository;
	
	@Override
	protected CrudRepository<Usuario, Long> getRepository() {
		return repository;
	}

	@Override
	public Iterable<Usuario> buscarTodos() {
		return repository.buscarPorNaoAdmOrdenandoPorNome();
		
		// Alternativa: Ordenar a lista no Java (usando a API de Stream do Java 8):
		//return  () -> StreamSupport.stream(super.buscarTodos().spliterator(), false)
		//		.sorted((user1, user2) -> user1.getNome().compareTo(user2.getNome()))
		//		.iterator();
	}
	
	@Override
	public void inserir(Usuario entidade) {
		validarUsuario(entidade);
		super.inserir(entidade);
	}
	
	@Override
	public void alterar(Long id, Usuario entidade) {
		// Recupera o usuário do Banco de Dados
		final Usuario entidadeParaAtualizacao = super.buscarUm(id);
		// Atribui o ID da entidade para executar a regra no método 'alterar'
		entidadeParaAtualizacao.setId(entidade.getId());
		// Atribui apenas os dados negocialmente alteráveis
		entidadeParaAtualizacao.setNome(entidade.getNome());
		entidadeParaAtualizacao.setDocumento(entidade.getDocumento());
		entidadeParaAtualizacao.setTipo(entidade.getTipo());
		entidadeParaAtualizacao.setTelefone(entidade.getTelefone());
		entidadeParaAtualizacao.setInteresses(entidade.getInteresses());
		validarUsuario(entidadeParaAtualizacao);
		super.alterar(id, entidadeParaAtualizacao);
	}

	@Override
	public Usuario logar(Usuario entidade) {
		String email = entidade.getEmail();
		String senha = entidade.getSenha();
		
		validarSenha(senha);
		validarEmail(email);
		
		Usuario usuarioLogado = repository.findByEmailAndSenha(email, senha)
				.orElseThrow(() -> new NegocioException("E-mail ou senha inválidos."));
		
		if (usuarioLogado.isBloqueado()) {
			throw new NegocioException("Usuário bloqueado, entre em contato com o administrador.");
		}
		
		// Evita que a senha seja enviada para o cliente (Web ou Android).
		usuarioLogado.setSenha(null);
		
		return usuarioLogado;
	}
	
	@Override
	public void bloquearDesbloquear(Long id) {
		Usuario usuario = super.buscarUm(id);
		usuario.setBloqueado(!usuario.isBloqueado());
		super.alterar(id, usuario);
	}
	
	/**
	 * Regras de negócio para a inclusão/altreção de Usuário.
	 * 
	 * @param entidade objeto que será validado.
	 */
	private void validarUsuario(Usuario entidade) {
		if (StringUtils.isBlank(entidade.getNome())) {
			throw new NegocioException("O preenchimento do nome é obrigatório.");
		}
		
		validarDocumento(entidade);
		
		validarEmail(entidade.getEmail());
		
		validarSenha(entidade.getSenha());
	}

	protected void validarDocumento(Usuario entidade) {
		if (StringUtils.isBlank(entidade.getDocumento())) {
			throw new NegocioException("O preenchimento do documento é obrigatório.");
		}
		String documentoSemMascara = entidade.getDocumento().replaceAll("[^0-9]", "");
		if (TipoUsuario.PF.equals(entidade.getTipo())) {
			try {
				new CPFValidator().assertValid(documentoSemMascara);
			} catch (InvalidStateException e) {
				throw new NegocioException("CPF inválido.");
			}
		} else if (TipoUsuario.PJ.equals(entidade.getTipo())) {
			try {
				new CNPJValidator().assertValid(documentoSemMascara);
			} catch (InvalidStateException e) {
				throw new NegocioException("CNPJ inválido.");
			}
		} else {
			throw new NegocioException("Tipo de usuário inválido.");
		}
		entidade.setDocumento(documentoSemMascara);
	}

	private void validarSenha(String senha) {
		if (StringUtils.isBlank(senha)) {
			throw new NegocioException("O preenchimento da senha é obrigatório.");
		}
	}

	private void validarEmail(String email) {
		if (StringUtils.isBlank(email)) {
			throw new NegocioException("O preenchimento do e-mail é obrigatório.");
		}
		if (!EmailValidator.getInstance().isValid(email)) {
			throw new NegocioException("O e-mail é inválido.");
		}
	}

}
