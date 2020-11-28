package br.unip.ads.pim.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.unip.ads.pim.config.docs.OpenApiConfig;
import br.unip.ads.pim.controller.BaseRestController;
import br.unip.ads.pim.model.usuarios.Usuario;
import br.unip.ads.pim.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@Tag(name = OpenApiConfig.TAG_LOGIN)
@RestController
public class LoginRestController extends BaseRestController {

	@Autowired
	private UsuarioService camadaNegocio;

	@PostMapping("login")
	public ResponseEntity<Usuario> logar(@RequestBody Usuario entidade) {
		Usuario usuarioLogado = camadaNegocio.logar(entidade);
		return ResponseEntity.ok(usuarioLogado);
	}

}
