/*
 * assemble
 * EmployeeRepository.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.repositories.employee;


import com.assemble.backend.models.entities.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query("""
                SELECT e
                FROM Employee e
                WHERE e.user IS NULL
                AND (
                    LOWER(e.firstname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                    OR LOWER(e.lastname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                )
            """)
    List<Employee> search( @Param("searchTerm") String searchTerm );

    @Query("""
                SELECT e
                FROM Employee e
                WHERE LOWER(e.firstname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                    OR LOWER(e.lastname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            """)
    List<Employee> searchAll( @Param("searchTerm") String searchTerm );

}
