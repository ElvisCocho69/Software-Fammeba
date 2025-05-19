package com.api.server.persistence.repository.client;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.client.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByDocumentnumber(String documentnumber);
    
    Page<Client> findByClienttype(String clienttype, Pageable pageable);
    
    Page<Client> findByClientstatus(Client.ClientStatus status, Pageable pageable);
    
    Page<Client> findByClienttypeAndClientstatus(String clienttype, Client.ClientStatus status, Pageable pageable);
}
