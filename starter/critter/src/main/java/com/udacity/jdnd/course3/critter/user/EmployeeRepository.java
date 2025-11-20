package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    List<Employee> findByDaysAvailableContains(DayOfWeek day);
    
    List<Employee> findBySkillsContains(EmployeeSkill skill);
    
    @Query("SELECT DISTINCT e FROM Employee e JOIN e.skills s " +
           "WHERE :day MEMBER OF e.daysAvailable AND s IN :skills")
    List<Employee> findAvailableEmployeesWithSkills(@Param("day") DayOfWeek day, 
                                                   @Param("skills") Set<EmployeeSkill> skills);
}