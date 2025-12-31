/*
 * assemble
 * ProjectAssignmentServiceImpl.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.project;

import com.assemble.backend.models.dtos.project.ProjectAssignmentCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectAssignmentDTO;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.project.ProjectAssignment;
import com.assemble.backend.models.mappers.project.ProjectAssignmentMapper;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.repositories.project.ProjectAssignmentRepository;
import com.assemble.backend.repositories.project.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class ProjectAssignmentServiceImpl implements ProjectAssignmentService {

    private ProjectAssignmentRepository projectAssignmentRepository;
    private ProjectAssignmentMapper projectAssignmentMapper;
    private EmployeeRepository employeeRepository;
    private ProjectRepository projectRepository;

    @Override
    public ProjectAssignmentDTO getProjectAssignmentById( String id ) {
        ProjectAssignment projectAssignment = projectAssignmentRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find project assignment with id: " + id )
                );

        return projectAssignmentMapper.toProjectAssignmentDTO( projectAssignment );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectAssignmentDTO> getProjectAssignmentsByProjectId( String projectId ) {
        return projectAssignmentRepository
                .findAllByProjectId( UUID.fromString( projectId ) )
                .stream()
                .map( projectAssignmentMapper::toProjectAssignmentDTO )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectAssignmentDTO> getProjectAssignmentsByEmployeeId( String employeeId ) {
        return projectAssignmentRepository
                .findAllByEmployeeId( UUID.fromString( employeeId ) )
                .stream()
                .map( projectAssignmentMapper::toProjectAssignmentDTO )
                .toList();
    }

    @Override
    @Transactional
    public ProjectAssignmentDTO createProjectAssignment( ProjectAssignmentCreateDTO projectAssignmentCreateDTO ) {
        Employee employee = employeeRepository.findById( UUID.fromString( projectAssignmentCreateDTO.getEmployeeId() ) )
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Could not find employee with id: " + projectAssignmentCreateDTO.getEmployeeId()
                        )
                );

        Project project = projectRepository.findById( UUID.fromString( projectAssignmentCreateDTO.getProjectId() ) )
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Could not find project with id: " + projectAssignmentCreateDTO.getProjectId()
                        )
                );

        ProjectAssignment projectAssignment = projectAssignmentRepository.save( projectAssignmentMapper
                .toProjectAssignment( projectAssignmentCreateDTO, employee, project )
        );


        return projectAssignmentMapper.toProjectAssignmentDTO( projectAssignment );

    }

    @Override
    @Transactional
    public void deleteProjectAssignmentById( String id ) {
        ProjectAssignment projectAssignment = projectAssignmentRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find project assignment with id: " + id )
                );

        projectAssignmentRepository.delete( projectAssignment );
    }
}
