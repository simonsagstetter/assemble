/*
 * assemble
 * EmployeeServiceImplTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.employee;

import com.assemble.backend.models.dtos.employee.*;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.mappers.employee.EmployeeMapper;
import com.assemble.backend.repositories.auth.UserRepository;
import com.assemble.backend.repositories.employee.EmployeeRepository;
import com.assemble.backend.services.core.IdService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeServiceImpl Unit Test")
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private IdService idService;

    @InjectMocks
    private EmployeeServiceImpl service;

    private static Employee employee;

    private static EmployeeDTO employeeDTO;

    private static User user;
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @BeforeAll
    static void setUp() {
        Instant now = Instant.now();
        UserAudit userAudit = new UserAudit( null, "SYSTEM" );
        employee = Employee.builder()
                .id( "1" )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .user( null )
                .createdDate( now )
                .lastModifiedDate( now )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .build();

        employeeDTO = EmployeeDTO.builder()
                .id( "1" )
                .fullname( employee.getFullname() )
                .firstname( employee.getFirstname() )
                .lastname( employee.getLastname() )
                .email( employee.getEmail() )
                .createdDate( employee.getCreatedDate() )
                .createdBy( userAudit )
                .lastModifiedBy( userAudit )
                .lastModifiedDate( employee.getLastModifiedDate() )
                .user( null )
                .build();

        user = User.builder()
                .id( "1" )
                .username( "testuser" )
                .firstname( "Test" )
                .lastname( "User" )
                .email( "test@example.com" )
                .roles( List.of( UserRole.USER ) )
                .password( PASSWORD_ENCODER.encode( "secret" ) )
                .createdDate( Instant.now() )
                .lastModifiedDate( Instant.now() )
                .createdBy( new UserAudit( null, "SYSTEM" ) )
                .lastModifiedBy( new UserAudit( null, "SYSTEM" ) )
                .build();
    }

    @BeforeEach
    void resetMocks() {
        reset( employeeRepository, userRepository, employeeMapper, idService );
        employee.setUser( null );
        employeeDTO.setUser( null );
        user.setEmployee( null );
    }

    @Test
    @DisplayName("getAllEmployees should return empty list if no employee exists")
    void getAllEmployees_ShouldReturnEmptyList_WhenNoEmployeeExists() {
        when( employeeRepository.findAll() ).thenReturn( List.of() );

        List<EmployeeDTO> actual = assertDoesNotThrow( () -> service.getAllEmployees() );

        assertEquals( 0, actual.size() );

        verify( employeeRepository, times( 1 ) ).findAll();
    }

    @Test
    @DisplayName("getAllEmployees should return a list of EmployeeDTO if employee exists")
    void getAllEmployees_ShouldReturnListOfEmployeeDTO_WhenEmployeeExists() {
        when( employeeRepository.findAll() ).thenReturn( List.of( employee ) );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        List<EmployeeDTO> actual = assertDoesNotThrow( () -> service.getAllEmployees() );

        assertEquals( 1, actual.size() );
        assertThat( actual ).contains( employeeDTO );

        verify( employeeRepository, times( 1 ) ).findAll();
        verify( employeeMapper, times( 1 ) ).employeeToEmployeeDTO( employee );
    }

    @Test
    @DisplayName("getEmployeeById should throw when employee does not exist")
    void getEmployeeById_ShouldThrow_WhenEmployeeDoesNotExist() {
        when( employeeRepository.findById( "1" ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.getEmployeeById( "1" ) );

        verify( employeeRepository, times( 1 ) ).findById( "1" );
    }

    @Test
    @DisplayName("getEmployeeById should return EmployeeDTO when employee exists")
    void getEmployeeById_ShouldReturnEmployeeDTO_WhenEmployeeExists() {
        String id = employee.getId();
        assert id != null;
        when( employeeRepository.findById( id ) ).thenReturn( Optional.of( employee ) );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.getEmployeeById( id ) );

        assertEquals( employeeDTO, actual );

        verify( employeeRepository, times( 1 ) ).findById( id );
        verify( employeeMapper, times( 1 ) ).employeeToEmployeeDTO( employee );
    }

    @Test
    @DisplayName("searchUnlinkedEmployees should return a list of EmployeeRefDTO if employee found")
    void searchUnlinkedEmployees_ShouldReturnListOfEmployeeRefDTO_WhenEmployeeFound() {
        when( employeeRepository.search( anyString() ) )
                .thenReturn( List.of( employee ) );

        EmployeeRefDTO employeeRefDTO = EmployeeRefDTO.builder()
                .id( employee.getId() )
                .fullname( employee.getFullname() )
                .build();

        when( employeeMapper.employeeToEmployeeRefDTO( employee ) ).thenReturn( employeeRefDTO );

        List<EmployeeRefDTO> actual = assertDoesNotThrow( () -> service.searchUnlinkedEmployees( "Max" ) );

        assertEquals( 1, actual.size() );
        assertThat( actual ).contains( employeeRefDTO );

        verify( employeeRepository, times( 1 ) ).search( anyString() );
        verify( employeeMapper, times( 1 ) ).employeeToEmployeeRefDTO( employee );
    }

    @Test
    @DisplayName("createEmployee should throw when user does not exist")
    void createEmployee_ShouldThrow_WhenUserDoesNotExist() {
        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .userId( "not-existing-user-id" )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();

        when( userRepository.findById( employeeCreateDTO.getUserId() ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.createEmployee( employeeCreateDTO ) );

        verify( userRepository, times( 1 ) ).findById( employeeCreateDTO.getUserId() );
    }

    @Test
    @DisplayName("createEmployee should throw when user is already linked to another employee")
    void createEmployee_ShouldThrow_WhenUserIsAlreadyLinkedToAnotherEmployee() {
        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .userId( user.getId() )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();

        when( userRepository.findById( employeeCreateDTO.getUserId() ) ).thenReturn( Optional.of( user ) );

        user.setEmployee( employee );

        assertThrows( InvalidParameterException.class, () -> service.createEmployee( employeeCreateDTO ) );

        verify( userRepository, times( 1 ) ).findById( employeeCreateDTO.getUserId() );

    }

    @Test
    @DisplayName("createEmployee should return EmployeeDTO without linked user")
    void createEmployee_ShouldReturnEmployeeDTOWithoutLinkedUser_WhenCalled() {
        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .userId( null )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();

        when( idService.generateIdFor( Employee.class ) ).thenReturn( "1" );
        when( employeeMapper.toEmployee( employeeCreateDTO, "1" ) ).thenReturn( employee );
        when( employeeRepository.save( employee ) ).thenReturn( employee );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.createEmployee( employeeCreateDTO ) );

        assertEquals( employeeDTO, actual );

        verify( idService, times( 1 ) ).generateIdFor( Employee.class );
        verify( employeeMapper, times( 1 ) ).toEmployee( employeeCreateDTO, "1" );
        verify( employeeMapper, times( 1 ) ).employeeToEmployeeDTO( employee );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }

    @Test
    @DisplayName("createEmployee should return EmployeeDTO with linked user")
    void createEmployee_ShouldReturnEmployeeDTOWithLinkedUser_WhenCalled() {
        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .userId( user.getId() )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();

        when( userRepository.findById( user.getId() ) ).thenReturn( Optional.of( user ) );
        when( idService.generateIdFor( Employee.class ) ).thenReturn( "1" );
        when( employeeMapper.toEmployee( employeeCreateDTO, "1" ) ).thenReturn( employee );
        when( employeeRepository.save( employee ) ).thenReturn( employee );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.createEmployee( employeeCreateDTO ) );

        assertEquals( employeeDTO, actual );

        verify( userRepository, times( 1 ) ).findById( user.getId() );
        verify( idService, times( 1 ) ).generateIdFor( Employee.class );
        verify( employeeMapper, times( 1 ) ).toEmployee( employeeCreateDTO, "1" );
        verify( employeeMapper, times( 1 ) ).employeeToEmployeeDTO( employee );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }

    @Test
    @DisplayName("setEmployeeUser should throw when employee does not exist")
    void setEmployeeUser_ShouldThrow_WhenEmployeeDoesNotExist() {
        when( employeeRepository.findById( "1" ) ).thenReturn( Optional.empty() );
        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder().userId( "1" ).build();
        assertThrows( EntityNotFoundException.class, () -> service.setEmployeeUser( "1", employeeUpdateUserDTO ) );

        verify( employeeRepository, times( 1 ) ).findById( "1" );
    }

    @Test
    @DisplayName("setEmployeeUser should throw when user does not exist")
    void setEmployeeUser_ShouldThrow_WhenUserDoesNotExist() {
        when( employeeRepository.findById( employee.getId() ) ).thenReturn( Optional.of( employee ) );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( "not-existing-user-id" )
                .build();

        when( userRepository.findById( employeeUpdateUserDTO.getUserId() ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.setEmployeeUser( employee.getId(), employeeUpdateUserDTO ) );

        verify( employeeRepository, times( 1 ) ).findById( employee.getId() );
        verify( userRepository, times( 1 ) ).findById( employeeUpdateUserDTO.getUserId() );
    }

    @Test
    @DisplayName("setEmployeeUser should throw when user is already linked to another employee")
    void setEmployeeUser_ShouldThrow_WhenUserIsAlreadyLinkedToAnotherEmployee() {
        when( employeeRepository.findById( employee.getId() ) ).thenReturn( Optional.of( employee ) );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( user.getId() )
                .build();

        user.setEmployee( employee );
        when( userRepository.findById( employeeUpdateUserDTO.getUserId() ) ).thenReturn( Optional.of( user ) );

        assertThrows( InvalidParameterException.class, () -> service.setEmployeeUser( employee.getId(), employeeUpdateUserDTO ) );

        verify( employeeRepository, times( 1 ) ).findById( employee.getId() );
        verify( userRepository, times( 1 ) ).findById( employeeUpdateUserDTO.getUserId() );
    }

    @Test
    @DisplayName("setEmployeeUser should return EmployeeDTO when user was unlinked")
    void setEmployeeUser_ShouldReturnEmployeeDTO_WhenUserWasUnlinked() {
        when( employeeRepository.findById( employee.getId() ) ).thenReturn( Optional.of( employee ) );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( null )
                .build();

        when( employeeRepository.save( employee ) ).thenReturn( employee );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.setEmployeeUser(
                employee.getId(), employeeUpdateUserDTO
        ) );

        assertEquals( employeeDTO, actual );

        verify( employeeRepository, times( 1 ) ).findById( employee.getId() );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }

    @Test
    @DisplayName("setEmployeeUser should return EmployeeDTO when new user was linked")
    void setEmployeeUser_ShouldReturnEmployeeDTO_WhenNewUserWasLinked() {
        when( employeeRepository.findById( employee.getId() ) ).thenReturn( Optional.of( employee ) );
        when( userRepository.findById( user.getId() ) ).thenReturn( Optional.of( user ) );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( user.getId() )
                .build();

        when( employeeRepository.save( employee ) ).thenReturn( employee );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.setEmployeeUser(
                employee.getId(), employeeUpdateUserDTO
        ) );

        assertEquals( employeeDTO, actual );

        verify( employeeRepository, times( 1 ) ).findById( employee.getId() );
        verify( userRepository, times( 1 ) ).findById( employeeUpdateUserDTO.getUserId() );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }

    @Test
    @DisplayName("updateEmployee should throw when employee does not exist")
    void updateEmployee_ShouldThrow_WhenEmployeeDoesNotExist() {
        when( employeeRepository.findById( "1" ) ).thenReturn( Optional.empty() );
        EmployeeUpdateDTO employeeUpdateDTO = EmployeeUpdateDTO.builder()
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .build();

        assertThrows( EntityNotFoundException.class, () -> service.updateEmployee( "1", employeeUpdateDTO ) );

        verify( employeeRepository, times( 1 ) ).findById( "1" );
    }

    @Test
    @DisplayName("updateEmployee should return updated EmployeeDTO when EmployeeUpdateDTO was valid")
    void updateEmployee_ShouldReturnUpdatedEmployeeDTO_WhenEmployeeUpdateDTOIsValid() {
        String id = employee.getId();
        assert id != null;
        EmployeeUpdateDTO employeeUpdateDTO = EmployeeUpdateDTO.builder()
                .firstname( "Test" )
                .lastname( "Test" )
                .build();

        when( employeeRepository.findById( id ) ).thenReturn( Optional.of( employee ) );

        when( employeeRepository.save( employee ) ).thenReturn( employee );
        employee.setFirstname( employeeUpdateDTO.getFirstname() );
        employee.setLastname( employeeUpdateDTO.getLastname() );

        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );
        employeeDTO.setFirstname( employeeUpdateDTO.getFirstname() );
        employeeDTO.setLastname( employeeUpdateDTO.getLastname() );
        when( employeeMapper.toEmployee( employeeUpdateDTO, employee ) ).thenReturn( employee );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.updateEmployee( id, employeeUpdateDTO ) );

        assertEquals( employeeDTO, actual );

        verify( employeeRepository, times( 1 ) ).findById( id );
        verify( employeeRepository, times( 1 ) ).save( employee );
        verify( employeeMapper, times( 1 ) ).employeeToEmployeeDTO( employee );
        verify( employeeMapper, times( 1 ) ).toEmployee( employeeUpdateDTO, employee );

    }


    @Test
    @DisplayName("deleteEmployee should throw when employee does not exist")
    void deleteEmployee_ShouldThrow_WhenEmployeeDoesNotExist() {
        when( employeeRepository.findById( "1" ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.deleteEmployee( "1" ) );

        verify( employeeRepository, times( 1 ) ).findById( "1" );
    }

    @Test
    @DisplayName("deleteEmployee should not throw when user exists")
    void deleteEmployee_ShouldNotThrow_WhenEmployeeExists() {
        String id = employee.getId();
        assert id != null;
        when( employeeRepository.findById( id ) ).thenReturn( Optional.of( employee ) );

        assertDoesNotThrow( () -> service.deleteEmployee( id ) );

        verify( employeeRepository, times( 1 ) ).findById( id );
        verify( employeeRepository, times( 1 ) ).delete( employee );
    }

    @Test
    @DisplayName("deleteEmployee should unlink user before deletion")
    void deleteEmployee_ShouldUnlinkUserBeforeDeletion_WhenEmployeeExists() {
        employee.setUser( user );
        user.setEmployee( null );
        String id = employee.getId();
        assert id != null;
        when( employeeRepository.findById( id ) ).thenReturn( Optional.of( employee ) );

        assertDoesNotThrow( () -> service.deleteEmployee( id ) );

        verify( employeeRepository, times( 1 ) ).findById( id );
        verify( userRepository, times( 1 ) ).save( user );
        verify( employeeRepository, times( 1 ) ).delete( employee );
    }
}