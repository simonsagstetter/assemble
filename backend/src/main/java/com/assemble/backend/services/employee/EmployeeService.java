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

import com.assemble.backend.models.dtos.employee.EmployeeCreateDTO;
import com.assemble.backend.models.dtos.employee.EmployeeDTO;
import com.assemble.backend.models.dtos.employee.EmployeeUpdateDTO;
import com.assemble.backend.models.dtos.employee.EmployeeUpdateUserDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById( String id );

    EmployeeDTO setEmployeeUser( String employeeId, EmployeeUpdateUserDTO employeeUpdateUserDTO );

    EmployeeDTO createEmployee( EmployeeCreateDTO employeeCreateDTO );

    EmployeeDTO updateEmployee( String id, EmployeeUpdateDTO employeeUpdateDTO );

    void deleteEmployee( String id );


}
