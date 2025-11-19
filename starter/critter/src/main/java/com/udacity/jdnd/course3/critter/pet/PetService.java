package com.udacity.jdnd.course3.critter.pet;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;

/**
 * PetService handles business logic for Pet operations.
 * 
 * Key responsibilities:
 * - Managing pet CRUD operations
 * - Handling pet-owner relationships
 * - Ensuring data consistency between pets and customers
 */
@Service
@Transactional
public class PetService {
    
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;
    
    /**
     * Constructor injection for both repositories.
     * PetService needs both because:
     * - PetRepository for pet operations
     * - CustomerRepository to verify owners exist and update relationships
     */
    @Autowired
    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }
    
    /**
     * Save a new pet with an owner.
     * 
     * Business logic:
     * 1. Verify the owner (customer) exists
     * 2. Create bidirectional relationship between pet and owner
     * 3. Save the pet (owner is also updated due to cascade settings)
     * 
     * @param pet The pet to save
     * @param ownerId The ID of the customer who owns this pet
     * @return Saved pet with generated ID
     * @throws RuntimeException if owner not found
     */
    public Pet savePet(Pet pet, Long ownerId) {
        // Find the owner (customer)
        Customer owner = customerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + ownerId));
        
        // Set up bidirectional relationship
        pet.setOwner(owner);
        
        // Save pet (this also updates the owner's pets list due to cascade settings)
        Pet savedPet = petRepository.save(pet);
        
        // Explicitly add to owner's pets list to maintain bidirectional relationship
        if (!owner.getPets().contains(savedPet)) {
            owner.addPet(savedPet);
            customerRepository.save(owner); // Save updated owner
        }
        
        return savedPet;
    }
    
    /**
     * Find a pet by ID.
     * 
     * @param petId The ID of the pet to find
     * @return Optional containing the pet if found
     */
    public Optional<Pet> findPetById(Long petId) {
        return petRepository.findById(petId);
    }
    
    /**
     * Get a pet by ID, throwing exception if not found.
     * 
     * @param petId The ID of the pet to get
     * @return The pet entity
     * @throws RuntimeException if pet not found
     */
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + petId));
    }
    
    /**
     * Get all pets in the system.
     * 
     * @return List of all pets
     */
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }
    
    /**
     * Get all pets belonging to a specific owner.
     * 
     * @param ownerId The ID of the owner (customer)
     * @return List of pets owned by the customer
     */
    public List<Pet> getPetsByOwner(Long ownerId) {
        // Verify owner exists first
        customerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + ownerId));
        
        return petRepository.findByOwnerId(ownerId);
    }
    
    /**
     * Get pets by type.
     * 
     * @param type The type of pets to find (CAT, DOG, etc.)
     * @return List of pets of the specified type
     */
    public List<Pet> getPetsByType(PetType type) {
        return petRepository.findByType(type);
    }
    
    /**
     * Update an existing pet.
     * 
     * Business logic:
     * 1. Verify pet exists
     * 2. Update pet fields
     * 3. Handle owner changes if needed
     * 
     * @param petId The ID of pet to update
     * @param updatedPet The updated pet data
     * @return Updated pet entity
     */
    public Pet updatePet(Long petId, Pet updatedPet) {
        Pet existingPet = getPetById(petId);
        
        // Update basic fields
        existingPet.setType(updatedPet.getType());
        existingPet.setName(updatedPet.getName());
        existingPet.setBirthDate(updatedPet.getBirthDate());
        existingPet.setNotes(updatedPet.getNotes());
        
        // Handle owner change if needed
        if (updatedPet.getOwner() != null && 
            !existingPet.getOwner().getId().equals(updatedPet.getOwner().getId())) {
            
            // Remove from old owner
            Customer oldOwner = existingPet.getOwner();
            oldOwner.removePet(existingPet);
            customerRepository.save(oldOwner);//updating the customer without the updated pet
            
            // Add to new owner
            Customer newOwner = updatedPet.getOwner();
            newOwner.addPet(existingPet);
            existingPet.setOwner(newOwner);
            customerRepository.save(newOwner);//updating the another customer with the new pet
        }
        
        return petRepository.save(existingPet);//updating the pet with new details
    }
    
    /**
     * Delete a pet by ID.
     * 
     * This removes the pet from its owner's pets list and deletes the pet.
     * 
     * @param petId The ID of pet to delete
     */
    public void deletePet(Long petId) {
        Pet pet = getPetById(petId);
        
        // Remove from owner's pets list
        Customer owner = pet.getOwner();
        owner.removePet(pet);
        customerRepository.save(owner);
        
        // Delete the pet
        petRepository.deleteById(petId);
    }
    
    /**
     * Change pet's owner.
     * 
     * @param petId The ID of the pet
     * @param newOwnerId The ID of the new owner
     * @return Updated pet
     */
    public Pet changePetOwner(Long petId, Long newOwnerId) {
        Pet pet = getPetById(petId);
        Customer newOwner = customerRepository.findById(newOwnerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + newOwnerId));
        
        // Remove from old owner
        Customer oldOwner = pet.getOwner();
        oldOwner.removePet(pet);
        customerRepository.save(oldOwner);
        
        // Add to new owner
        pet.setOwner(newOwner);
        newOwner.addPet(pet);
        customerRepository.save(newOwner);
        
        return petRepository.save(pet);
    }
}