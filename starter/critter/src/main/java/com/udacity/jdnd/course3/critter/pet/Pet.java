package com.udacity.jdnd.course3.critter.pet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Pet entity representing animals in the critter system.
 * 
 * Annotations explained:
 * @Entity - Marks this class as a JPA entity for database persistence
 * @Table - Specifies table name and constraints
 * @Enumerated - Maps enum fields to database (ORDINAL = numbers, STRING = text)
 * @ManyToOne - Defines many-to-one relationship (many pets can belong to one customer)
 *   - fetch: Loading strategy for related entity
 *   - optional: Whether the relationship can be null
 * @JoinColumn - Specifies foreign key column details
 * @ManyToMany - Defines many-to-many relationship (pets can be in multiple schedules)
 */
@Entity
@Table(name = "pets")
public class Pet {
    
    /**
     * Primary key with auto-generation
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Pet type stored as string in database
     * ORDINAL would store as numbers (0,1,2...) but STRING is more readable
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetType type;
    
    /**
     * Pet name with database constraints
     */
    @Column(nullable = false, length = 100)
    private String name;
    
    /**
     * Birth date of the pet
     */
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    /**
     * Optional notes about the pet
     */
    @Column(length = 500)
    private String notes;
    
    /**
     * Many-to-One relationship with Customer entity
     * fetch = LAZY: Owner is loaded only when accessed
     * optional = false: Every pet must have an owner (NOT NULL constraint)(java side)
     * @JoinColumn specifies the foreign key column name
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Customer owner;
    
    /**
     * Many-to-Many relationship with Schedule entity
     * mappedBy = "pets": Schedule entity owns the relationship through its "pets" field
     * This creates a join table managed by the Schedule entity
     */
    @ManyToMany(mappedBy = "pets")
    private List<Schedule> schedules = new ArrayList<>();
    
    // Default constructor required by JPA
    public Pet() {}
    
    // Constructor for creating new pets
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