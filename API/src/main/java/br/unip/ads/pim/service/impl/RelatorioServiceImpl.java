package br.unip.ads.pim.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unip.ads.pim.model.usuarios.TipoUsuario;
import br.unip.ads.pim.model.usuarios.Usuario;
import br.unip.ads.pim.repository.UsuarioRepository;
import br.unip.ads.pim.service.RelatorioService;
import br.unip.ads.pim.service.UsuarioService;

@Service
public class RelatorioServiceImpl implements RelatorioService {

	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public String exportarTodosUsuarios() {
		// Recupera todos os usuários (PF e PJ) do Banco de Dados
		Iterable<Usuario> usuarios = usuarioService.buscarTodos();
		return this.criarStringCsvUsuarios(usuarios);
	}

	@Override
	public String exportarUsuariosPorInteresses(List<Long> interesses) {
		Iterable<Usuario> usuarios;
		if (interesses == null || interesses.isEmpty()) {
			// Recupera todos os usuários (PF e PJ) do Banco de Dados sem interesses.
			usuarios = usuarioRepository.findByInteressesIsEmptyAndTipoNot(TipoUsuario.ADM);
		} else {
			// Recupera todos os usuários (PF e PJ) do Banco de Dados com os interesses em questão.
			usuarios = usuarioRepository.buscarPorInteressesRelacionados(interesses);
		}
		return this.criarStringCsvUsuarios(usuarios);
	}

	private String criarStringCsvUsuarios(Iterable<Usuario> usuarios) {
		// Cria um StringBuilder para estruturação do arquivo CSV (separado por vírgulas)
		StringBuilder sb = new StringBuilder();
		// Cria o cabeçalho do arquivo CSV
		String cabecalho = "Usuário,Documento,Email,Telefone,Interesses";
		this.criarLinha(sb, cabecalho);
		// Percore todos os usuários criando uma linha no CSV para cada um
		for (Usuario usuario : usuarios) {
			// Trata/Formata o Telefone e Interesses
			String telefone = Objects.isNull(usuario.getTelefone()) ? "" : usuario.getTelefone();
			String interesses = usuario.getInteresses().stream()
					.map(it -> it.getDescricao())
					.collect(Collectors.joining(" "));
			// Cria as colunas com seus respectivos dados:
			List<String> colunas = new ArrayList<>();
			colunas.add(usuario.getNome());
			colunas.add(usuario.getDocumento());
			colunas.add(usuario.getEmail());
			colunas.add(telefone);
			colunas.add(interesses);
			// Formata a linha	 no padrão CSV (separado por virgula):
			String linha = colunas.stream().collect(Collectors.joining(","));
			this.criarLinha(sb, linha);
		}
		return sb.toString();
	}

	private void criarLinha(StringBuilder sb, String dados) {
		sb.append(dados);
		sb.append(System.lineSeparator());
	}

}
