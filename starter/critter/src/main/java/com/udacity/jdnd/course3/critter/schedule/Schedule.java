package com.udacity.jdnd.course3.critter.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * Schedule entity representing appointments/bookings in the critter system.
 * 
 * Annotations explained:
 * @JoinTable - Specifies the join table for many-to-many relationships
 *   - name: Name of the join table
 *   - joinColumns: Foreign key columns referencing this entity
 *   - inverseJoinColumns: Foreign key columns referencing the other entity
 * @ManyToMany - Many schedules can have many employees/pets
 * @ElementCollection - For storing activities (EmployeeSkill enums)
 */
@Entity
@Table(name = "schedules")
public class Schedule {
    
    /**
     * Primary key with auto-generation
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Date of the scheduled appointment
     */
    @Column(nullable = false)
    private LocalDate date;
    
    /**
     * Many-to-Many relationship with Employee entity
     * Creates a join table "schedule_employees" to link schedules and employees
     * @JoinTable specifies:
     *   - name: Join table name
     *   - joinColumns: FK column pointing to this schedule
     *   - inverseJoinColumns: FK column pointing to employee
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "schedule_employees",
        joinColumns = @JoinColumn(name = "schedule_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employees = new ArrayList<>();
    
    /**
     * Many-to-Many relationship with Pet entity
     * Creates a join table "schedule_pets" to link schedules and pets
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "schedule_pets",
        joinColumns = @JoinColumn(name = "schedule_id"),
        inverseJoinColumns = @JoinColumn(name = "pet_id")
    )
    private List<Pet> pets = new ArrayList<>();
    
    /**
     * Activities/services to be performed during this schedule
     * @ElementCollection: Creates a separate table "schedule_activities"
     * @CollectionTable: Specifies join table details
     * @Enumerated(STRING): Stores enum values as strings
     */
    @ElementCollection(targetClass = EmployeeSkill.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "schedule_activities", joinColumns = @JoinColumn(name = "schedule_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "activity")
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
     */
    public void addEmployee(Employee employee) {
        if (!this.employees.contains(employee)) {
            this.employees.add(employee);
            employee.getSchedules().add(this);
        }
    }
    
    /**
     * Utility method to remove an employee from this schedule
     */
    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getSchedules().remove(this);
    }
    
    /**
     * Utility method to add a pet to this schedule
     */
    public void addPet(Pet pet) {
        if (!this.pets.contains(pet)) {
            this.pets.add(pet);
            pet.getSchedules().add(this);
        }
    }
    
    /**
     * Utility method to remove a pet from this schedule
     */
    public void removePet(Pet pet) {
        this.pets.remove(pet);
        pet.getSchedules().remove(this);
    }
    
    /**
     * Utility method to add an activity
     */
    public void addActivity(EmployeeSkill activity) {
        this.activities.add(activity);
    }
}