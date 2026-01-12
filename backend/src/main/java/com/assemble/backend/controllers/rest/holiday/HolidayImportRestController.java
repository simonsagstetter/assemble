/*
 * assemble
 * HolidayImportRestController.java
 *
 * Copyright (c) 2026 Simon Sagstetter
 *
 * This software is the property of Simon Sagstetter.
 * All rights reserved.
 */

package com.assemble.backend.controllers.rest.holiday;

import com.assemble.backend.models.dtos.holiday.HolidayDTO;
import com.assemble.backend.models.entities.holiday.Holiday;
import com.assemble.backend.services.holiday.HolidayImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays/import")
@PreAuthorize("hasRole('ADMIN')||hasRole('SUPERUSER')")
@AllArgsConstructor
@Tag(name = "HolidayImport", description = "Holiday import endpoints")
public class HolidayImportRestController {

    private HolidayImportService service;

    @Operation(
            summary = "Get imported years"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(
                            schema = @Schema(
                                    implementation = Integer.class
                            )
                    )
            )
    )
    @GetMapping(
            path = "/years",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Integer>> getImportedYears() {
        return ResponseEntity.ok( service.getImportedYears() );
    }

    @Operation(
            summary = "Import Holidays for a given year"
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
    @PostMapping(
            path = "/{year}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Holiday>> getHolidaysFromYear(
            @PathVariable String year
    ) {
        return ResponseEntity.ok( service.importHolidaysByYear( Integer.parseInt( year ) ) );
    }

    @Operation(
            summary = "Delete Holidays for a given year"
    )
    @ApiResponse(
            responseCode = "204",
            description = "No Content"
    )
    @DeleteMapping("/{year}")
    public ResponseEntity<Void> deleteHolidaysFromYear(
            @PathVariable String year
    ) {
        service.deleteHolidaysByYear( Integer.parseInt( year ) );
        return ResponseEntity.noContent().build();
    }
}
