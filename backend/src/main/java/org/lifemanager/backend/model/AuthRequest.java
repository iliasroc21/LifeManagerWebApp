package org.lifemanager.backend.model;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String email ;
    private String password;
}

