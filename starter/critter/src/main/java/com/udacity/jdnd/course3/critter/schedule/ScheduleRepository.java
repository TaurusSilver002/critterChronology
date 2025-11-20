package com.udacity.jdnd.course3.critter.schedule;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    List<Schedule> findAllByPets_Id(Long petId);
    
    List<Schedule> findAllByEmployees_Id(Long employeeId);
    
    List<Schedule> findAllByPets_Owner_Id(Long customerId);
}