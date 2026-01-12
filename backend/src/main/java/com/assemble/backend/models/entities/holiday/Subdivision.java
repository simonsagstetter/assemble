package com.assemble.backend.models.entities.holiday;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subdivision {

    @Column(nullable = false, name = "HOLIDAY_CODE")
    private String code;

    @Column(nullable = false, name = "HOLIDAY_ISO_CODE")
    private String isoCode;

    @Column(nullable = false, name = "HOLIDAY_SHORT_NAME")
    private String shortName;

    @Column(nullable = false, name = "HOLIDAY_NAME")
    private String name;

}
