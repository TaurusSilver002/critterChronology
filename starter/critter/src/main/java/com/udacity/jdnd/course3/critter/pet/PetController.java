package com.udacity.jdnd.course3.critter.pet;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// REST Controller - handles HTTP requests related to pets
@RestController
@RequestMapping("/pet") // Base URL path for all pet endpoints
public class PetController {
    
    // Inject the service layer to handle business logic
    private final PetService petService;
    
    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    // POST /pet - Creates a new pet
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertDTOToEntity(petDTO);
        Pet savedPet = petService.savePet(pet, petDTO.getOwnerId());
        return convertEntityToDTO(savedPet);
    }

    // GET /pet/{id} - Retrieves a specific pet by ID
    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);
        return convertEntityToDTO(pet);
    }

    // GET /pet - Retrieves all pets
    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getAllPets();
        return pets.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    // GET /pet/owner/{id} - Retrieves all pets for a specific owner
    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.getPetsByOwner(ownerId);
        return pets.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }
    
    // Helper method: Converts PetDTO (API layer) to Pet entity (database layer)
    private Pet convertDTOToEntity(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setId(petDTO.getId() != 0 ? petDTO.getId() : null);
        pet.setType(petDTO.getType());
        pet.setName(petDTO.getName());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());
        return pet;
    }
    
    // Helper method: Converts Pet entity (database layer) to PetDTO (API layer)
    private PetDTO convertEntityToDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setType(pet.getType());
        petDTO.setName(pet.getName());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setNotes(pet.getNotes());
        petDTO.setOwnerId(pet.getOwner().getId());
        return petDTO;
    }
}
