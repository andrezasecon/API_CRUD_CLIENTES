package com.andrezasecon.apiclientes.services;

import com.andrezasecon.apiclientes.dto.ClientDto;
import com.andrezasecon.apiclientes.entities.Client;
import com.andrezasecon.apiclientes.repositories.ClientRepository;
import com.andrezasecon.apiclientes.services.exceptions.DataBaseException;
import com.andrezasecon.apiclientes.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<ClientDto> findAllPageble(PageRequest pageRequest) {
        Page<Client> list = clientRepository.findAll(pageRequest);
        return list.map(x -> new ClientDto(x));
    }

    @Transactional(readOnly = true)
    public ClientDto findClientById(Long id) {
        Optional<Client> cli = clientRepository.findById(id);
        Client entity = cli.orElseThrow(() -> new ResourceNotFoundException("Id not found"));
        return new ClientDto(entity);
    }

    @Transactional
    public ClientDto insertClient(ClientDto dto) {
        Client entity = new Client();
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setBirthDate(dto.getBirthDate());
        entity.setIncome(dto.getIncome());
        entity.setChildren(dto.getChildren());
        entity = clientRepository.save(entity);
        return new ClientDto(entity);
    }

    @Transactional
    public ClientDto updateClient(Long id, ClientDto dto) {
        try {
            Client entity = clientRepository.getById(id);
            entity.setName(dto.getName());
            entity.setCpf(dto.getCpf());
            entity.setBirthDate(dto.getBirthDate());
            entity.setIncome(dto.getIncome());
            entity.setChildren(dto.getChildren());
            entity = clientRepository.save(entity);
            return new ClientDto(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
    }

    public void deleteClient(Long id) {
        try {
            clientRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity violation");
        }
    }
}
