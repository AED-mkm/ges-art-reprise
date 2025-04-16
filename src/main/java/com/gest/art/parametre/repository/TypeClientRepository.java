package com.gest.art.parametre.repository;



import com.gest.art.parametre.entite.TypeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeClientRepository  extends JpaRepository<TypeClient, String> {

	Optional<TypeClient> findTypeClientById(String id);

}
