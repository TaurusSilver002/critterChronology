package com.udacity.jdnd.course3.critter.schedule;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;

@Service
public class ScheduleService {
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PetRepository petRepository;
    
    public Schedule createSchedule(Schedule schedule) {
        List<Employee> employees = employeeRepository.findAllById(
            schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList()));
        List<Pet> pets = petRepository.findAllById(
            schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList()));
        
        schedule.setEmployees(employees);
        schedule.setPets(pets);
        return scheduleRepository.save(schedule);
    }
    
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
    
    public List<Schedule> getSchedulesForPet(Long petId) {
        return scheduleRepository.findAllByPets_Id(petId);
    }
    
    public List<Schedule> getSchedulesForEmployee(Long employeeId) {
        return scheduleRepository.findAllByEmployees_Id(employeeId);
    }
    
    public List<Schedule> getSchedulesForCustomer(Long customerId) {
        return scheduleRepository.findAllByPets_Owner_Id(customerId);
    }
}