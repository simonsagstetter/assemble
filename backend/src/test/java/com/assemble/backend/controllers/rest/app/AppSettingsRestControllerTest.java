/*
 * assemble
 * AppSettingsRestControllerTest.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.app;

import com.assemble.backend.models.dtos.app.AppSettingsDTO;
import com.assemble.backend.models.entities.app.AppSettings;
import com.assemble.backend.models.entities.auth.UserRole;
import com.assemble.backend.models.entities.holiday.SubdivisionCode;
import com.assemble.backend.repositories.app.AppSettingsRepository;
import com.assemble.backend.testcontainers.TestcontainersConfiguration;
import com.assemble.backend.testutils.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@DisplayName("AppSettingsRestController Integration Test")
class AppSettingsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppSettingsRepository appSettingsRepository;

    @Test
    @DisplayName("/GET getAppSettings should return status code 200")
    @WithMockCustomUser(roles = { UserRole.SUPERUSER })
    void getAppSettings_ShouldReturnStatusCode200_WhenCalled() throws Exception {
        mockMvc.perform(
                get( "/api/admin/settings" )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.holidaySubdivisionCode" ).value( SubdivisionCode.BE.toString() )
        );
    }

    @Test
    @DisplayName("/PUT updateAppSettings should return status code 400 if request body invalid")
    @WithMockCustomUser(roles = { UserRole.SUPERUSER })
    void updateAppSettings_ShouldReturnStatusCode400_WhenRequestBodyInvalid() throws Exception {
        AppSettings currentSettings = appSettingsRepository.getSingleton();
        AppSettingsDTO dto = AppSettingsDTO.builder()
                .companyName( "    " )
                .companyAddress( currentSettings.getCompanyAddress() )
                .holidaySubdivisionCode( SubdivisionCode.BY )
                .build();

        String jsonContent = new ObjectMapper().writeValueAsString( dto );

        mockMvc.perform(
                put( "/api/admin/settings" )
                        .content( jsonContent )
                        .contentType( MediaType.APPLICATION_JSON )
                        .with( csrf() )
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.message" ).isNotEmpty()
        );
    }

    @Test
    @DisplayName("/PUT updateAppSettings should return status code 200 if request body valid")
    @WithMockCustomUser(roles = { UserRole.SUPERUSER })
    void updateAppSettings_ShouldReturnStatusCode200_WhenRequestBodyIsValid() throws Exception {
        AppSettings currentSettings = appSettingsRepository.getSingleton();
        AppSettingsDTO dto = AppSettingsDTO.builder()
                .companyName( "Assemble.com" )
                .companyAddress( currentSettings.getCompanyAddress() )
                .holidaySubdivisionCode( SubdivisionCode.BY )
                .build();

        String jsonContent = new ObjectMapper().writeValueAsString( dto );

        mockMvc.perform(
                put( "/api/admin/settings" )
                        .content( jsonContent )
                        .contentType( MediaType.APPLICATION_JSON )
                        .with( csrf() )
        ).andExpect(
                status().isOk()
        ).andExpect(
                content().contentType( MediaType.APPLICATION_JSON )
        ).andExpect(
                jsonPath( "$.holidaySubdivisionCode" ).value( SubdivisionCode.BY.toString() )
        ).andExpect(
                jsonPath( "$.companyName" ).value( dto.getCompanyName() )
        );
    }

}