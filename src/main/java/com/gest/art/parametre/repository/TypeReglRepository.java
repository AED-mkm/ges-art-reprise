package com.gest.art.parametre.repository;




import com.gest.art.parametre.entite.TypeReglement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeReglRepository extends JpaRepository<TypeReglement, String> {
  Optional <TypeReglement> findTypeReglementById(String id);
}
