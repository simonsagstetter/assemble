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
import com.assemble.backend.models.dtos.project.ProjectAssignmentDeleteDTO;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectAssignmentServiceImpl implements ProjectAssignmentService {

    private ProjectAssignmentRepository projectAssignmentRepository;
    private ProjectAssignmentMapper projectAssignmentMapper;
    private EmployeeRepository employeeRepository;
    private ProjectRepository projectRepository;

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
    public List<ProjectAssignmentDTO> createProjectAssignments( List<ProjectAssignmentCreateDTO> assignments ) {
        Collection<UUID> employeeIds = assignments.stream()
                .map( assignment -> UUID.fromString( assignment.getEmployeeId() ) )
                .toList();
        Collection<UUID> projectIds = assignments.stream()
                .map( assignment -> UUID.fromString( assignment.getProjectId() ) )
                .toList();

        List<Employee> employees = employeeRepository.findAllByIdIsIn( employeeIds );
        List<Project> projects = projectRepository.findAllByIdIsIn( projectIds );

        Map<UUID, Employee> employeesMap = employees.stream()
                .collect( Collectors.toMap(
                        Employee::getId,
                        Function.identity()
                ) );
        Map<UUID, Project> projectsMap = projects.stream()
                .collect( Collectors.toMap(
                        Project::getId,
                        Function.identity()
                ) );

        List<ProjectAssignment> projectAssignments = new ArrayList<>();

        for ( ProjectAssignmentCreateDTO assignment : assignments ) {
            Employee employee = employeesMap.get( UUID.fromString( assignment.getEmployeeId() ) );
            Project project = projectsMap.get( UUID.fromString( assignment.getProjectId() ) );
            if ( employee == null || project == null )
                throw new EntityNotFoundException( "Employee or Project not found" );

            projectAssignments.add(
                    projectAssignmentMapper
                            .toProjectAssignment( assignment, employee, project )
            );
        }

        List<ProjectAssignment> savedProjectAssignments = projectAssignmentRepository.saveAll( projectAssignments );

        return savedProjectAssignments.stream()
                .map( projectAssignmentMapper::toProjectAssignmentDTO )
                .toList();

    }

    @Override
    @Transactional
    public void deleteProjectAssignmentByIds( ProjectAssignmentDeleteDTO deleteDTO ) {
        List<ProjectAssignment> assignments = projectAssignmentRepository
                .findAllByIdIsIn(
                        deleteDTO.getIds()
                                .stream()
                                .map( UUID::fromString )
                                .toList()
                );

        Map<UUID, ProjectAssignment> assignmentMap = assignments.stream()
                .collect( Collectors.toMap(
                        ProjectAssignment::getId,
                        Function.identity()
                ) );

        for ( String id : deleteDTO.getIds() ) {
            ProjectAssignment assignment = assignmentMap.get( UUID.fromString( id ) );
            if ( assignment == null ) throw new EntityNotFoundException( "Assignment not found" );
        }

        projectAssignmentRepository.deleteAll( assignments );
    }
}
