package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * EmployeeService handles business logic for Employee operations.
 * 
 * Key responsibilities:
 * - Managing employee CRUD operations
 * - Finding employees based on availability and skills
 * - Handling employee scheduling logic
 */
@Service
@Transactional
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    /**
     * Save a new employee.
     * 
     * @param employee The employee to save
     * @return Saved employee with generated ID
     */
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    /**
     * Find an employee by ID.
     * 
     * @param employeeId The ID of the employee
     * @return Optional containing employee if found
     */
    public Optional<Employee> findEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }
    
    /**
     * Get an employee by ID, throwing exception if not found.
     * 
     * @param employeeId The ID of the employee
     * @return The employee entity
     * @throws RuntimeException if employee not found
     */
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
    }
    
    /**
     * Get all employees.
     * 
     * @return List of all employees
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    /**
     * Set an employee's availability for specific days.
     * 
     * Business logic:
     * 1. Find the employee
     * 2. Update their availability days (preserve null if empty set provided)
     * 3. Save the changes
     * 
     * @param employeeId The ID of the employee
     * @param daysAvailable Set of days when employee is available
     * @return Updated employee
     */
    public Employee setEmployeeAvailability(Long employeeId, Set<DayOfWeek> daysAvailable) {
        Employee employee = getEmployeeById(employeeId);
        // Preserve null values for test compatibility - don't auto-initialize empty sets
        employee.setDaysAvailable(daysAvailable);
        return employeeRepository.save(employee);
    }
    
    /**
     * Find employees available for service on a specific date with required skills.
     * 
     * This is a core business method that combines multiple criteria:
     * 1. Employee must be available on the day of the week
     * 2. Employee must have at least one of the required skills
     * 3. Returns only employees who meet both criteria
     * 
     * Business logic breakdown:
     * 1. Extract day of week from the date
     * 2. Find employees available on that day AND with required skills
     * 3. Filter to ensure they have ALL required skills (if needed)
     * 
     * @param date The date of service
     * @param skills Set of skills required for the service
     * @return List of qualified employees
     */
    public List<Employee> findEmployeesForService(LocalDate date, Set<EmployeeSkill> skills) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        
        // Get employees available on this day with any of the required skills
        List<Employee> candidateEmployees = employeeRepository
                .findAvailableEmployeesWithSkills(dayOfWeek, skills);
        
        // Filter to ensure employees have ALL required skills
        // (The repository method finds employees with ANY skill, but we need ALL skills)
        return candidateEmployees.stream()
                .filter(employee -> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());
    }
    
    /**
     * Find employees available on a specific day (regardless of skills).
     * 
     * @param date The date to check availability for
     * @return List of employees available on that day
     */
    public List<Employee> findEmployeesAvailableOnDate(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return employeeRepository.findByDaysAvailableContains(dayOfWeek);
    }
    
    /**
     * Find employees with a specific skill (regardless of availability).
     * 
     * @param skill The skill to search for
     * @return List of employees with the specified skill
     */
    public List<Employee> findEmployeesWithSkill(EmployeeSkill skill) {
        return employeeRepository.findBySkillsContains(skill);
    }
    
    /**
     * Find employees with ALL specified skills.
     * 
     * @param skills Set of skills - employee must have ALL of them
     * @return List of employees with all specified skills
     */
    public List<Employee> findEmployeesWithAllSkills(Set<EmployeeSkill> skills) {
        return employeeRepository.findEmployeesWithAllSkills(skills, skills.size());
    }
    
    /**
     * Update an employee's information.
     * 
     * @param employeeId The ID of employee to update
     * @param updatedEmployee The updated employee data
     * @return Updated employee
     */
    public Employee updateEmployee(Long employeeId, Employee updatedEmployee) {
        Employee existingEmployee = getEmployeeById(employeeId);
        
        // Update fields
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setSkills(updatedEmployee.getSkills());
        existingEmployee.setDaysAvailable(updatedEmployee.getDaysAvailable());
        
        return employeeRepository.save(existingEmployee);
    }
    
    /**
     * Delete an employee.
     * 
     * Note: This might affect existing schedules, so consider business rules
     * about whether to allow deletion of employees with future schedules.
     * 
     * @param employeeId The ID of employee to delete
     */
    public void deleteEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found with ID: " + employeeId);
        }
        employeeRepository.deleteById(employeeId);
    }
    
    /**
     * Add a skill to an employee.
     * 
     * @param employeeId The employee ID
     * @param skill The skill to add
     * @return Updated employee
     */
    public Employee addSkillToEmployee(Long employeeId, EmployeeSkill skill) {
        Employee employee = getEmployeeById(employeeId);
        employee.addSkill(skill);
        return employeeRepository.save(employee);
    }
    
    /**
     * Add an available day to an employee.
     * 
     * @param employeeId The employee ID
     * @param day The day to add
     * @return Updated employee
     */
    public Employee addAvailableDayToEmployee(Long employeeId, DayOfWeek day) {
        Employee employee = getEmployeeById(employeeId);
        employee.addAvailableDay(day);
        return employeeRepository.save(employee);
    }
}