package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * EmployeeRepository interface for database operations on Employee entities.
 * 
 * Advanced query methods explained:
 * - JOIN operations in JPQL to query related data
 * - Collection operations (member of, IN clauses)
 * - Set operations for matching skills and availability
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    /**
     * Find employees available on a specific day.
     * 
     * Method naming convention breakdown:
     * - "findBy" indicates query method
     * - "DaysAvailable" refers to the daysAvailable Set field
     * - "Contains" checks if the Set contains the specified value
     * 
     * Generated query checks if the day exists in employee's daysAvailable set.
     * 
     * @param day The day of week to check availability for
     * @return List of employees available on the specified day
     */
    List<Employee> findByDaysAvailableContains(DayOfWeek day);
    
    /**
     * Find employees who have a specific skill.
     * Similar to daysAvailable, this checks if the skill exists in the skills Set.
     * 
     * @param skill The skill to search for
     * @return List of employees who have the specified skill
     */
    List<Employee> findBySkillsContains(EmployeeSkill skill);
    
    /**
     * Find employees available on a specific day AND having specific skills.
     * 
     * JPQL breakdown:
     * - "SELECT DISTINCT e" - select unique employees (avoid duplicates from joins)
     * - "FROM Employee e" - from Employee table
     * - "JOIN e.skills s" - join with skills collection
     * - "WHERE :day MEMBER OF e.daysAvailable" - check if day is in daysAvailable set
     * - "AND s IN :skills" - check if any employee skill is in the required skills set
     * 
     * MEMBER OF operator: Checks if a value exists in a collection
     * IN operator: Checks if a value exists in a list/set of values
     * 
     * @param day The day employee must be available
     * @param skills Set of required skills (employee must have at least one)
     * @return List of employees available on the day with required skills
     */
    @Query("SELECT DISTINCT e FROM Employee e JOIN e.skills s " +
           "WHERE :day MEMBER OF e.daysAvailable AND s IN :skills")
    List<Employee> findAvailableEmployeesWithSkills(@Param("day") DayOfWeek day, 
                                                   @Param("skills") Set<EmployeeSkill> skills);
    
    /**
     * Find employees who have ALL specified skills (not just one).
     * 
     * This complex query ensures employees have every required skill:
     * - Counts how many required skills each employee has
     * - Groups by employee
     * - Filters for employees whose skill count matches required skills count
     * 
     * @param skills Set of ALL required skills
     * @return List of employees who have all the specified skills
     */
    @Query("SELECT e FROM Employee e JOIN e.skills s " +
           "WHERE s IN :skills " +
           "GROUP BY e " +
           "HAVING COUNT(DISTINCT s) = :skillCount")
    List<Employee> findEmployeesWithAllSkills(@Param("skills") Set<EmployeeSkill> skills,
                                            @Param("skillCount") long skillCount);
    
    /**
     * Find employees by name (case-insensitive).
     * Useful for searching/filtering employees by name.
     * 
     * @param name The name to search for
     * @return List of employees with matching names
     */
    List<Employee> findByNameIgnoreCase(String name);
}