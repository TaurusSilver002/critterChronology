package com.udacity.jdnd.course3.critter.pet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;

@Service
@Transactional
public class PetService {
    
    private final PetRepository petRepository;
    private final CustomerRepository customerRepository;
    
    @Autowired
    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }
    
    public Pet savePet(Pet pet, Long ownerId) {
        Customer owner = customerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + ownerId));
        
        pet.setOwner(owner);
        Pet savedPet = petRepository.save(pet);
        
        owner.addPet(savedPet);
        
        return savedPet;
    }
    
    public Pet getPetById(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + petId));
    }
    
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }
    
    public List<Pet> getPetsByOwner(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }
}