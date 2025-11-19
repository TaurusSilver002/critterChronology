package com.udacity.jdnd.course3.critter.user;

import java.util.ArrayList;
import java.util.List;

import com.udacity.jdnd.course3.critter.pet.Pet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Customer entity representing pet owners in the critter system.
 * 
 * Annotations explained:
 * @Entity - Marks this class as a JPA entity that maps to a database table
 * @Table - Specifies the table name in the database (optional, defaults to class name)
 * @Id - Marks the primary key field
 * @GeneratedValue - Automatically generates primary key values using specified strategy
 * @Column - Maps the field to a specific database column with constraints
 * @OneToMany - Defines one-to-many relationship (one customer can have many pets)
 *   - mappedBy: Specifies the field in the Pet entity that owns the relationship
 *   - cascade: Operations that should cascade to related entities
 *   - fetch: Loading strategy (LAZY = load on demand, EAGER = load immediately)
 *   - orphanRemoval: Automatically delete pets when removed from customer's pet list
 */
@Entity
@Table(name = "customers")
public class Customer {
    
    /**
     * Primary key with auto-generation
     * IDENTITY strategy uses database's auto-increment feature
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Customer name with database constraints
     * nullable = false: Creates NOT NULL constraint
     * length = 100: Creates VARCHAR(100) column
     */
    @Column(nullable = false, length = 100)
    private String name;
    
    /**
     * Phone number field with length constraint
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    /**
     * Optional notes field with larger text capacity
     */
    @Column(length = 500)
    private String notes;
    
    /**
     * One-to-Many relationship with Pet entity
     * mappedBy = "owner": Pet entity has an "owner" field that references this Customer
     * cascade = ALL: All operations (save, update, delete) cascade to pets
     * fetch = LAZY: Pets are loaded only when accessed (better performance)
     * orphanRemoval = true: If a pet is removed from this list, it's deleted from database
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();
    
    // Default constructor required by JPA
    public Customer() {}
    
    // Constructor for creating new customers
    public Customer(String name, String phoneNumber, String notes) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public List<Pet> getPets() {
        return pets;
    }
    
    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
    
    /**
     * Utility method to add a pet and maintain bidirectional relationship
     */
    public void addPet(Pet pet) {
        pets.add(pet);
        pet.setOwner(this);
    }
    
    /**
     * Utility method to remove a pet and maintain bidirectional relationship
     */
    public void removePet(Pet pet) {
        pets.remove(pet);
        pet.setOwner(null);
    }
}