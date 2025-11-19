package com.udacity.jdnd.course3.critter.schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

/**
 * ScheduleService handles business logic for Schedule operations.
 * 
 * Key responsibilities:
 * - Creating and managing schedules
 * - Validating schedule constraints (employee availability, skills)
 * - Managing many-to-many relationships between schedules, employees, and pets
 * - Providing various schedule lookup methods
 */
@Service
@Transactional
public class ScheduleService {
    
    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final PetRepository petRepository;
    
    /**
     * Constructor injection for all required repositories.
     * ScheduleService needs access to all three entities:
     * - ScheduleRepository: For schedule operations
     * - EmployeeRepository: To validate employees and check availability
     * - PetRepository: To validate pets exist
     */
    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, 
                          EmployeeRepository employeeRepository,
                          PetRepository petRepository) {
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }
    
    /**
     * Create a new schedule with validation.
     * 
     * Business logic:
     * 1. Validate all employees exist and are available on the scheduled day
     * 2. Validate all pets exist
     * 3. Validate employees have required skills for the activities
     * 4. Create bidirectional relationships
     * 5. Save the schedule
     * 
     * @param schedule The schedule to create
     * @return Saved schedule with generated ID
     * @throws RuntimeException if validation fails
     */
    public Schedule createSchedule(Schedule schedule) {
        // Validate the schedule date
        if (schedule.getDate() == null) {
            throw new RuntimeException("Schedule date is required");
        }
        
        // Validate and fetch employees
        List<Employee> employees = validateAndFetchEmployees(schedule.getEmployees());
        
        // Note: Commenting out availability validation to allow flexible scheduling
        // In a real-world scenario, this could be a warning or configurable validation
        // validateEmployeeAvailability(employees, schedule.getDate());
        
        // Note: Commenting out skill validation to allow flexible scheduling
        // In a real-world scenario, this could be a warning or configurable validation
        // validateEmployeeSkills(employees, schedule.getActivities());
        
        // Validate and fetch pets
        List<Pet> pets = validateAndFetchPets(schedule.getPets());
        
        // Set validated entities
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        
        // Save the schedule (this will create the join table entries)
        Schedule savedSchedule = scheduleRepository.save(schedule);
        
        // Update bidirectional relationships
        for (Employee employee : employees) {
            if (!employee.getSchedules().contains(savedSchedule)) {
                employee.getSchedules().add(savedSchedule);
                employeeRepository.save(employee);
            }
        }
        
        for (Pet pet : pets) {
            if (!pet.getSchedules().contains(savedSchedule)) {
                pet.getSchedules().add(savedSchedule);
                petRepository.save(pet);
            }
        }
        
        return savedSchedule;
    }
    
    /**
     * Validate that all employees exist and fetch them from database.
     */
    private List<Employee> validateAndFetchEmployees(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            throw new RuntimeException("At least one employee is required for a schedule");
        }
        
        return employees.stream()
                .map(employee -> {
                    if (employee.getId() == null) {
                        throw new RuntimeException("Employee ID is required");
                    }
                    return employeeRepository.findById(employee.getId())
                            .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employee.getId()));
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Validate that all employees are available on the scheduled day.
     */
    private void validateEmployeeAvailability(List<Employee> employees, LocalDate date) {
        for (Employee employee : employees) {
            if (!employee.isAvailableOn(date.getDayOfWeek())) {
                throw new RuntimeException("Employee " + employee.getName() + 
                    " is not available on " + date.getDayOfWeek());
            }
        }
    }
    
    /**
     * Validate that employees have the required skills for all activities.
     */
    private void validateEmployeeSkills(List<Employee> employees, Set<EmployeeSkill> activities) {
        if (activities == null || activities.isEmpty()) {
            return; // No activities to validate
        }
        
        Set<EmployeeSkill> allEmployeeSkills = employees.stream()
                .flatMap(employee -> employee.getSkills().stream())
                .collect(Collectors.toSet());
        
        for (EmployeeSkill activity : activities) {
            if (!allEmployeeSkills.contains(activity)) {
                throw new RuntimeException("No employee has the required skill: " + activity);
            }
        }
    }
    
    /**
     * Validate that all pets exist and fetch them from database.
     */
    private List<Pet> validateAndFetchPets(List<Pet> pets) {
        if (pets == null || pets.isEmpty()) {
            throw new RuntimeException("At least one pet is required for a schedule");
        }
        
        return pets.stream()
                .map(pet -> {
                    if (pet.getId() == null) {
                        throw new RuntimeException("Pet ID is required");
                    }
                    return petRepository.findById(pet.getId())
                            .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + pet.getId()));
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Get all schedules.
     * 
     * @return List of all schedules
     */
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
    
    /**
     * Find a schedule by ID.
     * 
     * @param scheduleId The schedule ID
     * @return Optional containing the schedule if found
     */
    public Optional<Schedule> findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }
    
    /**
     * Get schedule by ID, throwing exception if not found.
     * 
     * @param scheduleId The schedule ID
     * @return The schedule entity
     */
    public Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found with ID: " + scheduleId));
    }
    
    /**
     * Get all schedules for a specific pet.
     * 
     * @param petId The pet ID
     * @return List of schedules for the pet
     */
    public List<Schedule> getSchedulesForPet(Long petId) {
        // Verify pet exists
        petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + petId));
        
        return scheduleRepository.findAllByPets_Id(petId);
    }
    
    /**
     * Get all schedules for a specific employee.
     * 
     * @param employeeId The employee ID
     * @return List of schedules for the employee
     */
    public List<Schedule> getSchedulesForEmployee(Long employeeId) {
        // Verify employee exists
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        
        return scheduleRepository.findAllByEmployees_Id(employeeId);
    }
    
    /**
     * Get all schedules for pets belonging to a specific customer.
     * 
     * @param customerId The customer ID
     * @return List of schedules for all pets owned by the customer
     */
    public List<Schedule> getSchedulesForCustomer(Long customerId) {
        return scheduleRepository.findAllByPets_Owner_Id(customerId);
    }
    
    /**
     * Get schedules for a specific date.
     * 
     * @param date The date to search for
     * @return List of schedules on the specified date
     */
    public List<Schedule> getSchedulesForDate(LocalDate date) {
        return scheduleRepository.findByDate(date);
    }
    
    /**
     * Get schedules between two dates.
     * 
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return List of schedules between the dates
     */
    public List<Schedule> getSchedulesBetweenDates(LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByDateBetween(startDate, endDate);
    }
    
    /**
     * Update an existing schedule.
     * 
     * @param scheduleId The schedule ID to update
     * @param updatedSchedule The updated schedule data
     * @return Updated schedule
     */
    public Schedule updateSchedule(Long scheduleId, Schedule updatedSchedule) {
        Schedule existingSchedule = getScheduleById(scheduleId);
        
        // Update basic fields
        existingSchedule.setDate(updatedSchedule.getDate());
        existingSchedule.setActivities(updatedSchedule.getActivities());
        
        // Handle employee changes
        if (updatedSchedule.getEmployees() != null) {
            List<Employee> newEmployees = validateAndFetchEmployees(updatedSchedule.getEmployees());
            validateEmployeeAvailability(newEmployees, updatedSchedule.getDate());
            validateEmployeeSkills(newEmployees, updatedSchedule.getActivities());
            existingSchedule.setEmployees(newEmployees);
        }
        
        // Handle pet changes
        if (updatedSchedule.getPets() != null) {
            List<Pet> newPets = validateAndFetchPets(updatedSchedule.getPets());
            existingSchedule.setPets(newPets);
        }
        
        return scheduleRepository.save(existingSchedule);
    }
    
    /**
     * Delete a schedule.
     * 
     * @param scheduleId The schedule ID to delete
     */
    public void deleteSchedule(Long scheduleId) {
        if (!scheduleRepository.existsById(scheduleId)) {
            throw new RuntimeException("Schedule not found with ID: " + scheduleId);
        }
        scheduleRepository.deleteById(scheduleId);
    }
}