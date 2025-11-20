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

@Entity
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ElementCollection
    private Set<EmployeeSkill> skills = new HashSet<>();
    
    @ElementCollection
    private Set<DayOfWeek> daysAvailable = new HashSet<>();
    
    @ManyToMany(mappedBy = "employees")
    private List<Schedule> schedules = new ArrayList<>();
    
    public Employee() {}
    
    public Employee(String name, Set<EmployeeSkill> skills, Set<DayOfWeek> daysAvailable) {
        this.name = name;
        this.skills = skills != null ? skills : new HashSet<>();
        this.daysAvailable = daysAvailable;
    }
    
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
        this.daysAvailable = daysAvailable;
    }
    
    public List<Schedule> getSchedules() {
        return schedules;
    }
    
    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
    
    public void addSkill(EmployeeSkill skill) {
        this.skills.add(skill);
    }
    
    public void addAvailableDay(DayOfWeek day) {
        if (this.daysAvailable == null) {
            this.daysAvailable = new HashSet<>();
        }
        this.daysAvailable.add(day);
    }
    
    public boolean isAvailableOn(DayOfWeek day) {
        return this.daysAvailable != null && this.daysAvailable.contains(day);
    }
    
    public boolean hasSkill(EmployeeSkill skill) {
        return this.skills.contains(skill);
    }
}