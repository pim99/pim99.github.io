package br.unip.ads.pim.service;

import java.util.List;

public interface RelatorioService {

	String exportarTodosUsuarios();

	String exportarUsuariosPorInteresses(List<Long> interesses);

}

