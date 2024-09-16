package org.lifemanager.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="_user")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String firstname ;
    private String lastname;
    @JsonIgnore
    private String password;
    @Column(name = "verification_code")
    @JsonIgnore
    private String verificationCode;
    @Column(name = "verification_expiration")
    @JsonIgnore
    private LocalDateTime verificationCodeExpiresAt;
    private boolean enabled;
//    @OneToMany(mappedBy = "user" , fetch=FetchType.LAZY)
//    @JsonIgnore
//    private List<Token> tokens;
}
