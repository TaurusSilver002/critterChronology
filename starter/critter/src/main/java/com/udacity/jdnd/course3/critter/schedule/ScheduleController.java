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

// REST Controller for schedule-related operations
@RestController
@RequestMapping("/schedule") // Base URL path for all schedule endpoints
public class ScheduleController {
    
    // Service dependencies for business logic and entity fetching
    private final ScheduleService scheduleService;
    private final EmployeeService employeeService;
    private final PetService petService;
    
    // Constructor injection of all required services
    @Autowired
    public ScheduleController(ScheduleService scheduleService,
                            EmployeeService employeeService,
                            PetService petService) {
        this.scheduleService = scheduleService;
        this.employeeService = employeeService;
        this.petService = petService;
    }

    // POST endpoint: Creates a new schedule
    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertDTOToEntity(scheduleDTO);
        Schedule savedSchedule = scheduleService.createSchedule(schedule);
        return convertEntityToDTO(savedSchedule);
    }

    // GET endpoint: Retrieves all schedules
    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return schedules.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    // GET endpoint: Finds all schedules for a specific pet
    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = scheduleService.getSchedulesForPet(petId);
        return schedules.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    // GET endpoint: Finds all schedules for a specific employee
    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.getSchedulesForEmployee(employeeId);
        return schedules.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    // GET endpoint: Finds all schedules for pets owned by a specific customer
    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules = scheduleService.getSchedulesForCustomer(customerId);
        return schedules.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }
    
    // Helper method: Converts ScheduleDTO (API layer) to Schedule entity (database layer)
    private Schedule convertDTOToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        schedule.setId(scheduleDTO.getId() != 0 ? scheduleDTO.getId() : null);
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());
        
        // Convert employee IDs to full Employee entities
        if (scheduleDTO.getEmployeeIds() != null) {
            List<Employee> employees = scheduleDTO.getEmployeeIds().stream()
                    .map(employeeService::getEmployeeById)
                    .collect(Collectors.toList());
            schedule.setEmployees(employees);
        }
        
        // Convert pet IDs to full Pet entities
        if (scheduleDTO.getPetIds() != null) {
            List<Pet> pets = scheduleDTO.getPetIds().stream()
                    .map(petService::getPetById)
                    .collect(Collectors.toList());
            schedule.setPets(pets);
        }
        
        return schedule;
    }
    
    // Helper method: Converts Schedule entity (database layer) to ScheduleDTO (API layer)
    private ScheduleDTO convertEntityToDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());
        
        // Extract employee IDs from Employee entities
        if (schedule.getEmployees() != null) {
            List<Long> employeeIds = schedule.getEmployees().stream()
                    .map(Employee::getId)
                    .collect(Collectors.toList());
            scheduleDTO.setEmployeeIds(employeeIds);
        }
        
        // Extract pet IDs from Pet entities
        if (schedule.getPets() != null) {
            List<Long> petIds = schedule.getPets().stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList());
            scheduleDTO.setPetIds(petIds);
        }
        
        return scheduleDTO;
    }
}
