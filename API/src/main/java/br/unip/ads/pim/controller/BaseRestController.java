package br.unip.ads.pim.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.unip.ads.pim.service.exception.NegocioException;
import br.unip.ads.pim.service.exception.SemResultadoException;

@RequestMapping("/api")
public abstract class BaseRestController {
	
	@ExceptionHandler(SemResultadoException.class)
	private ResponseEntity<Void> handlerSemResultadoException(SemResultadoException e) {
		return ResponseEntity.noContent().build();
	}
	
	@ExceptionHandler(NegocioException.class)
	private ResponseEntity<ErrorResponse> handlerNegocioException(NegocioException e) {
		ErrorResponse error = new ErrorResponse();
		error.setMensagem(e.getMessage());
		return ResponseEntity.badRequest().body(error);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	private ResponseEntity<ErrorResponse> handlerNegocioException(AccessDeniedException e) {
		ErrorResponse error = new ErrorResponse();
		error.setMensagem("Acesso negado.");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
	}

	@ExceptionHandler(Throwable.class)
	private ResponseEntity<ErrorResponse> handlerErroInesperado(Throwable e) {
		e.printStackTrace();
		ErrorResponse error = new ErrorResponse();
		error.setMensagem("Ops, ocorreu um erro inesperado.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	protected URI criarUri(Long idGerado) {
		return ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(idGerado)
				.toUri();
	}
}
