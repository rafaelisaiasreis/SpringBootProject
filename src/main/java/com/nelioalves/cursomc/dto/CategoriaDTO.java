package com.nelioalves.cursomc.dto;

import com.nelioalves.cursomc.domain.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoriaDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;
  private String nome;

  public void setId(Integer id) {
    this.id = id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Integer getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public CategoriaDTO(Categoria categoria){
    this.id = categoria.getId();
    this.nome = categoria.getNome();
  }

}
