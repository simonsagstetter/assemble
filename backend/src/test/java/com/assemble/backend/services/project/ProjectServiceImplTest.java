/*
 * assemble
 * ProjectServiceImplTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.project;

import com.assemble.backend.models.dtos.project.ProjectCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectDTO;
import com.assemble.backend.models.dtos.project.ProjectUpdateDTO;
import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.entities.project.*;
import com.assemble.backend.models.mappers.project.ProjectMapper;
import com.assemble.backend.repositories.project.ProjectAssignmentRepository;
import com.assemble.backend.repositories.project.ProjectRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectServiceImpl Unit Test")
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private ProjectAssignmentRepository projectAssignmentRepository;

    @InjectMocks
    private ProjectServiceImpl service;

    private static UUID randomId;
    private static UUID testProjectId;
    private static Project testProject;
    private static ProjectDTO testProjectDTO;

    @BeforeEach
    void init() {
        UserAudit userAudit = new UserAudit( null, "SYSTEM" );
        Instant now = Instant.now();

        randomId = UuidCreator.getTimeOrderedEpoch();
        testProjectId = UuidCreator.getTimeOrderedEpoch();

        testProject = Project.builder()
                .id( testProjectId )
                .no( "P000001" )
                .name( "Test Project" )
                .description( "Test Project Description" )
                .category( "Maintanance" )
                .stage( ProjectStage.PROPOSAL )
                .type( ProjectType.EXTERNAL )
                .color( ProjectColor.PURPLE )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        testProjectDTO = ProjectDTO.builder()
                .id( testProjectId.toString() )
                .no( testProject.getNo() )
                .name( testProject.getName() )
                .active( testProject.isActive() )
                .stage( testProject.getStage() )
                .color( ProjectColor.PURPLE )
                .type( testProject.getType() )
                .description( testProject.getDescription() )
                .category( testProject.getCategory() )
                .createdDate( testProject.getCreatedDate() )
                .lastModifiedDate( testProject.getLastModifiedDate() )
                .createdBy( testProject.getCreatedBy() )
                .lastModifiedBy( testProject.getLastModifiedBy() )
                .build();

        reset( projectMapper, projectRepository );
    }

    @Test
    @DisplayName("getAllProjects should return empty list when no project exists in db")
    void getAllProjects_ShouldReturnEmptyList_WhenNoProjectExistsInDB() {
        when( projectRepository.findAll() ).thenReturn( List.of() );

        List<ProjectDTO> actual = service.getAllProjects();

        assertEquals( 0, actual.size() );

        verify( projectRepository, times( 1 ) ).findAll();
    }

    @Test
    @DisplayName("getAllProjects should return a project list when project exists in db")
    void getAllProjects_ShouldReturnProjectList_WhenProjectExistsInDB() {
        when( projectRepository.findAll() ).thenReturn( List.of( testProject ) );
        when( projectMapper.toProjectDTO( testProject ) ).thenReturn( testProjectDTO );

        List<ProjectDTO> actual = service.getAllProjects();

        assertEquals( 1, actual.size() );
        assertEquals( testProjectDTO, actual.getFirst() );

        verify( projectRepository, times( 1 ) ).findAll();
        verify( projectMapper, times( 1 ) ).toProjectDTO( testProject );
    }

    @Test
    @DisplayName("getProjectById should throw when project not found")
    void getProjectById_ShouldThrow_WhenProjectNotFound() {
        when( projectRepository.findById( randomId ) ).thenReturn( Optional.empty() );

        String randomIdString = randomId.toString();
        assertThrows( EntityNotFoundException.class, () -> service.getProjectById( randomIdString ) );

        verify( projectRepository, times( 1 ) ).findById( randomId );
    }

    @Test
    @DisplayName("getProjectById should return project dto instance when project id is valid")
    void getProjectById_ShouldReturnProjectDTOInstance_WhenProjectIdIsValid() {
        when( projectRepository.findById( testProjectId ) ).thenReturn( Optional.of( testProject ) );
        when( projectMapper.toProjectDTO( testProject ) ).thenReturn( testProjectDTO );

        ProjectDTO actual = assertDoesNotThrow( () -> service.getProjectById( testProjectId.toString() ) );

        assertEquals( testProjectDTO, actual );

        verify( projectRepository, times( 1 ) ).findById( testProjectId );
        verify( projectMapper, times( 1 ) ).toProjectDTO( testProject );
    }

    @Test
    @DisplayName("searchAllProjects should return a list of ProjectDTO if project found")
    void searchAllProjects_ShouldReturnListOfProjectDTO_WhenProjectFound() {
        String searchTerm = "Test";
        when( projectRepository.searchAll( searchTerm.toLowerCase() ) )
                .thenReturn( List.of( testProject ) );


        when( projectMapper.toProjectDTO( testProject ) ).thenReturn( testProjectDTO );

        List<ProjectDTO> actual = assertDoesNotThrow( () -> service.searchAllProjects( searchTerm ) );

        assertEquals( 1, actual.size() );
        assertThat( actual ).contains( testProjectDTO );

        verify( projectRepository, times( 1 ) ).searchAll( searchTerm.toLowerCase() );
        verify( projectMapper, times( 1 ) ).toProjectDTO( testProject );
    }

    @Test
    @DisplayName("createProject should return project dto instance when project was created")
    void createProject_ShouldReturnProjectDTOInstance_WhenProjectWasCreated() {
        ProjectCreateDTO projectCreateDTO = ProjectCreateDTO.builder()
                .name( "Test Project" )
                .description( "Test Project Description" )
                .category( "Maintanance" )
                .stage( ProjectStage.PROPOSAL )
                .type( ProjectType.EXTERNAL )
                .build();

        Project mappedProject = Project.builder()
                .name( projectCreateDTO.getName() )
                .description( projectCreateDTO.getDescription() )
                .category( projectCreateDTO.getCategory() )
                .stage( projectCreateDTO.getStage() )
                .type( projectCreateDTO.getType() )
                .active( true )
                .build();

        when( projectMapper.toProject( projectCreateDTO ) ).thenReturn( mappedProject );
        when( projectRepository.save( mappedProject ) ).thenReturn( testProject );
        when( projectMapper.toProjectDTO( testProject ) ).thenReturn( testProjectDTO );

        ProjectDTO actual = assertDoesNotThrow( () -> service.createProject( projectCreateDTO ) );

        assertEquals( testProjectDTO, actual );

        verify( projectMapper, times( 1 ) ).toProject( projectCreateDTO );
        verify( projectRepository, times( 1 ) ).save( mappedProject );
        verify( projectMapper, times( 1 ) ).toProjectDTO( testProject );
    }

    @Test
    @DisplayName("updateProject should throw when project not found")
    void updateProject_ShouldThrow_WhenProjectNotFound() {
        ProjectUpdateDTO projectUpdateDTO = ProjectUpdateDTO.builder()
                .name( "Test Project" )
                .build();

        when( projectRepository.findById( randomId ) ).thenReturn( Optional.empty() );

        String randomIdString = randomId.toString();
        assertThrows( EntityNotFoundException.class, () -> service.updateProject( randomIdString, projectUpdateDTO ) );

        verify( projectRepository, times( 1 ) ).findById( randomId );
    }

    @Test
    @DisplayName("updateProject should return project dto when all fields have updates")
    void updateProject_ShouldReturnProjectDTO_WhenAllFieldsHaveUpdates() {
        ProjectUpdateDTO projectUpdateDTO = ProjectUpdateDTO.builder()
                .name( "Updated project name" )
                .description( "Updated project description" )
                .active( false )
                .color( ProjectColor.YELLOW )
                .category( "Updated category" )
                .type( ProjectType.INTERNAL )
                .stage( ProjectStage.NEGOTIATION )
                .build();

        when( projectRepository.findById( testProjectId ) ).thenReturn( Optional.of( testProject ) );

        Project updatedProject = Project.builder()
                .id( testProjectId )
                .no( testProject.getNo() )
                .name( projectUpdateDTO.getName() )
                .description( projectUpdateDTO.getDescription() )
                .active( projectUpdateDTO.getActive() )
                .color( projectUpdateDTO.getColor() )
                .category( projectUpdateDTO.getCategory() )
                .type( projectUpdateDTO.getType() )
                .stage( projectUpdateDTO.getStage() )
                .createdDate( testProject.getCreatedDate() )
                .lastModifiedDate( testProject.getLastModifiedDate() )
                .createdBy( testProject.getCreatedBy() )
                .lastModifiedBy( testProject.getLastModifiedBy() )
                .build();

        when( projectMapper.toProject( projectUpdateDTO, testProject ) ).thenReturn( updatedProject );

        when( projectRepository.save( updatedProject ) ).thenReturn( updatedProject );

        assertNotNull( projectUpdateDTO.getName() );

        testProjectDTO.setName( projectUpdateDTO.getName() );
        testProjectDTO.setDescription( projectUpdateDTO.getDescription() );
        testProjectDTO.setActive( projectUpdateDTO.getActive() );
        testProjectDTO.setColor( projectUpdateDTO.getColor() );
        testProjectDTO.setCategory( projectUpdateDTO.getCategory() );
        testProjectDTO.setType( projectUpdateDTO.getType() );
        testProjectDTO.setStage( projectUpdateDTO.getStage() );

        when( projectMapper.toProjectDTO( updatedProject ) ).thenReturn( testProjectDTO );

        String testProjectIdString = testProjectId.toString();
        ProjectDTO actual = assertDoesNotThrow( () -> service.updateProject( testProjectIdString, projectUpdateDTO ) );

        assertEquals( testProjectDTO, actual );

        verify( projectRepository, times( 1 ) ).findById( testProjectId );
        verify( projectMapper, times( 1 ) ).toProject( projectUpdateDTO, testProject );
        verify( projectRepository, times( 1 ) ).save( updatedProject );
        verify( projectMapper, times( 1 ) ).toProjectDTO( updatedProject );
    }

    @Test
    @DisplayName("updateProject should return project dto when no fields have updates")
    void updateProject_ShouldReturnProjectDTO_WhenNoFieldsHaveUpdates() {
        ProjectUpdateDTO projectUpdateDTO = ProjectUpdateDTO.builder()
                .build();

        when( projectRepository.findById( testProjectId ) ).thenReturn( Optional.of( testProject ) );
        when( projectMapper.toProject( projectUpdateDTO, testProject ) ).thenReturn( testProject );
        when( projectRepository.save( testProject ) ).thenReturn( testProject );
        when( projectMapper.toProjectDTO( testProject ) ).thenReturn( testProjectDTO );

        String testProjectIdString = testProjectId.toString();
        ProjectDTO actual = assertDoesNotThrow( () -> service.updateProject( testProjectIdString, projectUpdateDTO ) );

        assertEquals( testProjectDTO, actual );

        verify( projectRepository, times( 1 ) ).findById( testProjectId );
        verify( projectMapper, times( 1 ) ).toProject( projectUpdateDTO, testProject );
        verify( projectRepository, times( 1 ) ).save( testProject );
        verify( projectMapper, times( 1 ) ).toProjectDTO( testProject );
    }

    @Test
    @DisplayName("deleteProjectById should throw when project does not exist in db")
    void deleteProjectById_ShouldThrow_WhenProjectDoesNotExistInDB() {
        when( projectRepository.findById( randomId ) ).thenReturn( Optional.empty() );

        String randomIdString = randomId.toString();
        assertThrows( EntityNotFoundException.class, () -> service.deleteProjectById( randomIdString ) );

        verify( projectRepository, times( 1 ) ).findById( randomId );
    }

    @Test
    @DisplayName("deleteProjectById should delete project when project exists in db")
    void deleteProjectById_ShouldDeleteProject_WhenProjectExistsInDB() {
        when( projectRepository.findById( testProjectId ) ).thenReturn( Optional.of( testProject ) );
        when( projectAssignmentRepository.findAllByProjectId( testProjectId ) ).thenReturn( List.of() );

        assertDoesNotThrow( () -> service.deleteProjectById( testProjectId.toString() ) );

        verify( projectRepository, times( 1 ) ).findById( testProjectId );
        verify( projectRepository, times( 1 ) ).delete( testProject );
        verify( projectAssignmentRepository, times( 1 ) ).findAllByProjectId( testProjectId );
    }

    @Test
    @DisplayName("deleteProjectById should delete assignments before deletion when project exists in db")
    void deleteProjectById_ShouldDeleteAssignmentsBeforeDeletion_WhenProjectExistsInDB() {
        ProjectAssignment assignment = ProjectAssignment.builder()
                .employee( Employee.builder()
                        .id( testProjectId )
                        .no( "E000001" )
                        .firstname( "Max" )
                        .lastname( "Mustermann" )
                        .email( "max.mustermann@example.com" )
                        .build() )
                .project( testProject )
                .build();

        when( projectRepository.findById( testProjectId ) ).thenReturn( Optional.of( testProject ) );
        when( projectAssignmentRepository.findAllByProjectId( testProjectId ) ).thenReturn( List.of( assignment ) );

        assertDoesNotThrow( () -> service.deleteProjectById( testProjectId.toString() ) );

        verify( projectRepository, times( 1 ) ).findById( testProjectId );
        verify( projectRepository, times( 1 ) ).delete( testProject );
        verify( projectAssignmentRepository, times( 1 ) ).findAllByProjectId( testProjectId );
        verify( projectAssignmentRepository, times( 1 ) ).deleteAll( List.of( assignment ) );
    }

}