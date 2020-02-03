package com.nelioalves.cursomc.services;

import java.util.Optional;

import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

  @Autowired
  private CategoriaRepository repo;

  public Categoria find(Integer id) {
    Optional<Categoria> obj = repo.findById(id);
    return obj.orElseThrow(() -> new ObjectNotFoundException(
        "Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
  }

  public Categoria insert(Categoria categoria) {
    categoria.setId(null); //O JPA entende que , como id é null, se trata de uma inserção
    return repo.save(categoria);
  }

  public Categoria update(Categoria categoria) {
    find(categoria.getId());
    return repo.save(categoria); // como o id não é nulo , ele atualiza o id existente
  }

  public void delete(Integer id) {
    find(id);
    try {
      repo.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
    }

  }

}
