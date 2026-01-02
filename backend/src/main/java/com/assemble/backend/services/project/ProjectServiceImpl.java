/*
 * assemble
 * ProjectServiceImpl.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.project;

import com.assemble.backend.models.dtos.project.ProjectCreateDTO;
import com.assemble.backend.models.dtos.project.ProjectDTO;
import com.assemble.backend.models.entities.project.Project;
import com.assemble.backend.models.entities.project.ProjectAssignment;
import com.assemble.backend.models.mappers.project.ProjectMapper;
import com.assemble.backend.repositories.project.ProjectAssignmentRepository;
import com.assemble.backend.repositories.project.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectAssignmentRepository assignmentRepository;

    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map( projectMapper::toProjectDTO )
                .toList();
    }

    @Override
    public ProjectDTO getProjectById( String id ) {
        Project project = projectRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find project with id: " + id )
                );

        return projectMapper.toProjectDTO( project );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> searchAllProjects( String searchTerm ) {
        String normalizedSearchTerm = searchTerm == null ? "" : searchTerm.toLowerCase();
        return projectRepository
                .searchAll( normalizedSearchTerm )
                .stream()
                .map( projectMapper::toProjectDTO )
                .toList();
    }

    @Override
    public ProjectDTO createProject( ProjectCreateDTO projectCreateDTO ) {
        Project newProject = projectRepository.save( projectMapper.toProject( projectCreateDTO ) );
        return projectMapper.toProjectDTO( newProject );
    }

    @Override
    @Transactional
    public void deleteProjectById( String id ) {
        Project project = projectRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find project with id: " + id )
                );

        assignmentRepository.deleteAll(
                assignmentRepository
                        .findAllByProjectId( project.getId() )
        );

        projectRepository.delete( project );
    }
}
