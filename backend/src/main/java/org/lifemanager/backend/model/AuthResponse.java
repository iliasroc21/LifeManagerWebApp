package org.lifemanager.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lifemanager.backend.entity.User;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken ;
    private User user ;
}

