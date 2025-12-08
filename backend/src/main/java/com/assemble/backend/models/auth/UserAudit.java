package com.assemble.backend.models.auth;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
@ToString
@EqualsAndHashCode
public class UserAudit implements Serializable {

    private String id;
    private String username;

}
