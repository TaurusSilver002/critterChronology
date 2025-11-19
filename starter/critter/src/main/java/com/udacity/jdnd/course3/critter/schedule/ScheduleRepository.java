package com.udacity.jdnd.course3.critter.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * ScheduleRepository interface for database operations on Schedule entities.
 * 
 * Complex relationship queries explained:
 * - Many-to-many relationships require JOIN operations
 * - Navigation through entity relationships (schedule -> pets -> owner)
 * - Date-based filtering for schedule queries
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    /**
     * Find all schedules that include a specific pet.
     * 
     * Method naming convention:
     * - "findAllBy" indicates finding multiple results
     * - "Pets_Id" navigates: schedules -> pets collection -> pet's id field
     * - Underscore (_) indicates property path navigation in Spring Data
     * 
     * This translates to: SELECT s FROM Schedule s JOIN s.pets p WHERE p.id = ?
     * 
     * @param petId The ID of the pet to find schedules for
     * @return List of schedules that include the specified pet
     */
    List<Schedule> findAllByPets_Id(Long petId);
    
    /**
     * Find all schedules that include a specific employee.
     * 
     * Similar to pets, this navigates the many-to-many employees relationship.
     * 
     * @param employeeId The ID of the employee to find schedules for
     * @return List of schedules that include the specified employee
     */
    List<Schedule> findAllByEmployees_Id(Long employeeId);
    
    /**
     * Find all schedules for pets belonging to a specific customer.
     * 
     * Property path navigation: schedules -> pets -> owner -> id
     * - "Pets" refers to the pets collection in Schedule
     * - "Owner" refers to the owner field in Pet entity
     * - "Id" refers to the id field in Customer entity
     * 
     * This is equivalent to a multi-join query across three tables:
     * Schedule -> schedule_pets -> Pet -> Customer
     * 
     * @param customerId The ID of the customer (pet owner)
     * @return List of schedules for all pets owned by the customer
     */
    List<Schedule> findAllByPets_Owner_Id(Long customerId);
    
    /**
     * Find schedules by date using method naming convention.
     * 
     * @param date The date to search for schedules
     * @return List of schedules on the specified date
     */
    List<Schedule> findByDate(LocalDate date);
    
    /**
     * Find schedules between two dates (inclusive).
     * 
     * Method naming: "Between" creates a range query with SQL BETWEEN
     * 
     * @param startDate The start date (inclusive)
     * @param endDate The end date (inclusive)
     * @return List of schedules between the specified dates
     */
    List<Schedule> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Custom query to find schedules for a specific employee on a specific date.
     * Combines date filtering with employee relationship.
     * 
     * JPQL breakdown:
     * - JOIN s.employees e: Creates join between schedules and employees
     * - WHERE e.id = :employeeId: Filter by specific employee
     * - AND s.date = :date: Also filter by specific date
     * 
     * @param employeeId The employee ID to search for
     * @param date The date to search for
     * @return List of schedules for the employee on the specified date
     */
    @Query("SELECT s FROM Schedule s JOIN s.employees e " +
           "WHERE e.id = :employeeId AND s.date = :date")
    List<Schedule> findSchedulesForEmployeeOnDate(@Param("employeeId") Long employeeId,
                                                 @Param("date") LocalDate date);
    
    /**
     * Custom query to find schedules for a specific pet on a specific date.
     * 
     * @param petId The pet ID to search for
     * @param date The date to search for
     * @return List of schedules for the pet on the specified date
     */
    @Query("SELECT s FROM Schedule s JOIN s.pets p " +
           "WHERE p.id = :petId AND s.date = :date")
    List<Schedule> findSchedulesForPetOnDate(@Param("petId") Long petId,
                                           @Param("date") LocalDate date);
}