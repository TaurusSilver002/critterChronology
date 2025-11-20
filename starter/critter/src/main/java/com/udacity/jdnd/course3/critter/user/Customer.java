package com.udacity.jdnd.course3.critter.user;

import java.util.ArrayList;
import java.util.List;

import com.udacity.jdnd.course3.critter.pet.Pet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Customer entity representing pet owners in the critter system.
 * Simplified version with only required JPA annotations.
 */
@Entity // JPA annotation: Marks this class as a database entity
public class Customer {
    
    @Id // JPA annotation: Primary key identifier
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment strategy
    private Long id;
    
    // Simple fields mapped to database columns
    private String name;
    private String phoneNumber;
    private String notes;
    
    // JPA relationship: One customer can have many pets
    // mappedBy="owner" indicates Pet entity owns the foreign key
    // cascade=ALL means operations on customer cascade to pets
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
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
     * Important: Maintains both sides of the @OneToMany/@ManyToOne relationship
     */
    public void addPet(Pet pet) {
        pets.add(pet);      // Add to customer's pet list
        pet.setOwner(this); // Set customer as pet's owner
    }
    
    /**
     * Utility method to remove a pet and maintain bidirectional relationship
     * Important: Cleans up both sides of the relationship
     */
    public void removePet(Pet pet) {
        pets.remove(pet);     // Remove from customer's pet list
        pet.setOwner(null);   // Clear pet's owner reference
    }
}