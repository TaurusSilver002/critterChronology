package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow();
    }
    
    public Employee setEmployeeAvailability(Long employeeId, Set<DayOfWeek> daysAvailable) {
        Employee employee = getEmployeeById(employeeId);
        employee.setDaysAvailable(daysAvailable);
        return employeeRepository.save(employee);
    }
    
    public List<Employee> findEmployeesForService(LocalDate date, Set<EmployeeSkill> skills) {
        return employeeRepository.findAvailableEmployeesWithSkills(date.getDayOfWeek(), skills)
                .stream()
                .filter(employee -> employee.getSkills().containsAll(skills))
                .collect(Collectors.toList());
    }
}