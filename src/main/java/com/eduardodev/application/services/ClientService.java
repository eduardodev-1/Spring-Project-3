package com.eduardodev.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eduardodev.application.dto.ClientDTO;
import com.eduardodev.application.entities.Client;
import com.eduardodev.application.exceptions.DataBaseException;
import com.eduardodev.application.exceptions.ResourceNotFoundException;
import com.eduardodev.application.repositories.ClientRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Client client = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente Inexistente"));
		ClientDTO dto = new ClientDTO(client);
		return dto;
	}
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(Pageable pageable) {
		Page<Client> result = repository.findAll(pageable);
		return result.map(x -> new ClientDTO(x));
	}
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
			Client entity = new Client();
			copyDtoToEntity(entity, dto);
			entity = repository.save(entity);
			return new ClientDTO(entity);
	}
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {

		try {
			Client entity = repository.getReferenceById(id);
			copyDtoToEntity(entity, dto);
			entity = repository.save(entity);
			return new ClientDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Cliente Inexistente");
		}
	}
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Cliente Inexistente");
		}
		try {
			repository.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Falha de integridade referencial");
		}
	}
	private void copyDtoToEntity(Client entity, ClientDTO dto) {
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
	}
}
