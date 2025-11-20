package com.udacity.jdnd.course3.critter.pet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;

// Service layer - contains business logic for pet operations
@Service
@Transactional // Ensures all database operations are atomic
public class PetService {
    
    // Dependency injection - Spring automatically provides these repositories
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;
    
    // Constructor injection - preferred way to inject dependencies
    @Autowired
    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }
    
    // Saves a new pet and establishes relationship with owner
    public Pet savePet(Pet pet, Long ownerId) {
        // Find the customer who will own this pet
        Customer owner = customerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + ownerId));
        
        // Set up the bidirectional relationship
        pet.setOwner(owner);
        Pet savedPet = petRepository.save(pet);
        
        // Update owner's pet collection for in-memory consistency
        owner.addPet(savedPet);
        
        return savedPet;
    }
    
    // Retrieves a specific pet by its ID, throws exception if not found
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + petId));
    }
    
    // Retrieves all pets from the database
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }
    
    // Retrieves all pets belonging to a specific owner
    public List<Pet> getPetsByOwner(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }
}