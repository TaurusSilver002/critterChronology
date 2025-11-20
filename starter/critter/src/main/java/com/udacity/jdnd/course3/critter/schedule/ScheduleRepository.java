package com.udacity.jdnd.course3.critter.schedule;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository interface for Schedule entity with relationship-based queries
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    // Finds all schedules that include a specific pet (using property path)
    List<Schedule> findAllByPets_Id(Long petId);
    
    // Finds all schedules that include a specific employee (using property path)
    List<Schedule> findAllByEmployees_Id(Long employeeId);
    
    // Finds all schedules for pets owned by a specific customer (nested property path)
    List<Schedule> findAllByPets_Owner_Id(Long customerId);
}