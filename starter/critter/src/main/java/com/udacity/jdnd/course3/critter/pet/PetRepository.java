package com.udacity.jdnd.course3.critter.pet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * PetRepository interface for database operations on Pet entities.
 * 
 * Custom query methods explained:
 * Spring Data JPA automatically implements methods based on method names:
 * - findByOwnerId: Finds pets where owner.id equals the parameter
 *   Spring parses "findBy" + "Owner" + "Id" and creates appropriate SQL
 * 
 * @Query annotation allows custom JPQL (Java Persistence Query Language):
 * - JPQL uses entity names and properties, not table/column names
 * - :ownerId is a named parameter that matches the @Param annotation
 * - Alternative to method name conventions for complex queries
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    
    /**
     * Find all pets belonging to a specific owner (customer).
     * 
     * Spring Data JPA method naming convention:
     * - "findBy" tells Spring this is a query method
     * - "Owner" refers to the "owner" field in Pet entity
     * - "Id" refers to the "id" field of the owner (Customer entity)
     * 
     * Generated SQL equivalent: SELECT * FROM pets WHERE owner_id = ?
     * 
     * @param ownerId The ID of the customer who owns the pets
     * @return List of pets belonging to the specified owner
     */
    List<Pet> findByOwnerId(Long ownerId);
    
    /**
     * Alternative way to find pets by owner using @Query annotation.
     * This demonstrates JPQL (Java Persistence Query Language).
     * 
     * JPQL syntax:
     * - "SELECT p FROM Pet p" - select Pet entities (aliased as 'p')
     * - "WHERE p.owner.id = :ownerId" - filter by owner's id field
     * - ":ownerId" is a named parameter bound to method parameter
     * 
     * @param ownerId The ID of the customer who owns the pets
     * @return List of pets belonging to the specified owner
     */
    @Query("SELECT p FROM Pet p WHERE p.owner.id = :ownerId")
    List<Pet> findPetsByOwnerId(@Param("ownerId") Long ownerId);
    
    /**
     * Find pets by type using method naming convention.
     * Spring automatically generates: SELECT * FROM pets WHERE type = ?
     * 
     * @param type The type of pets to find (CAT, DOG, etc.)
     * @return List of pets of the specified type
     */
    List<Pet> findByType(PetType type);
    
    /**
     * Find pets by name (case-insensitive) using method naming.
     * "IgnoreCase" tells Spring to use case-insensitive comparison
     * 
     * @param name The name to search for (case-insensitive)
     * @return List of pets with matching names
     */
    List<Pet> findByNameIgnoreCase(String name);
}