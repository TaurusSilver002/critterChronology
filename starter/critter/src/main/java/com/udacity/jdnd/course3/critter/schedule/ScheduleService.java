package com.udacity.jdnd.course3.critter.schedule;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;

// Service layer for schedule management and coordination
@Service
public class ScheduleService {
    
    // Multiple repositories needed for schedule operations
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PetRepository petRepository;
    
    // Creates a new schedule by linking employees and pets
    public Schedule createSchedule(Schedule schedule) {
        // Fetch full entities for employees and pets (JPA will validate IDs exist)
        List<Employee> employees = employeeRepository.findAllById(
            schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
        List<Pet> pets = petRepository.findAllById(
            schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        return scheduleRepository.save(schedule);
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