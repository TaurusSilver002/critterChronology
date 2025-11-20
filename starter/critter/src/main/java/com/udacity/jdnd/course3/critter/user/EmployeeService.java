package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Service layer for employee business logic and scheduling
@Service
public class EmployeeService {
    
    // Repository for employee database operations
    @Autowired
    private EmployeeRepository employeeRepository;
    
    // Saves a new employee to the database
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    // Retrieves an employee by ID, throws exception if not found
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow();
    }
    
    // Updates an employee's availability schedule
    public Employee setEmployeeAvailability(Long employeeId, Set<DayOfWeek> daysAvailable) {
        Employee employee = getEmployeeById(employeeId);
        employee.setDaysAvailable(daysAvailable);
        return employeeRepository.save(employee);
    }
    
    // Finds employees available for service on a specific date with required skills
    public List<Employee> findEmployeesForService(LocalDate date, Set<EmployeeSkill> skills) {
        // Find employees available on this day with required skills, then filter for ALL skills
        return employeeRepository.findAvailableEmployeesWithSkills(date.getDayOfWeek(), skills)
                .stream()
                .filter(employee -> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());
    }
}