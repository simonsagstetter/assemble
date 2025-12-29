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
import com.github.f4b6a3.uuid.UuidCreator;
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
import java.util.UUID;

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

    @InjectMocks
    private EmployeeServiceImpl service;

    private static Employee employee;

    private static EmployeeDTO employeeDTO;

    private static User user;
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private static UUID uuid;
    private static UUID notExistingUUID;

    @BeforeAll
    static void setUp() {
        uuid = UuidCreator.getTimeOrderedEpoch();
        notExistingUUID = UuidCreator.getTimeOrderedEpoch();
        Instant now = Instant.now();
        UserAudit userAudit = new UserAudit( null, "SYSTEM" );
        employee = Employee.builder()
                .id( uuid )
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

        employeeDTO = EmployeeDTO.builder()
                .id( uuid.toString() )
                .no( "E000001" )
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
                .id( uuid )
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
        reset( employeeRepository, userRepository, employeeMapper );
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

        when( employeeRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.getEmployeeById( notExistingUUID.toString() ) );

        verify( employeeRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("getEmployeeById should return EmployeeDTO when employee exists")
    void getEmployeeById_ShouldReturnEmployeeDTO_WhenEmployeeExists() {
        assert employee.getId() != null;
        UUID id = employee.getId();
        when( employeeRepository.findById( id ) ).thenReturn( Optional.of( employee ) );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.getEmployeeById( id.toString() ) );

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
                .id( uuid.toString() )
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
                .userId( notExistingUUID.toString() )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();

        assert employeeCreateDTO.getUserId() != null;
        UUID userId = UUID.fromString( employeeCreateDTO.getUserId() );

        when( userRepository.findById( userId ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.createEmployee( employeeCreateDTO ) );

        verify( userRepository, times( 1 ) ).findById( userId );
    }

    @Test
    @DisplayName("createEmployee should throw when user is already linked to another employee")
    void createEmployee_ShouldThrow_WhenUserIsAlreadyLinkedToAnotherEmployee() {
        assert user.getId() != null;
        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .userId( user.getId().toString() )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();

        assert employeeCreateDTO.getUserId() != null;
        UUID userId = UUID.fromString( employeeCreateDTO.getUserId() );

        when( userRepository.findById( userId ) ).thenReturn( Optional.of( user ) );

        user.setEmployee( employee );

        assertThrows( InvalidParameterException.class, () -> service.createEmployee( employeeCreateDTO ) );

        verify( userRepository, times( 1 ) ).findById( userId );

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

        when( employeeMapper.toEmployee( employeeCreateDTO ) ).thenReturn( employee );
        when( employeeRepository.save( employee ) ).thenReturn( employee );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.createEmployee( employeeCreateDTO ) );

        assertEquals( employeeDTO, actual );

        verify( employeeMapper, times( 1 ) ).toEmployee( employeeCreateDTO );
        verify( employeeMapper, times( 1 ) ).employeeToEmployeeDTO( employee );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }

    @Test
    @DisplayName("createEmployee should return EmployeeDTO with linked user")
    void createEmployee_ShouldReturnEmployeeDTOWithLinkedUser_WhenCalled() {
        assert user.getId() != null;
        EmployeeCreateDTO employeeCreateDTO = EmployeeCreateDTO.builder()
                .userId( user.getId().toString() )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();

        when( userRepository.findById( user.getId() ) ).thenReturn( Optional.of( user ) );
        when( employeeMapper.toEmployee( employeeCreateDTO ) ).thenReturn( employee );
        when( employeeRepository.save( employee ) ).thenReturn( employee );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.createEmployee( employeeCreateDTO ) );

        assertEquals( employeeDTO, actual );

        verify( userRepository, times( 1 ) ).findById( user.getId() );
        verify( employeeMapper, times( 1 ) ).toEmployee( employeeCreateDTO );
        verify( employeeMapper, times( 1 ) ).employeeToEmployeeDTO( employee );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }

    @Test
    @DisplayName("setEmployeeUser should throw when employee does not exist")
    void setEmployeeUser_ShouldThrow_WhenEmployeeDoesNotExist() {
        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.empty() );
        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder().userId( uuid.toString() ).build();
        assertThrows( EntityNotFoundException.class, () -> service.setEmployeeUser( uuid.toString(), employeeUpdateUserDTO ) );

        verify( employeeRepository, times( 1 ) ).findById( uuid );
    }

    @Test
    @DisplayName("setEmployeeUser should throw when user does not exist")
    void setEmployeeUser_ShouldThrow_WhenUserDoesNotExist() {
        assert employee.getId() != null;
        UUID id = employee.getId();
        when( employeeRepository.findById( id ) ).thenReturn( Optional.of( employee ) );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( notExistingUUID.toString() )
                .build();


        when( userRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.setEmployeeUser( id.toString(), employeeUpdateUserDTO ) );

        verify( employeeRepository, times( 1 ) ).findById( id );
        verify( userRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("setEmployeeUser should throw when user is already linked to another employee")
    void setEmployeeUser_ShouldThrow_WhenUserIsAlreadyLinkedToAnotherEmployee() {
        assert employee.getId() != null;
        UUID id = employee.getId();
        when( employeeRepository.findById( id ) ).thenReturn( Optional.of( employee ) );

        assert user.getId() != null;

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( user.getId().toString() )
                .build();

        user.setEmployee( employee );
        when( userRepository.findById( user.getId() ) ).thenReturn( Optional.of( user ) );

        assertThrows( InvalidParameterException.class, () -> service.setEmployeeUser( id.toString(), employeeUpdateUserDTO ) );

        verify( employeeRepository, times( 1 ) ).findById( id );
        verify( userRepository, times( 1 ) ).findById( user.getId() );
    }

    @Test
    @DisplayName("setEmployeeUser should return EmployeeDTO when user was unlinked")
    void setEmployeeUser_ShouldReturnEmployeeDTO_WhenUserWasUnlinked() {
        assert employee.getId() != null;
        UUID id = employee.getId();
        when( employeeRepository.findById( id ) ).thenReturn( Optional.of( employee ) );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( null )
                .build();

        when( employeeRepository.save( employee ) ).thenReturn( employee );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.setEmployeeUser(
                id.toString(), employeeUpdateUserDTO
        ) );

        assertEquals( employeeDTO, actual );

        verify( employeeRepository, times( 1 ) ).findById( id );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }

    @Test
    @DisplayName("setEmployeeUser should return EmployeeDTO when new user was linked")
    void setEmployeeUser_ShouldReturnEmployeeDTO_WhenNewUserWasLinked() {

        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.of( employee ) );
        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );

        EmployeeUpdateUserDTO employeeUpdateUserDTO = EmployeeUpdateUserDTO.builder()
                .userId( uuid.toString() )
                .build();

        when( employeeRepository.save( employee ) ).thenReturn( employee );
        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.setEmployeeUser(
                uuid.toString(), employeeUpdateUserDTO
        ) );

        assertEquals( employeeDTO, actual );

        verify( employeeRepository, times( 1 ) ).findById( uuid );
        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }

    @Test
    @DisplayName("updateEmployee should throw when employee does not exist")
    void updateEmployee_ShouldThrow_WhenEmployeeDoesNotExist() {
        when( employeeRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );
        EmployeeUpdateDTO employeeUpdateDTO = EmployeeUpdateDTO.builder()
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .build();

        assertThrows( EntityNotFoundException.class, () -> service.updateEmployee( notExistingUUID.toString(), employeeUpdateDTO ) );

        verify( employeeRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("updateEmployee should return updated EmployeeDTO when EmployeeUpdateDTO was valid")
    void updateEmployee_ShouldReturnUpdatedEmployeeDTO_WhenEmployeeUpdateDTOIsValid() {
        EmployeeUpdateDTO employeeUpdateDTO = EmployeeUpdateDTO.builder()
                .firstname( "Test" )
                .lastname( "Test" )
                .build();

        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.of( employee ) );

        when( employeeRepository.save( employee ) ).thenReturn( employee );
        employee.setFirstname( employeeUpdateDTO.getFirstname() );
        employee.setLastname( employeeUpdateDTO.getLastname() );

        when( employeeMapper.employeeToEmployeeDTO( employee ) ).thenReturn( employeeDTO );
        employeeDTO.setFirstname( employeeUpdateDTO.getFirstname() );
        employeeDTO.setLastname( employeeUpdateDTO.getLastname() );
        when( employeeMapper.toEmployee( employeeUpdateDTO, employee ) ).thenReturn( employee );

        EmployeeDTO actual = assertDoesNotThrow( () -> service.updateEmployee( uuid.toString(), employeeUpdateDTO ) );

        assertEquals( employeeDTO, actual );

        verify( employeeRepository, times( 1 ) ).findById( uuid );
        verify( employeeRepository, times( 1 ) ).save( employee );
        verify( employeeMapper, times( 1 ) ).employeeToEmployeeDTO( employee );
        verify( employeeMapper, times( 1 ) ).toEmployee( employeeUpdateDTO, employee );

    }


    @Test
    @DisplayName("deleteEmployee should throw when employee does not exist")
    void deleteEmployee_ShouldThrow_WhenEmployeeDoesNotExist() {
        when( employeeRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> service.deleteEmployee( notExistingUUID.toString() ) );

        verify( employeeRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("deleteEmployee should not throw when user exists")
    void deleteEmployee_ShouldNotThrow_WhenEmployeeExists() {
        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.of( employee ) );

        assertDoesNotThrow( () -> service.deleteEmployee( uuid.toString() ) );

        verify( employeeRepository, times( 1 ) ).findById( uuid );
        verify( employeeRepository, times( 1 ) ).delete( employee );
    }

    @Test
    @DisplayName("deleteEmployee should unlink user before deletion")
    void deleteEmployee_ShouldUnlinkUserBeforeDeletion_WhenEmployeeExists() {
        employee.setUser( user );
        user.setEmployee( null );
        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.of( employee ) );

        assertDoesNotThrow( () -> service.deleteEmployee( uuid.toString() ) );

        verify( employeeRepository, times( 1 ) ).findById( uuid );
        verify( userRepository, times( 1 ) ).save( user );
        verify( employeeRepository, times( 1 ) ).delete( employee );
    }
}