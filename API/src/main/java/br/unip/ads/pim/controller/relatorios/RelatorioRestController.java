package br.unip.ads.pim.controller.relatorios;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.unip.ads.pim.config.docs.OpenApiConfig;
import br.unip.ads.pim.controller.BaseRestController;
import br.unip.ads.pim.service.RelatorioService;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@Tag(name = OpenApiConfig.TAG_RELATORIOS)
@RestController
public class RelatorioRestController extends BaseRestController {

	@Autowired
	private RelatorioService camadaNegocio;

	@PreAuthorize("hasAuthority('ADM')")
	@GetMapping("relatorios/usuarios")
	public void exportarTodosUsuarios(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=usuarios.csv");

		try (OutputStream outputStream = response.getOutputStream()) {
			String csv = camadaNegocio.exportarTodosUsuarios();
			outputStream.write(csv.getBytes());
			outputStream.flush();
		}
	}

	@PreAuthorize("hasAuthority('ADM')")
	@GetMapping("relatorios/usuarios/interesses")
	public void exportarUsuariosPorInteresses(@RequestParam List<Long> interesses, HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=usuarios.csv");

		try (OutputStream outputStream = response.getOutputStream()) {
			String csv = camadaNegocio.exportarUsuariosPorInteresses(interesses);
			outputStream.write(csv.getBytes());
			outputStream.flush();
		}
	}

}
