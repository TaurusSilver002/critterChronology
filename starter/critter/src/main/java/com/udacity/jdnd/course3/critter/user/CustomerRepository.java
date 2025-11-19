package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CustomerRepository interface for database operations on Customer entities.
 * 
 * Annotations explained:
 * @Repository - Marks this interface as a Spring Data repository component
 *              Spring will automatically create an implementation at runtime
 * 
 * JpaRepository<Customer, Long> provides:
 * - Customer: The entity type this repository manages
 * - Long: The type of the entity's primary key (id field)
 * 
 * Inherited methods from JpaRepository include:
 * - save(Customer customer): Saves or updates a customer
 * - findById(Long id): Finds customer by ID
 * - findAll(): Retrieves all customers
 * - delete(Customer customer): Deletes a customer
 * - deleteById(Long id): Deletes customer by ID
 * - count(): Returns total number of customers
 * - existsById(Long id): Checks if customer exists
 * 
 * Spring Data JPA automatically implements this interface at runtime,
 * providing all CRUD operations without writing any implementation code.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // JpaRepository provides all basic CRUD operations
    // No additional methods needed for basic customer operations
    // Spring Data JPA will automatically implement:
    // - save(), findById(), findAll(), delete(), etc.
}