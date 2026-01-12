package com.assemble.backend.models.entities.holiday;

import com.assemble.backend.models.entities.core.BaseJPAEntity;
import com.assemble.backend.models.entities.holiday.api.TemporalScope;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "holidays",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "EXTERNAL_ID"
        })
)
public class Holiday extends BaseJPAEntity {

    @Column(nullable = false, unique = true, name = "EXTERNAL_ID")
    private String externalId;

    @Column(nullable = false, name = "NAME")
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "SUBDIVISIONS", joinColumns = @JoinColumn(name = "HOLIDAY_ID"))
    private List<Subdivision> subdivisions;

    @Column(nullable = false, name = "START_DATE")
    private LocalDate startDate;

    @Column(nullable = false, name = "END_DATE")
    private LocalDate endDate;

    @Column(nullable = false, name = "TEMPORAL_SCOPE")
    @Enumerated(EnumType.STRING)
    private TemporalScope temporalScope;

    @Column(nullable = false, name = "NATION_WIDE")
    private Boolean nationWide;

}
