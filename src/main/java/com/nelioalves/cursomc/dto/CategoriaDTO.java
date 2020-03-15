package com.nelioalves.cursomc.dto;

import com.nelioalves.cursomc.domain.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@AllArgsConstructor
@Builder
@Data
public class CategoriaDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer id;

  @NotEmpty(message = "Preenchimento Obrigatório")
  @Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
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

  public CategoriaDTO(){}

  public CategoriaDTO(Categoria categoria){
    this.id = categoria.getId();
    this.nome = categoria.getNome();
  }

}
