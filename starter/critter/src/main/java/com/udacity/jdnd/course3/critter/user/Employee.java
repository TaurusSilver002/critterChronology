package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.udacity.jdnd.course3.critter.schedule.Schedule;

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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * Employee entity representing staff members in the critter system.
 * 
 * Annotations explained:
 * @ElementCollection - Maps collections of basic types or embeddable objects
 *   - Used for skills and daysAvailable which are enums, not entities
 *   - Creates separate tables to store these collections
 * @CollectionTable - Specifies the table name and join column for element collections
 * @Enumerated - Specifies how enums are stored in database
 */
@Entity
@Table(name = "employees")
public class Employee {
    
    /**
     * Primary key with auto-generation
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Employee name with database constraints
     */
    @Column(nullable = false, length = 100)
    private String name;
    
    /**
     * Employee skills collection
     * @ElementCollection: Creates a separate table for employee skills
     * @CollectionTable: Specifies table name "employee_skills" and foreign key "employee_id"
     * @Enumerated(STRING): Stores enum values as strings in database
     * @Column: Specifies the column name in the collection table
     */
    @ElementCollection(targetClass = EmployeeSkill.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_skills", joinColumns = @JoinColumn(name = "employee_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "skill")
    private Set<EmployeeSkill> skills = new HashSet<>();
    
    /**
     * Days when employee is available
     * Similar to skills but for DayOfWeek enum
     * Creates "employee_availability" table
     */
    @ElementCollection(targetClass = DayOfWeek.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_availability", joinColumns = @JoinColumn(name = "employee_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day_available")
    private Set<DayOfWeek> daysAvailable = new HashSet<>();
    
    /**
     * Many-to-Many relationship with Schedule entity
     * mappedBy = "employees": Schedule entity owns the relationship
     */
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
     * Utility method to add a skill
     */
    public void addSkill(EmployeeSkill skill) {
        this.skills.add(skill);
    }
    
    /**
     * Utility method to add availability day
     */
    public void addAvailableDay(DayOfWeek day) {
        if (this.daysAvailable == null) {
            this.daysAvailable = new HashSet<>();
        }
        this.daysAvailable.add(day);
    }
    
    /**
     * Utility method to check if employee is available on a specific day
     */
    public boolean isAvailableOn(DayOfWeek day) {
        return this.daysAvailable != null && this.daysAvailable.contains(day);
    }
    
    /**
     * Utility method to check if employee has a specific skill
     */
    public boolean hasSkill(EmployeeSkill skill) {
        return this.skills.contains(skill);
    }
}