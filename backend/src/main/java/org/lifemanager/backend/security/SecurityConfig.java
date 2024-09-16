package org.lifemanager.backend.security;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.lifemanager.backend.repository.TokenRepository;
import org.lifemanager.backend.security.filter.AuthenticationFilter;
import org.lifemanager.backend.security.filter.ExceptionHandlerFilter;
import org.lifemanager.backend.security.filter.JwtAuthorizationFilter;
import org.lifemanager.backend.security.manager.CustomAuthenticationManager;
import org.lifemanager.backend.service.JwtService;
import org.lifemanager.backend.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Properties;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final  CustomAuthenticationManager customAuthenticationManager ;
    private final  UserService userService;
    private final JwtService jwtService;
    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;
    public SecurityConfig(@Lazy CustomAuthenticationManager authenticationManager,
                          @Lazy  UserService userService,
                          @Lazy JwtService jwtService
                          ) {
        this.customAuthenticationManager=authenticationManager;
        this.userService = userService ;
        this.jwtService = jwtService;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager , userService , jwtService);
        authenticationFilter.setFilterProcessesUrl(Constants.AUTHENTICATE_PATH);
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->
                        auth.requestMatchers(Constants.WHITE_LIST).permitAll()
                                .anyRequest().authenticated()
                ).
                addFilterBefore(new ExceptionHandlerFilter() , AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterAfter(new JwtAuthorizationFilter(jwtService , userService) , AuthenticationFilter.class)
                .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:5173"); // Your frontend's URL
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setMaxAge(3600L); // 1 hour max age for preflight
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Constants.CORS_FILTER_ORDER); // Ensure it's applied early in the filter chain
        return bean;
    }
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }
}
