package com.udacity.jdnd.course3.critter.schedule;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeService;

/**
 * Handles web requests related to Schedules.
 * 
 * This controller is more complex because it needs to:
 * - Handle many-to-many relationships (schedules ↔ employees, schedules ↔ pets)
 * - Convert between entity IDs in DTOs and actual entity objects
 * - Coordinate between multiple services (schedule, employee, pet)
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    private final EmployeeService employeeService;
    private final PetService petService;
    
    /**
     * Constructor injection for all required services.
     * ScheduleController needs multiple services because:
     * - ScheduleService: For schedule operations
     * - EmployeeService: To fetch employee entities from IDs
     * - PetService: To fetch pet entities from IDs
     */
    @Autowired
    public ScheduleController(ScheduleService scheduleService,
                            EmployeeService employeeService,
                            PetService petService) {
        this.scheduleService = scheduleService;
        this.employeeService = employeeService;
        this.petService = petService;
    }

    /**
     * Create a new schedule.
     * 
     * HTTP POST /schedule
     * Request body: ScheduleDTO (JSON) - contains employee IDs, pet IDs, date, activities
     * Response: ScheduleDTO (JSON) with generated schedule ID
     * 
     * Complex conversion process:
     * 1. Convert employee IDs to Employee entities
     * 2. Convert pet IDs to Pet entities
     * 3. Create Schedule entity with relationships
     * 4. Save via service (with validation)
     * 5. Convert saved Schedule back to ScheduleDTO
     */
    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        // throw new UnsupportedOperationException();
        // Replaced with complex DTO-to-entity conversion:
        // - Fetches actual employee and pet entities from their IDs
        // - Creates proper entity relationships for persistence
        // - Uses service layer for business logic and validation
        
        Schedule schedule = convertDTOToEntity(scheduleDTO);
        Schedule savedSchedule = scheduleService.createSchedule(schedule);
        return convertEntityToDTO(savedSchedule);
    }

    /**
     * Get all schedules.
     * 
     * HTTP GET /schedule
     * Response: List<ScheduleDTO> (JSON array)
     */
    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        // throw new UnsupportedOperationException();
        // Replaced with service call to get all schedules
        // Converts entity relationships back to ID lists in DTOs
        
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return schedules.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get schedules for a specific pet.
     * 
     * HTTP GET /schedule/pet/{petId}
     * Path variable: petId (long)
     * Response: List<ScheduleDTO> (JSON array)
     */
    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        // throw new UnsupportedOperationException();
        // Replaced with service call that uses repository's many-to-many query
        // Service validates pet exists and returns all related schedules
        
        List<Schedule> schedules = scheduleService.getSchedulesForPet(petId);
        return schedules.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get schedules for a specific employee.
     * 
     * HTTP GET /schedule/employee/{employeeId}
     * Path variable: employeeId (long)
     * Response: List<ScheduleDTO> (JSON array)
     */
    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        // throw new UnsupportedOperationException();
        // Replaced with service call for employee schedules
        // Uses repository's join query to find schedules containing this employee
        
        List<Schedule> schedules = scheduleService.getSchedulesForEmployee(employeeId);
        return schedules.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get schedules for all pets belonging to a customer.
     * 
     * HTTP GET /schedule/customer/{customerId}
     * Path variable: customerId (long)
     * Response: List<ScheduleDTO> (JSON array)
     * 
     * This is a complex query that crosses multiple relationships:
     * Customer → Pets → Schedules
     */
    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        // throw new UnsupportedOperationException();
        // Replaced with service call that performs complex relationship query
        // Finds all schedules for any pet owned by the customer
        
        List<Schedule> schedules = scheduleService.getSchedulesForCustomer(customerId);
        return schedules.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert ScheduleDTO to Schedule entity (complex manual mapping).
     * 
     * This is the most complex conversion because:
     * - Employee IDs must be converted to Employee entities
     * - Pet IDs must be converted to Pet entities
     * - Proper entity relationships must be established
     */
    private Schedule convertDTOToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setId(scheduleDTO.getId() != 0 ? scheduleDTO.getId() : null);
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());
        
        // Convert employee IDs to Employee entities
        if (scheduleDTO.getEmployeeIds() != null) {
            List<Employee> employees = scheduleDTO.getEmployeeIds().stream()
                    .map(employeeService::getEmployeeById)
                    .collect(Collectors.toList());
            schedule.setEmployees(employees);
        }
        
        // Convert pet IDs to Pet entities
        if (scheduleDTO.getPetIds() != null) {
            List<Pet> pets = scheduleDTO.getPetIds().stream()
                    .map(petService::getPetById)
                    .collect(Collectors.toList());
            schedule.setPets(pets);
        }
        
        return schedule;
    }
    
    /**
     * Convert Schedule entity to ScheduleDTO (manual mapping).
     * 
     * Converts entity relationships back to ID lists for API response.
     */
    private ScheduleDTO convertEntityToDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());
        
        // Convert Employee entities to employee IDs
        if (schedule.getEmployees() != null) {
            List<Long> employeeIds = schedule.getEmployees().stream()
                    .map(Employee::getId)
                    .collect(Collectors.toList());
            scheduleDTO.setEmployeeIds(employeeIds);
        }
        
        // Convert Pet entities to pet IDs
        if (schedule.getPets() != null) {
            List<Long> petIds = schedule.getPets().stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList());
            scheduleDTO.setPetIds(petIds);
        }
        
        return scheduleDTO;
    }
}
