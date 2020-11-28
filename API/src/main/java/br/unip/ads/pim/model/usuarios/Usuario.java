package br.unip.ads.pim.model.usuarios;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import br.unip.ads.pim.model.BaseEntity;
import br.unip.ads.pim.model.interesses.Interesse;

@Entity
public class Usuario extends BaseEntity {

	@Column(nullable = false)
	private String nome;
	@Column(nullable = false, unique = true)
	private String documento;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private String senha;
	@Column(nullable = false, length = 3)
	@Enumerated(EnumType.STRING)
	private TipoUsuario tipo;
	@ManyToMany
	@JoinTable(
		name = "usuario_interesse", 
		joinColumns = { @JoinColumn(name = "usuario_id") },
		inverseJoinColumns = {@JoinColumn(name = "interesse_id") })
	private List<Interesse> interesses;
	@Column
	private boolean bloqueado;
	@Column
	private String telefone;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	public List<Interesse> getInteresses() {
		return interesses;
	}

	public void setInteresses(List<Interesse> interesses) {
		this.interesses = interesses;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}
