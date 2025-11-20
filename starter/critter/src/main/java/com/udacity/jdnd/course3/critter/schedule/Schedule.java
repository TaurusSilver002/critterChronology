package com.udacity.jdnd.course3.critter.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

/**
 * Schedule entity representing appointments/bookings in the critter system.
 * Simplified version with only required JPA annotations.
 */
@Entity // JPA annotation: Marks this class as a database entity
public class Schedule {
    
    @Id // JPA annotation: Primary key identifier
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;
    
    // Simple field for appointment date
    private LocalDate date;
    
    // @ManyToMany: Schedule can have multiple employees, employees can have multiple schedules
    // Creates schedule_employees join table with schedule_id and employees_id
    @ManyToMany
    private List<Employee> employees = new ArrayList<>();
    
    // @ManyToMany: Schedule can have multiple pets, pets can have multiple schedules
    // Creates schedule_pets join table with schedule_id and pets_id
    @ManyToMany
    private List<Pet> pets = new ArrayList<>();
    
    // @ElementCollection: Stores activity types in separate table
    // Creates schedule_activities table with schedule_id and activities columns
    @ElementCollection
    private Set<EmployeeSkill> activities = new HashSet<>();
    
    // Default constructor required by JPA
    public Schedule() {}
    
    // Constructor for creating new schedules
    public Schedule(LocalDate date, List<Employee> employees, List<Pet> pets, Set<EmployeeSkill> activities) {
        this.date = date;
        this.employees = employees != null ? employees : new ArrayList<>();
        this.pets = pets != null ? pets : new ArrayList<>();
        this.activities = activities != null ? activities : new HashSet<>();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public List<Employee> getEmployees() {
        return employees;
    }
    
    public void setEmployees(List<Employee> employees) {
        this.employees = employees != null ? employees : new ArrayList<>();
    }
    
    public List<Pet> getPets() {
        return pets;
    }
    
    public void setPets(List<Pet> pets) {
        this.pets = pets != null ? pets : new ArrayList<>();
    }
    
    public Set<EmployeeSkill> getActivities() {
        return activities;
    }
    
    public void setActivities(Set<EmployeeSkill> activities) {
        this.activities = activities != null ? activities : new HashSet<>();
    }
    
    /**
     * Utility method to add an employee to this schedule
     * Maintains bidirectional @ManyToMany relationship
     */
    public void addEmployee(Employee employee) {
        if (!this.employees.contains(employee)) {
            this.employees.add(employee);           // Add to schedule's employee list
            employee.getSchedules().add(this);      // Add schedule to employee's schedule list
        }
    }
    
    /**
     * Utility method to remove an employee from this schedule
     * Cleans up both sides of the @ManyToMany relationship
     */
    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);            // Remove from schedule's employee list
        employee.getSchedules().remove(this);       // Remove schedule from employee's list
    }
    
    /**
     * Utility method to add a pet to this schedule
     * Maintains bidirectional @ManyToMany relationship
     */
    public void addPet(Pet pet) {
        if (!this.pets.contains(pet)) {
            this.pets.add(pet);                     // Add to schedule's pet list
            pet.getSchedules().add(this);           // Add schedule to pet's schedule list
        }
    }
    
    /**
     * Utility method to remove a pet from this schedule
     * Cleans up both sides of the @ManyToMany relationship
     */
    public void removePet(Pet pet) {
        this.pets.remove(pet);                      // Remove from schedule's pet list
        pet.getSchedules().remove(this);            // Remove schedule from pet's list
    }
    
    /**
     * Utility method to add an activity/service to this schedule
     */
    public void addActivity(EmployeeSkill activity) {
        this.activities.add(activity);
    }
}