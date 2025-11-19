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

/**
 * Handles web requests related to Pets.
 * 
 * Controller annotations explained:
 * @RestController - Combines @Controller and @ResponseBody
 *                  Automatically converts return values to JSON/XML
 * @RequestMapping - Maps HTTP requests to handler methods
 *                  Class-level mapping applies to all methods in the class
 * @PostMapping - Maps HTTP POST requests to the method
 * @GetMapping - Maps HTTP GET requests to the method
 * @PathVariable - Binds URI template variable to method parameter
 * @RequestBody - Binds HTTP request body to method parameter
 * @Autowired - Tells Spring to inject dependencies automatically
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    
    private final PetService petService;
    
    /**
     * Constructor injection for PetService dependency.
     * @Autowired is optional when there's only one constructor.
     */
    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * Save a new pet.
     * 
     * HTTP POST /pet
     * Request body: PetDTO (JSON)
     * Response: PetDTO (JSON) with generated ID
     * 
     * Process:
     * 1. Convert PetDTO to Pet entity
     * 2. Call service to save pet with owner
     * 3. Convert saved Pet back to PetDTO
     * 4. Return PetDTO (Spring converts to JSON automatically)
     */
    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        // throw new UnsupportedOperationException();
        // Replaced with actual implementation that:
        // - Converts DTO to entity for business logic layer
        // - Uses service layer for data persistence
        // - Converts entity back to DTO for API response
        
        Pet pet = convertDTOToEntity(petDTO);
        Pet savedPet = petService.savePet(pet, petDTO.getOwnerId());
        return convertEntityToDTO(savedPet);
    }

    /**
     * Get a specific pet by ID.
     * 
     * HTTP GET /pet/{petId}
     * Path variable: petId (long)
     * Response: PetDTO (JSON)
     */
    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        // throw new UnsupportedOperationException();
        // Replaced with service call to retrieve pet by ID
        // Service handles validation and throws exception if not found
        
        Pet pet = petService.getPetById(petId);
        return convertEntityToDTO(pet);
    }

    /**
     * Get all pets in the system.
     * 
     * HTTP GET /pet
     * Response: List<PetDTO> (JSON array)
     */
    @GetMapping
    public List<PetDTO> getPets(){
        // throw new UnsupportedOperationException();
        // Replaced with service call to get all pets
        // Uses Java Streams to convert entities to DTOs
        
        List<Pet> pets = petService.getAllPets();
        return pets.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all pets belonging to a specific owner.
     * 
     * HTTP GET /pet/owner/{ownerId}
     * Path variable: ownerId (long)
     * Response: List<PetDTO> (JSON array)
     */
    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        // throw new UnsupportedOperationException();
        // Replaced with service call to get pets by owner
        // Service validates owner exists and returns their pets
        
        List<Pet> pets = petService.getPetsByOwner(ownerId);
        return pets.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert PetDTO to Pet entity (manual mapping).
     * 
     * Why manual mapping instead of ModelMapper:
     * - Better control over conversion logic
     * - No additional dependencies
     * - Clear understanding of what fields are mapped
     * - Easier to debug and maintain
     */
    private Pet convertDTOToEntity(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setId(petDTO.getId() != 0 ? petDTO.getId() : null);
        pet.setType(petDTO.getType());
        pet.setName(petDTO.getName());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setNotes(petDTO.getNotes());
        // Note: Owner is set in the service layer based on ownerId
        return pet;
    }
    
    /**
     * Convert Pet entity to PetDTO (manual mapping).
     */
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
