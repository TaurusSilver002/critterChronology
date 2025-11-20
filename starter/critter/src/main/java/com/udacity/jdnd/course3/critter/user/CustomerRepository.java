package com.udacity.jdnd.course3.critter.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for Customer entity database operations
// Extends JpaRepository for automatic CRUD operations
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Inherits standard methods: save(), findById(), findAll(), delete(), etc.
    // No custom queries needed - basic CRUD operations are sufficient
}