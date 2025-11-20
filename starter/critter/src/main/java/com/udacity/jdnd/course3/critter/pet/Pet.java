package com.udacity.jdnd.course3.critter.pet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

/**
 * Pet entity representing animals in the critter system.
 * Simplified version with only required JPA annotations.
 */
@Entity // JPA annotation: Marks this class as a database entity
public class Pet {
    
    @Id // JPA annotation: Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;
    
    // Simple fields mapped directly to database columns
    private PetType type;
    private String name;
    private LocalDate birthDate;
    private String notes;
    
    @ManyToOne // JPA relationship: Many pets can belong to one customer
    private Customer owner;
    
    @ManyToMany(mappedBy = "pets") // JPA relationship: Pet can be in multiple schedules
    private List<Schedule> schedules = new ArrayList<>();
    
    // Default constructor required by JPA for entity instantiation
    public Pet() {}
    
    // Constructor for creating new pets with initial data
    public Pet(PetType type, String name, LocalDate birthDate, String notes) {
        this.type = type;
        this.name = name;
        this.birthDate = birthDate;
        this.notes = notes;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public PetType getType() {
        return type;
    }
    
    public void setType(PetType type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Customer getOwner() {
        return owner;
    }
    
    public void setOwner(Customer owner) {
        this.owner = owner;
    }
    
    public List<Schedule> getSchedules() {
        return schedules;
    }
    
    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}