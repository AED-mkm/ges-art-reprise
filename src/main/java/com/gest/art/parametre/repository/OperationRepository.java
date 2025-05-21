package com.gest.art.parametre.repository;

import com.gest.art.parametre.entite.Operation;
import com.gest.art.parametre.entite.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, String> {
}
