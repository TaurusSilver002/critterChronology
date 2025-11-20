package com.udacity.jdnd.course3.critter.schedule;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;

// Service layer for schedule management and coordination
@Service
@Transactional
public class ScheduleService {
    
    // Multiple repositories needed for schedule operations
    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final PetRepository petRepository;
    
    // Constructor injection for all required repositories
    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, 
                          EmployeeRepository employeeRepository,
                          PetRepository petRepository) {
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
    }
    
    // Creates a new schedule by linking validated employees and pets
    public Schedule createSchedule(Schedule schedule) {
        List<Employee> employees = validateAndFetchEmployees(schedule.getEmployees());
        List<Pet> pets = validateAndFetchPets(schedule.getPets());
        
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        
        return scheduleRepository.save(schedule);
    }
    
    // Helper method: Validates and fetches full employee entities from database
    private List<Employee> validateAndFetchEmployees(List<Employee> employees) {
        return employees.stream()
                .map(employee -> employeeRepository.findById(employee.getId())
                        .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employee.getId())))
                .collect(Collectors.toList());
    }
    
    // Helper method: Validates and fetches full pet entities from database
    private List<Pet> validateAndFetchPets(List<Pet> pets) {
        return pets.stream()
                .map(pet -> petRepository.findById(pet.getId())
                        .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + pet.getId())))
                .collect(Collectors.toList());
    }
    
    // Retrieves all schedules from the database
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
    
    // Finds all schedules that include a specific pet
    public List<Schedule> getSchedulesForPet(Long petId) {
        return scheduleRepository.findAllByPets_Id(petId);
    }
    
    // Finds all schedules that include a specific employee
    public List<Schedule> getSchedulesForEmployee(Long employeeId) {
        return scheduleRepository.findAllByEmployees_Id(employeeId);
    }
    
    // Finds all schedules for pets owned by a specific customer
    public List<Schedule> getSchedulesForCustomer(Long customerId) {
        return scheduleRepository.findAllByPets_Owner_Id(customerId);
    }
}