/*
 * assemble
 * HolidayRestController.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.holiday;

import com.assemble.backend.models.dtos.holiday.HolidayDTO;
import com.assemble.backend.models.entities.holiday.SubdivisionCode;
import com.assemble.backend.services.app.AppSettingsService;
import com.assemble.backend.services.holiday.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@AllArgsConstructor
@Tag(name = "Holidays", description = "Holiday endpoints")
public class HolidayRestController {

    private HolidayService service;
    private AppSettingsService appSettingsService;

    @Operation(
            summary = "Get Company Subdivision Code"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(
                            implementation = SubdivisionCode.class
                    )
            )
    )
    @GetMapping(
            path = "/settings",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubdivisionCode> getCompanySubdivisionCode() {
        return ResponseEntity.ok( appSettingsService.getSettings().getHolidaySubdivisionCode() );
    }

    @Operation(
            summary = "Get Holidays By Year and Subdivision Code"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = HolidayDTO.class
                            )
                    )
            )
    )
    @GetMapping(
            path = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<HolidayDTO>> getHolidaysByYearAndSubdivisionCode(
            @RequestParam String year,
            @RequestParam String subdivisionCode
    ) {
        return ResponseEntity.ok( service
                .getHolidaysByYearAndSubdivisionCode( Integer.parseInt( year ), subdivisionCode )
        );
    }

}