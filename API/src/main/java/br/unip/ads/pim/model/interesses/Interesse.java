package br.unip.ads.pim.model.interesses;

import javax.persistence.Column;
import javax.persistence.Entity;

import br.unip.ads.pim.model.BaseEntity;

@Entity
public class Interesse extends BaseEntity {
	
	@Column(nullable = false)
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
