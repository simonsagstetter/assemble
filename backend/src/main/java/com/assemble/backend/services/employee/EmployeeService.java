/*
 * assemble
 * EmployeeService.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.employee;

import com.assemble.backend.models.dtos.employee.*;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById( String id );

    EmployeeDTO setEmployeeUser( String employeeId, EmployeeUpdateUserDTO employeeUpdateUserDTO );

    List<EmployeeRefDTO> searchUnlinkedEmployees( String searchTerm );

    List<EmployeeRefDTO> searchAllEmployees( String searchTerm );

    EmployeeDTO createEmployee( EmployeeCreateDTO employeeCreateDTO );

    EmployeeDTO updateEmployee( String id, EmployeeUpdateDTO employeeUpdateDTO );

    void deleteEmployee( String id );


}
