/*
 * assemble
 * ProjectAssignmentServiceImplTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.project;

import com.assemble.backend.models.dtos.employee.EmployeeRefDTO;
import com.assemble.backend.models.dtos.project.ProjectAssignmentCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectAssignmentDTO;
import com.assemble.backend.models.dtos.project.ProjectRefDTO;
import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.project.ProjectAssignment;
import com.assemble.backend.models.entities.project.ProjectStage;
import com.assemble.backend.models.entities.project.ProjectType;
import com.assemble.backend.models.mappers.project.ProjectAssignmentMapper;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.repositories.project.ProjectAssignmentRepository;
import com.assemble.backend.repositories.project.ProjectRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectAssignmentServiceImpl Unit Test")
class ProjectAssignmentServiceImplTest {

    @Mock
    private ProjectAssignmentRepository projectAssignmentRepository;

    @Mock
    private ProjectAssignmentMapper projectAssignmentMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectAssignmentServiceImpl service;

    private static UUID randomId;
    private static UUID recordId;
    private static Project testProject;
    private static Employee testEmployee;
    private static ProjectAssignment testProjectAssignment;
    private static ProjectAssignmentDTO testProjectAssignmentDTO;

    @BeforeAll
    static void init() {
        UserAudit userAudit = new UserAudit( null, "SYSTEM" );
        Instant now = Instant.now();

        randomId = UuidCreator.getTimeOrderedEpoch();
        recordId = UuidCreator.getTimeOrderedEpoch();

        testProject = Project.builder()
                .id( recordId )
                .no( "P000001" )
                .name( "Test Project" )
                .description( "Test Project Description" )
                .category( "Maintenance" )
                .stage( ProjectStage.PROPOSAL )
                .type( ProjectType.EXTERNAL )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        testEmployee = Employee.builder()
                .id( recordId )
                .no( "E000001" )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .user( null )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        testProjectAssignment = ProjectAssignment.builder()
                .id( recordId )
                .project( testProject )
                .employee( testEmployee )
                .active( true )
                .hourlyRate( BigDecimal.valueOf( 30 ) )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        testProjectAssignmentDTO = ProjectAssignmentDTO.builder()
                .id( recordId.toString() )
                .project(
                        ProjectRefDTO.builder()
                                .id( recordId.toString() )
                                .no( testProject.getNo() )
                                .name( testProject.getName() )
                                .build()
                )
                .employee(
                        EmployeeRefDTO.builder()
                                .id( recordId.toString() )
                                .fullname( testEmployee.getFullname() )
                                .build()
                )
                .active( testProjectAssignment.isActive() )
                .hourlyRate( testProjectAssignment.getHourlyRate() )
                .createdDate( testProjectAssignment.getCreatedDate() )
                .lastModifiedDate( testProjectAssignment.getLastModifiedDate() )
                .createdBy( testProjectAssignment.getCreatedBy() )
                .lastModifiedBy( testProjectAssignment.getLastModifiedBy() )
                .build();
    }

    @Test
    @DisplayName("getProjectAssignmentsByProjectId should return empty list when no ProjectAssignment found")
    void getProjectAssignmentsByProjectId_ShouldReturnEmptyList_WhenNoProjectAssignmentFound() {
        when( projectAssignmentRepository.findAllByProjectId( randomId ) ).thenReturn( List.of() );

        List<ProjectAssignmentDTO> actual = service.getProjectAssignmentsByProjectId( randomId.toString() );

        assertEquals( 0, actual.size() );

        verify( projectAssignmentRepository, times( 1 ) ).findAllByProjectId( randomId );
    }

    @Test
    @DisplayName("getProjectAssignmentsByProjectId should return list of ProjectAssignmentDTO when ProjectAssignment found")
    void getProjectAssignmentsByProjectId_ShouldReturnListOfProjectAssignmentDTO_WhenProjectAssignmentFound() {
        when( projectAssignmentRepository.findAllByProjectId( recordId ) )
                .thenReturn( List.of( testProjectAssignment ) );

        when( projectAssignmentMapper.toProjectAssignmentDTO( testProjectAssignment ) )
                .thenReturn( testProjectAssignmentDTO );

        List<ProjectAssignmentDTO> actual = service.getProjectAssignmentsByProjectId( recordId.toString() );

        assertEquals( 1, actual.size() );
        assertEquals( testProjectAssignmentDTO, actual.getFirst() );

        verify( projectAssignmentRepository, times( 1 ) ).findAllByProjectId( recordId );
        verify( projectAssignmentMapper, times( 1 ) ).toProjectAssignmentDTO( testProjectAssignment );
    }

    @Test
    @DisplayName("getProjectAssignmentsByEmployeeId should return empty list when no ProjectAssignment found")
    void getProjectAssignmentsByEmployeeId_ShouldReturnEmptyList_WhenNoProjectAssignmentFound() {
        when( projectAssignmentRepository.findAllByEmployeeId( randomId ) ).thenReturn( List.of() );

        List<ProjectAssignmentDTO> actual = service.getProjectAssignmentsByEmployeeId( randomId.toString() );

        assertEquals( 0, actual.size() );

        verify( projectAssignmentRepository, times( 1 ) ).findAllByEmployeeId( randomId );
    }

    @Test
    @DisplayName("getProjectAssignmentsByEmployeeId should return list of ProjectAssignmentDTO when ProjectAssignment found")
    void getProjectAssignmentsByEmployeeId_ShouldReturnListOfProjectAssignmentDTO_WhenProjectAssignmentFound() {
        when( projectAssignmentRepository.findAllByEmployeeId( recordId ) )
                .thenReturn( List.of( testProjectAssignment ) );

        when( projectAssignmentMapper.toProjectAssignmentDTO( testProjectAssignment ) )
                .thenReturn( testProjectAssignmentDTO );

        List<ProjectAssignmentDTO> actual = service.getProjectAssignmentsByEmployeeId( recordId.toString() );

        assertEquals( 1, actual.size() );
        assertEquals( testProjectAssignmentDTO, actual.getFirst() );

        verify( projectAssignmentRepository, times( 1 ) ).findAllByEmployeeId( recordId );
        verify( projectAssignmentMapper, times( 1 ) ).toProjectAssignmentDTO( testProjectAssignment );
    }

    @Test
    @DisplayName("createProjectAssignment should throw when employee does not exist")
    void createProjectAssignment_ShouldThrow_WhenEmployeeDoesNotExist() {
        ProjectAssignmentCreateDTO assignment = ProjectAssignmentCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( randomId.toString() )
                .active( true )
                .hourlyRate( BigDecimal.valueOf( 30 ) )
                .build();

        when( employeeRepository.findById( randomId ) ).thenReturn( java.util.Optional.empty() );


        assertThrows( EntityNotFoundException.class, () -> service.createProjectAssignment( assignment ) );

        verify( employeeRepository, times( 1 ) ).findById( randomId );
    }

    @Test
    @DisplayName("createProjectAssignment should throw when project does not exist")
    void createProjectAssignment_ShouldThrow_WhenProjectDoesNotExist() {
        ProjectAssignmentCreateDTO assignment = ProjectAssignmentCreateDTO.builder()
                .employeeId( recordId.toString() )
                .projectId( randomId.toString() )
                .active( true )
                .hourlyRate( BigDecimal.valueOf( 30 ) )
                .build();

        when( employeeRepository.findById( recordId ) ).thenReturn( java.util.Optional.of( testEmployee ) );
        when( projectRepository.findById( randomId ) ).thenReturn( java.util.Optional.empty() );


        assertThrows( EntityNotFoundException.class, () -> service.createProjectAssignment( assignment ) );

        verify( employeeRepository, times( 1 ) ).findById( recordId );
        verify( projectRepository, times( 1 ) ).findById( randomId );
    }

    @Test
    @DisplayName("createProjectAssignment should return ProjectAssignmentDTO when ProjectAssignment was created")
    void createProjectAssignment_ShouldReturnProjectAssignmentDTO_WhenProjectAssignmentWasCreated() {
        ProjectAssignmentCreateDTO assignment = ProjectAssignmentCreateDTO.builder()
                .projectId( recordId.toString() )
                .employeeId( recordId.toString() )
                .active( true )
                .hourlyRate( BigDecimal.valueOf( 30 ) )
                .build();

        when( employeeRepository.findById( recordId ) ).thenReturn( java.util.Optional.of( testEmployee ) );
        when( projectRepository.findById( recordId ) ).thenReturn( java.util.Optional.of( testProject ) );

        when( projectAssignmentMapper.toProjectAssignment( assignment, testEmployee, testProject ) )
                .thenReturn( testProjectAssignment );

        when( projectAssignmentRepository.save( testProjectAssignment ) )
                .thenReturn( testProjectAssignment );

        when( projectAssignmentMapper.toProjectAssignmentDTO( testProjectAssignment ) )
                .thenReturn( testProjectAssignmentDTO );

        ProjectAssignmentDTO actual = assertDoesNotThrow( () -> service.createProjectAssignment( assignment ) );

        assertEquals( testProjectAssignmentDTO, actual );

        verify( employeeRepository, times( 1 ) ).findById( recordId );
        verify( projectRepository, times( 1 ) ).findById( recordId );
        verify( projectAssignmentMapper, times( 1 ) )
                .toProjectAssignment( assignment, testEmployee, testProject );
        verify( projectAssignmentRepository, times( 1 ) )
                .save( testProjectAssignment );

        verify( projectAssignmentMapper, times( 1 ) )
                .toProjectAssignmentDTO( testProjectAssignment );
    }

    @Test
    @DisplayName("deleteProjectAssignment should throw when ProjectAssignment does not exist")
    void deleteProjectAssignment_ShouldThrow_WhenProjectAssignmentDoesNotExist() {

        when( projectAssignmentRepository.findById( randomId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.deleteProjectAssignmentById( randomId.toString() ) );

        verify( projectAssignmentRepository, times( 1 ) ).findById( randomId );
    }

    @Test
    @DisplayName("deleteProjectAssignmentsshould delete ProjectAssignment when ProjectAssignemnt exist")
    void deleteProjectAssignment_ShouldDeleteProjectAssignment_WhenProjectAssignmentExist() {
        when( projectAssignmentRepository.findById( recordId ) )
                .thenReturn( Optional.of( testProjectAssignment ) );

        assertDoesNotThrow( () -> service.deleteProjectAssignmentById( recordId.toString() ) );

        verify( projectAssignmentRepository, times( 1 ) ).findById( recordId );
        verify( projectAssignmentRepository, times( 1 ) ).delete( testProjectAssignment );
    }
}