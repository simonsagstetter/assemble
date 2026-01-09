/*
 * assemble
 * UserAdminServiceImplTest.java
 *
 * Copyright (c) 2025 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.services.auth;

import com.assemble.backend.models.dtos.auth.admin.*;
import com.assemble.backend.models.dtos.employee.EmployeeRefDTO;
import com.assemble.backend.models.entities.auth.User;
import com.assemble.backend.models.entities.auth.UserAudit;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.employee.Employee;
import com.assemble.backend.models.mappers.auth.UserAdminMapper;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("UserAdminServiceIml Unit Test")
@ExtendWith(MockitoExtension.class)
class UserAdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAdminMapper userAdminMapper;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private SessionService sessionService;

    @InjectMocks
    private UserAdminServiceImpl userAdminServiceImpl;

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private static User user;
    private static UserAdminDTO userAdminDTO;
    private static UUID uuid;
    private static UUID notExistingUUID;

    @BeforeAll
    static void setUp() {
        uuid = UuidCreator.getTimeOrderedEpoch();
        notExistingUUID = UuidCreator.getTimeOrderedEpoch();

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

        userAdminDTO = UserAdminDTO.builder()
                .id( uuid.toString() )
                .username( "testuser" )
                .firstname( "Test" )
                .lastname( "User" )
                .email( "test@example.com" )
                .roles( List.of( UserRole.USER ) )
                .enabled( true )
                .locked( false )
                .createdDate( user.getCreatedDate() )
                .lastModifiedDate( user.getLastModifiedDate() )
                .createdBy( user.getCreatedBy() )
                .lastModifiedBy( user.getLastModifiedBy() )
                .employee( null )
                .fullname( user.getFullname() )
                .build();
    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset( userRepository, userAdminMapper, employeeRepository, passwordEncoder, sessionService );
        user.setEmployee( null );
        userAdminDTO.setEmployee( null );
    }

    @Test
    @DisplayName("getAllUsers should return an empty list when no users in database")
    void getAllUsers_ShouldReturnAnEmptyList_WhenNoUsersInDatabase() {
        when( userRepository.findAll() ).thenReturn( List.of() );

        List<UserAdminDTO> users = userAdminServiceImpl.getAllUsers();

        assertThat( users ).isEmpty();

        verify( userRepository, times( 1 ) ).findAll();
    }

    @Test
    @DisplayName("getAllUsers should return a list containing one user")
    void getAllUsers_ShouldReturnAListContainingOneUser() {
        when( userRepository.findAll() ).thenReturn( List.of( user ) );
        when( userAdminMapper.toUserAdminDTO( user ) ).thenReturn( userAdminDTO );

        List<UserAdminDTO> users = userAdminServiceImpl.getAllUsers();

        assertThat( users )
                .isNotEmpty()
                .hasSize( 1 )
                .containsExactly( userAdminDTO );

        verify( userRepository, times( 1 ) ).findAll();
    }

    @Test
    @DisplayName("getUserById should throw EntityNotFoundException when user does not exist")
    void getUserById_ShouldThrowEntityNotFoundException_WhenUserDoesNotExist() {
        when( userRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> userAdminServiceImpl.getUserById( notExistingUUID.toString() ) );

        verify( userRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("searchUnlinkedUsers should return a list of User when search term had a match")
    void searchUnlinkedUsers_ShouldReturnListOfUsers_WhenSearchTermHadAMatch() {
        String searchTerm = user.getFirstname().toLowerCase();
        UserRefDTO userRefDTO = UserRefDTO.builder()
                .id( uuid.toString() )
                .username( user.getUsername() )
                .build();

        when( userRepository.search( searchTerm ) ).thenReturn( List.of( user ) );
        when( userAdminMapper.userToUserRefDTO( user ) ).thenReturn( userRefDTO );

        List<UserRefDTO> actual = userAdminServiceImpl.searchUnlinkedUsers( searchTerm );

        assertThat( actual )
                .isNotNull()
                .isNotEmpty()
                .hasSize( 1 )
                .containsExactly( userRefDTO );

        verify( userRepository, times( 1 ) ).search( searchTerm );
    }

    @Test
    @DisplayName("searchUnlinkedUsers should return empty list when search term had no match")
    void searchUnlinkedUsers_ShouldReturnEmptyList_WhenSearchTermHadNoMatch() {
        String searchTerm = "not-existing-user";
        when( userRepository.search( searchTerm ) ).thenReturn( List.of() );

        List<UserRefDTO> actual = userAdminServiceImpl.searchUnlinkedUsers( searchTerm );

        assertThat( actual )
                .isNotNull()
                .isEmpty();

        verify( userRepository, times( 1 ) ).search( searchTerm );
    }

    @Test
    @DisplayName("getUserById should return AdminUserDTO instance when user exists")
    void getUserById_ShouldReturnAdminUserDTO_WhenUserExists() {
        when( userRepository.save( user ) ).thenReturn( user );
        userRepository.save( user );
        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );
        when( userAdminMapper.toUserAdminDTO( user ) ).thenReturn( userAdminDTO );

        UserAdminDTO actual = assertDoesNotThrow( () -> userAdminServiceImpl.getUserById( uuid.toString() ) );

        assertThat( actual )
                .isNotNull()
                .isEqualTo( userAdminDTO );

        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( userRepository, times( 1 ) ).save( user );
        verify( userAdminMapper, times( 1 ) ).toUserAdminDTO( user );
    }

    @Test
    @DisplayName("createUser should throw EntityNotFoundException when invalid employeeId was passed")
    void createUser_ShouldThrowEntityNotFoundException_WhenInvalidEmployeeIdWasPassed() {
        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .employeeId( notExistingUUID.toString() )
                .username( "testuser" )
                .firstname( "Test" )
                .lastname( "User" )
                .password( "" )
                .email( "test@example.com" )
                .roles( List.of( UserRole.USER ) )
                .build();

        when( employeeRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> userAdminServiceImpl.createUser( userCreateDTO ) );

        verify( employeeRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("createUser should throw InvalidParameterException when employee is already linked")
    void createUser_ShouldThrowInvalidParameterException_WhenEmployeeIsAlreadyLinked() {
        Employee employee = Employee.builder()
                .id( uuid )
                .user( user )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();
        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.of( employee ) );

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .employeeId( uuid.toString() )
                .username( "testuser" )
                .firstname( "Test" )
                .lastname( "User" )
                .password( "" )
                .email( "test@example.com" )
                .roles( List.of( UserRole.USER ) )
                .build();

        assertThrows( InvalidParameterException.class, () -> userAdminServiceImpl.createUser( userCreateDTO ) );

        verify( employeeRepository, times( 1 ) ).findById( uuid );
    }

    @Test
    @DisplayName("createUser should return new user when dto is valid")
    void createUser_ShouldReturnNewUser_WhenDtoIsValid() {
        Employee employee = Employee.builder()
                .id( uuid )
                .user( null )
                .no( "E000001" )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .employeeId( uuid.toString() )
                .username( "testuser" )
                .firstname( "Test" )
                .lastname( "User" )
                .password( "secret" )
                .email( "test@example.com" )
                .roles( List.of( UserRole.USER ) )
                .build();

        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.of( employee ) );
        when( passwordEncoder.encode( userCreateDTO.getPassword() ) ).thenReturn( "secret" );
        user.setEmployee( employee );
        when( userAdminMapper.toUser( userCreateDTO ) ).thenReturn( user );
        when( userRepository.save( user ) ).thenReturn( user );
        userAdminDTO.setEmployee( EmployeeRefDTO.builder()
                .id( uuid.toString() )
                .fullname( employee.getFullname() )
                .no( employee.getNo() )
                .build()
        );
        when( userAdminMapper.toUserAdminDTO( user ) ).thenReturn( userAdminDTO );
        when( userRepository.save( user ) ).thenReturn( user );

        UserAdminDTO actual = assertDoesNotThrow( () -> userAdminServiceImpl.createUser( userCreateDTO ) );

        assertThat( actual ).isEqualTo( userAdminDTO );

        verify( employeeRepository, times( 1 ) ).findById( uuid );
        verify( passwordEncoder, times( 1 ) ).encode( userCreateDTO.getPassword() );
        verify( userAdminMapper, times( 1 ) ).toUser( userCreateDTO );
        verify( userRepository, times( 1 ) ).save( user );
        verify( userAdminMapper, times( 1 ) ).toUserAdminDTO( user );
        verify( userRepository, times( 1 ) ).save( user );
    }

    @Test
    @DisplayName("createUser should return new user with generated password when dto is valid")
    void createUser_ShouldReturnNewUserWithGeneratedPassword_WhenDtoIsValid() {
        Employee employee = Employee.builder()
                .id( uuid )
                .user( null )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .no( "E000001" )
                .build();

        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .employeeId( uuid.toString() )
                .username( "testuser" )
                .firstname( "Test" )
                .lastname( "User" )
                .email( "test@example.com" )
                .roles( List.of( UserRole.USER ) )
                .build();

        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.of( employee ) );
        when( passwordEncoder.encode( anyString() ) ).thenReturn( "UUID" );
        user.setEmployee( employee );
        when( userAdminMapper.toUser( userCreateDTO ) ).thenReturn( user );
        when( userRepository.save( user ) ).thenReturn( user );
        userAdminDTO.setEmployee( EmployeeRefDTO.builder().id( uuid.toString() ).fullname( employee.getFullname() ).no( employee.getNo() ).build() );
        when( userAdminMapper.toUserAdminDTO( user ) ).thenReturn( userAdminDTO );
        when( userRepository.save( user ) ).thenReturn( user );

        UserAdminDTO actual = assertDoesNotThrow( () -> userAdminServiceImpl.createUser( userCreateDTO ) );

        assertThat( actual ).isEqualTo( userAdminDTO );

        verify( employeeRepository, times( 1 ) ).findById( uuid );
        verify( passwordEncoder, times( 1 ) ).encode( anyString() );
        verify( userAdminMapper, times( 1 ) ).toUser( userCreateDTO );
        verify( userRepository, times( 1 ) ).save( user );
        verify( userAdminMapper, times( 1 ) ).toUserAdminDTO( user );
        verify( userRepository, times( 1 ) ).save( user );
    }

    @Test
    @DisplayName("setUserStatus should throw EntityNotFoundException when user does not exist")
    void setUserStatus_ShouldThrowEntityNotFoundException_WhenUserDoesNotExist() {
        when( userRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> userAdminServiceImpl.setUserStatus(
                        notExistingUUID.toString(),
                        UserUpdateStatusDTO.builder()
                                .enabled( true )
                                .locked( false )
                                .build()
                )
        );

        verify( userRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("setUserStatus should update UserRoles when DTO is valid")
    void setUserStatus_ShouldUpdateUserAdminDTO_WhenDtoIsValid() {
        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );
        userAdminDTO.setEnabled( true );
        userAdminDTO.setLocked( true );

        when( userRepository.save( user ) ).thenReturn( user );
        when( userAdminMapper.toUserAdminDTO( user ) ).thenReturn( userAdminDTO );

        UserUpdateStatusDTO userUpdateStatusDTO = UserUpdateStatusDTO.builder()
                .enabled( true )
                .locked( true )
                .build();

        UserAdminDTO actual = assertDoesNotThrow( () -> userAdminServiceImpl.setUserStatus(
                        uuid.toString(),
                        userUpdateStatusDTO
                )
        );

        assertThat( actual ).isEqualTo( userAdminDTO );

        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( userRepository, times( 1 ) ).save( user );
    }

    @Test
    @DisplayName("setUserRoles should throw EntityNotFoundException when user does not exist")
    void setUserRoles_ShouldThrowEntityNotFoundException_WhenUserDoesNotExist() {
        when( userRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> userAdminServiceImpl.setUserRoles(
                        notExistingUUID.toString(),
                        List.of( UserRole.USER )
                )
        );

        verify( userRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("setUserRoles should update UserRoles when DTO is valid")
    void setUserRoles_ShouldUpdateUserAdminDTO_WhenDtoIsValid() {
        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );
        List<UserRole> roles = List.of( UserRole.USER, UserRole.ADMIN );

        userAdminDTO.setRoles( roles );

        when( userRepository.save( user ) ).thenReturn( user );
        when( userAdminMapper.toUserAdminDTO( user ) ).thenReturn( userAdminDTO );

        UserAdminDTO actual = assertDoesNotThrow( () -> userAdminServiceImpl.setUserRoles(
                        uuid.toString(),
                        roles
                )
        );

        assertThat( actual ).isEqualTo( userAdminDTO );

        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( userRepository, times( 1 ) ).save( user );
    }

    @Test
    @DisplayName("setUserEmployee should throw EntityNotFoundException when user does not exist")
    void setUserEmployee_ShouldThrowEntityNotFoundException_WhenUserDoesNotExist() {
        when( userRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> userAdminServiceImpl.setUserEmployee(
                        notExistingUUID.toString(),
                        UserUpdateEmployeeDTO.builder().employeeId( "test" ).build()
                )
        );

        verify( userRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("setUserEmployee should throw EntityNotFoundException when employee does not exist")
    void setUserEmployee_ShouldThrowEntityNotFoundException_WhenEmployeeDoesNotExist() {
        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );

        UserUpdateEmployeeDTO userUpdateEmployeeDTO = UserUpdateEmployeeDTO.builder()
                .employeeId( notExistingUUID.toString() )
                .build();

        assertThrows( EntityNotFoundException.class, () -> userAdminServiceImpl.setUserEmployee( uuid.toString(), userUpdateEmployeeDTO ) );

        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( employeeRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("setUserEmployee should throw InvalidParamterException when employee is already linked")
    void setUserEmployee_ShouldThrowInvalidParamterException_WhenEmployeeIsAlreadyLinked() {
        Employee employee = Employee.builder()
                .id( uuid )
                .user( user )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();
        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.of( employee ) );


        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );

        user.setEmployee( employee );

        UserUpdateEmployeeDTO userUpdateEmployeeDTO = UserUpdateEmployeeDTO.builder()
                .employeeId( uuid.toString() )
                .build();

        assertThrows( InvalidParameterException.class, () -> userAdminServiceImpl.setUserEmployee( uuid.toString(), userUpdateEmployeeDTO ) );

        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( employeeRepository, times( 1 ) ).findById( uuid );
    }

    @Test
    @DisplayName("setUserEmployee should return UserAdminDTO with Valid DTO and no employee linked")
    void setUserEmployee_ShouldReturnUserAdminDTOWithValidDtoAndNoEmployeeLinked() {
        Employee employee = Employee.builder()
                .id( uuid )
                .user( user )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();

        user.setEmployee( employee );

        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );
        when( employeeRepository.save( employee ) ).thenReturn( employee );
        when( userAdminMapper.toUserAdminDTO( user ) ).thenReturn( userAdminDTO );

        UserUpdateEmployeeDTO userUpdateEmployeeDTO = UserUpdateEmployeeDTO.builder()
                .employeeId( null )
                .build();

        UserAdminDTO actual = assertDoesNotThrow( () -> userAdminServiceImpl.setUserEmployee( uuid.toString(), userUpdateEmployeeDTO ) );

        assertThat( actual ).isEqualTo( userAdminDTO );
        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }

    @Test
    @DisplayName("setUserEmployee should return UserAdminDTO with Valid DTO and new employee linked")
    void setUserEmployee_ShouldReturnUserAdminDTOWithValidDtoAndNewEmployeeLinked() {
        Employee employee = Employee.builder()
                .id( uuid )
                .user( null )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .no( "E000001" )
                .build();

        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );

        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.of( employee ) );
        when( employeeRepository.save( employee ) ).thenReturn( employee );
        userAdminDTO.setEmployee( EmployeeRefDTO.builder().id( uuid.toString() ).fullname( employee.getFullname() ).no( employee.getNo() ).build() );
        when( userAdminMapper.toUserAdminDTO( user ) ).thenReturn( userAdminDTO );

        UserUpdateEmployeeDTO userUpdateEmployeeDTO = UserUpdateEmployeeDTO.builder()
                .employeeId( uuid.toString() )
                .build();

        UserAdminDTO actual = assertDoesNotThrow( () -> userAdminServiceImpl.setUserEmployee( uuid.toString(), userUpdateEmployeeDTO ) );

        assertThat( actual ).isEqualTo( userAdminDTO );
        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( employeeRepository, times( 1 ) ).findById( uuid );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }

    @Test
    @DisplayName("setUserEmployee should return UserAdminDTO, unlink old employee and link new employee")
    void setUserEmployee_ShouldReturnUserAdminDTOWithValidDtoAndUnlinkOldEmployeeAndLinkNewEmployee() {
        Employee oldEmployee = Employee.builder()
                .id( uuid )
                .user( null )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .build();

        user.setEmployee( oldEmployee );

        Employee newEmployee = Employee.builder()
                .id( uuid )
                .user( null )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@example.com" )
                .no( "E000001" )
                .build();


        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );

        when( employeeRepository.findById( uuid ) ).thenReturn( Optional.of( newEmployee ) );

        when( employeeRepository.save( oldEmployee ) ).thenReturn( oldEmployee );
        when( employeeRepository.save( newEmployee ) ).thenReturn( newEmployee );
        userAdminDTO.setEmployee( EmployeeRefDTO.builder().id( uuid.toString() ).fullname( newEmployee.getFullname() ).no( newEmployee.getNo() ).build() );
        when( userAdminMapper.toUserAdminDTO( user ) ).thenReturn( userAdminDTO );

        UserUpdateEmployeeDTO userUpdateEmployeeDTO = UserUpdateEmployeeDTO.builder()
                .employeeId( uuid.toString() )
                .build();

        UserAdminDTO actual = assertDoesNotThrow( () -> userAdminServiceImpl.setUserEmployee( uuid.toString(), userUpdateEmployeeDTO ) );

        assertThat( actual ).isEqualTo( userAdminDTO );
        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( employeeRepository, times( 1 ) ).findById( uuid );
        verify( employeeRepository, times( 1 ) ).save( newEmployee );
        verify( employeeRepository, times( 1 ) ).save( oldEmployee );
    }

    @Test
    @DisplayName("setUserPassword should throw EntityNotFoundException when user does not exist")
    void setUserPassword_ShouldThrowEntityNotFoundException_WhenUserDoesNotExist() {
        when( userRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> userAdminServiceImpl.setUserPassword(
                        notExistingUUID.toString(),
                        "test",
                        false
                )
        );

        verify( userRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("setUserPassword should change password")
    void setUserPassword_ShouldChangePassword_WhenCalled() {
        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );
        when( passwordEncoder.encode( "test" ) ).thenReturn( "encodede-test" );
        assertDoesNotThrow( () -> userAdminServiceImpl.setUserPassword(
                        uuid.toString(),
                        "test",
                        false
                )
        );

        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( passwordEncoder, times( 1 ) ).encode( "test" );
    }

    @Test
    @DisplayName("setUserPassword should invalidate sessions when called")
    void setUserPassword_ShouldInvalidateSessions_WhenCalled() {
        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );
        when( passwordEncoder.encode( "test" ) ).thenReturn( "encodede-test" );

        assertDoesNotThrow( () -> userAdminServiceImpl.setUserPassword(
                        uuid.toString(),
                        "test",
                        true
                )
        );

        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( passwordEncoder, times( 1 ) ).encode( "test" );
        verify( sessionService, times( 1 ) ).invalidateUserSessions( user.getUsername() );
    }

    @Test
    @DisplayName("updateUser should throw EntityNotFoundException when user does not exist")
    void updateUser_ShouldThrowEntityNotFoundException_WhenUserDoesNotExist() {
        when( userRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> userAdminServiceImpl.updateUser(
                        notExistingUUID.toString(),
                        UserUpdateDTO.builder().build()
                )
        );

        verify( userRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("updateUser should return UserAdminDTO when UserUpdateDTO is valid")
    void updateUser_ShouldReturnUserAdminDTO_WhenUserUpdateDTOIsValid() {
        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .email( "test@updated.com" )
                .build();

        assert userUpdateDTO.getEmail() != null;

        user.setEmail( userUpdateDTO.getEmail() );
        when( userAdminMapper.toUser( userUpdateDTO, user ) ).thenReturn( user );
        when( userRepository.save( user ) ).thenReturn( user );
        userAdminDTO.setEmail( userUpdateDTO.getEmail() );
        when( userAdminMapper.toUserAdminDTO( user ) ).thenReturn( userAdminDTO );

        UserAdminDTO actual = assertDoesNotThrow( () -> userAdminServiceImpl.updateUser(
                        uuid.toString(),
                        userUpdateDTO
                )
        );

        assertThat( actual ).isEqualTo( userAdminDTO );

        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( userAdminMapper, times( 1 ) ).toUser( userUpdateDTO, user );
        verify( userRepository, times( 1 ) ).save( user );
        verify( userAdminMapper, times( 1 ) ).toUserAdminDTO( user );
    }

    @Test
    @DisplayName("deleteUser should delete user when user exists")
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );

        assertDoesNotThrow( () -> userAdminServiceImpl.deleteUser( uuid.toString() ) );

        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( userRepository, times( 1 ) ).delete( user );
    }

    @Test
    @DisplayName("deleteUser should throw EntityNotFoundException when user does not exist")
    void deleteUser_ShouldThrowEntityNotFoundException_WhenUserDoesNotExist() {
        when( userRepository.findById( notExistingUUID ) ).thenReturn( Optional.empty() );

        assertThrows( EntityNotFoundException.class, () -> userAdminServiceImpl.deleteUser( notExistingUUID.toString() ) );

        verify( userRepository, times( 1 ) ).findById( notExistingUUID );
    }

    @Test
    @DisplayName("deleteUser should delete user and update employee relationship when user exists")
    void deleteUser_ShouldDeleteUserAndUpdateEmployeeRelationship_WhenUserExists() {
        Employee employee = Employee.builder()
                .id( uuid )
                .user( user )
                .firstname( "Max" )
                .lastname( "Mustermann" )
                .email( "max.mustermann@mail.de" )
                .build();

        user.setEmployee( employee );


        when( userRepository.findById( uuid ) ).thenReturn( Optional.of( user ) );
        when( employeeRepository.save( employee ) ).thenReturn( employee );

        assertDoesNotThrow( () -> userAdminServiceImpl.deleteUser( uuid.toString() ) );

        verify( userRepository, times( 1 ) ).findById( uuid );
        verify( userRepository, times( 1 ) ).delete( user );
        verify( employeeRepository, times( 1 ) ).save( employee );
    }
}