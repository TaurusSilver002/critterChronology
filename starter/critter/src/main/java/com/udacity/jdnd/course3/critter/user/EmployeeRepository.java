package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Repository interface for Employee entity with custom scheduling queries
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Finds employees available on a specific day of week
    List<Employee> findByDaysAvailableContains(DayOfWeek day);
    
    // Finds employees with a specific skill
    List<Employee> findBySkillsContains(EmployeeSkill skill);
    
    // Custom JPQL query: Finds employees available on specific day with any of the required skills
    @Query("SELECT DISTINCT e FROM Employee e JOIN e.skills s " +
           "WHERE :day MEMBER OF e.daysAvailable AND s IN :skills")
    List<Employee> findAvailableEmployeesWithSkills(@Param("day") DayOfWeek day, 
                                                   @Param("skills") Set<EmployeeSkill> skills);
}