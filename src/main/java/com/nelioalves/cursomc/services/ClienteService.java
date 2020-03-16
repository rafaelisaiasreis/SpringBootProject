package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.dto.ClienteNewDTO;
import com.nelioalves.cursomc.repositories.CidadeRepository;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

  @Autowired
  CidadeRepository cidadeRepository;
  @Autowired
  private ClienteRepository repo;
  @Autowired
  EnderecoRepository enderecoRepository;

  public Cliente find(Integer id) {
    Optional<Cliente> obj = repo.findById(id);
    return obj.orElseThrow(() -> new ObjectNotFoundException(
        "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
  }

  public Cliente update(Cliente obj) {
    Cliente newObj = find(obj.getId());
    updateData(newObj, obj);
    return repo.save(newObj); // como o id não é nulo , ele atualiza o id existente
  }

  private void updateData(Cliente newObj, Cliente obj) {
    newObj.setNome(obj.getNome());
    newObj.setEmail(obj.getEmail());
  }

  public void delete(Integer id) {
    find(id);
    try {
      repo.deleteById(id);
    } catch (DataIntegrityViolationException e) {
      throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
    }
  }

  public List<Cliente> findAll() {
    return repo.findAll();
  }

  public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy,
                                String direction) {
    PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    return repo.findAll(pageRequest);
  }

  public Cliente fromDTO(ClienteDTO ClienteDTO) {
    return new Cliente(ClienteDTO.getId(), ClienteDTO.getNome(), ClienteDTO.getEmail(), null, null);
  }

  public Cliente fromDTO(ClienteNewDTO clienteNewDTO) {

    Cliente cliente = new Cliente(null, clienteNewDTO.getNome(), clienteNewDTO.getEmail(),
        clienteNewDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDTO.getTipo()));

    final Cidade cidade =
        cidadeRepository.findById(clienteNewDTO.getCidadeId()).orElseThrow(() -> new RuntimeException(
            "Cidade não encontrada"));

    final Endereco endereco = new Endereco(null, clienteNewDTO.getLogradouro(),
        clienteNewDTO.getNumero(), clienteNewDTO.getComplemento(), clienteNewDTO.getBairro(),
        clienteNewDTO.getCep(), cliente, cidade);

    cliente.getEnderecos().add(endereco);
    cliente.getTelefones().add(clienteNewDTO.getTelefoneUm());

    if (clienteNewDTO.getTelefoneDois() != null)
      cliente.getTelefones().add(clienteNewDTO.getTelefoneDois());
    if (clienteNewDTO.getTelefoneTres() != null)
      cliente.getTelefones().add(clienteNewDTO.getTelefoneTres());

    return cliente;
  }

  public Cliente insert(Cliente cliente) {
    cliente.setId(null); //O JPA entende que , como id é null, se trata de uma inserção
    cliente = repo.save(cliente);
    enderecoRepository.saveAll(cliente.getEnderecos());
    return cliente;
  }

}
