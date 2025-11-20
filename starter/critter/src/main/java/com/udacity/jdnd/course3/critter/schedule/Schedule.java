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

@Entity
public class Schedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate date;
    
    @ManyToMany
    private List<Employee> employees = new ArrayList<>();
    
    @ManyToMany
    private List<Pet> pets = new ArrayList<>();
    
    @ElementCollection
    private Set<EmployeeSkill> activities = new HashSet<>();
    
    public Schedule() {}
    
    public Schedule(LocalDate date, List<Employee> employees, List<Pet> pets, Set<EmployeeSkill> activities) {
        this.date = date;
        this.employees = employees != null ? employees : new ArrayList<>();
        this.pets = pets != null ? pets : new ArrayList<>();
        this.activities = activities != null ? activities : new HashSet<>();
    }
    
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
    
    public void addEmployee(Employee employee) {
        if (!this.employees.contains(employee)) {
            this.employees.add(employee);
            employee.getSchedules().add(this);
        }
    }
    
    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getSchedules().remove(this);
    }
    
    public void addPet(Pet pet) {
        if (!this.pets.contains(pet)) {
            this.pets.add(pet);
            pet.getSchedules().add(this);
        }
    }
    
    public void removePet(Pet pet) {
        this.pets.remove(pet);
        pet.getSchedules().remove(this);
    }
    
    public void addActivity(EmployeeSkill activity) {
        this.activities.add(activity);
    }
}