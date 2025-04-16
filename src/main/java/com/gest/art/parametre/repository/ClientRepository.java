package com.gest.art.parametre.repository;


import com.gest.art.parametre.entite.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
	Client findClientById(String id);
}
