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
import com.assemble.backend.models.mappers.project.ProjectMapper;
import com.assemble.backend.repositories.project.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

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
    public ProjectDTO createProject( ProjectCreateDTO projectCreateDTO ) {
        Project newProject = projectRepository.save( projectMapper.toProject( projectCreateDTO ) );
        return projectMapper.toProjectDTO( newProject );
    }

    @Override
    public void deleteProjectById( String id ) {
        Project project = projectRepository.findById( UUID.fromString( id ) )
                .orElseThrow(
                        () -> new EntityNotFoundException( "Could not find project with id: " + id )
                );

        projectRepository.delete( project );
    }
}
