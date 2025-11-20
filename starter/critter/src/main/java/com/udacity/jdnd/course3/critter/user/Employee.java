package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.udacity.jdnd.course3.critter.schedule.Schedule;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

/**
 * Employee entity representing staff members in the critter system.
 * Simplified version with only required JPA annotations.
 */
@Entity // JPA annotation: Marks this class as a database entity
public class Employee {
    
    @Id // JPA annotation: Primary key identifier
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;
    
    // Simple field mapped to database column
    private String name;
    
    // @ElementCollection: Maps enum values to a separate table
    // Creates employee_skills table with employee_id and skills columns
    @ElementCollection
    private Set<EmployeeSkill> skills = new HashSet<>();
    
    // @ElementCollection: Maps DayOfWeek enum to separate table
    // Creates employee_days_available table
    @ElementCollection
    private Set<DayOfWeek> daysAvailable = new HashSet<>();
    
    // JPA relationship: Employee can be assigned to multiple schedules
    // mappedBy="employees" indicates Schedule entity owns the relationship
    @ManyToMany(mappedBy = "employees")
    private List<Schedule> schedules = new ArrayList<>();
    
    // Default constructor required by JPA
    public Employee() {}
    
    // Constructor for creating new employees
    public Employee(String name, Set<EmployeeSkill> skills, Set<DayOfWeek> daysAvailable) {
        this.name = name;
        this.skills = skills != null ? skills : new HashSet<>();
        this.daysAvailable = daysAvailable; // Don't initialize to empty set - leave as provided (could be null)
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Set<EmployeeSkill> getSkills() {
        return skills;
    }
    
    public void setSkills(Set<EmployeeSkill> skills) {
        this.skills = skills != null ? skills : new HashSet<>();
    }
    
    public Set<DayOfWeek> getDaysAvailable() {
        return daysAvailable;
    }
    
    public void setDaysAvailable(Set<DayOfWeek> daysAvailable) {
        this.daysAvailable = daysAvailable; // Don't auto-initialize - preserve null values
    }
    
    public List<Schedule> getSchedules() {
        return schedules;
    }
    
    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
    
    /**
     * Utility method to add a skill to employee's skill set
     */
    public void addSkill(EmployeeSkill skill) {
        this.skills.add(skill);
    }
    
    /**
     * Utility method to add an available day to employee's schedule
     * Handles null safety for daysAvailable collection
     */
    public void addAvailableDay(DayOfWeek day) {
        if (this.daysAvailable == null) {
            this.daysAvailable = new HashSet<>();
        }
        this.daysAvailable.add(day);
    }
    
    /**
     * Business logic method: Checks if employee is available on a specific day
     * Used by scheduling service to match employees to appointments
     */
    public boolean isAvailableOn(DayOfWeek day) {
        return this.daysAvailable != null && this.daysAvailable.contains(day);
    }
    
    /**
     * Business logic method: Checks if employee has a specific skill
     * Used by scheduling service to match employees to required services
     */
    public boolean hasSkill(EmployeeSkill skill) {
        return this.skills.contains(skill);
    }
}