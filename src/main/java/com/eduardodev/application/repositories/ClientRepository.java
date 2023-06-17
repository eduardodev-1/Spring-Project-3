package com.eduardodev.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eduardodev.application.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
}
