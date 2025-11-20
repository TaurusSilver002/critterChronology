package com.udacity.jdnd.course3.critter.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository layer - handles database operations for Pet entities
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    
    // Custom query method - Spring Data JPA automatically generates SQL
    // Finds all pets that belong to a specific owner by their ID
    List<Pet> findByOwnerId(Long ownerId);
}