package dev.angelcruzl.users.app.auth;

import dev.angelcruzl.users.app.auth.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    public static final String USERS_URL = "/api/users";
    public static final String USERS_ID_URL = "/api/users/{id}";
    public static final String USERS_ID_PASSWORD_URL = "/api/users/{id}/password";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, USERS_URL).permitAll()
                .requestMatchers(HttpMethod.GET, USERS_ID_URL).hasAnyRole(ADMIN_ROLE, USER_ROLE)
                .requestMatchers(HttpMethod.POST, USERS_URL).hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.PATCH, USERS_ID_PASSWORD_URL).hasAnyRole(ADMIN_ROLE, USER_ROLE)
                .requestMatchers(HttpMethod.PUT, USERS_ID_URL).hasRole(ADMIN_ROLE)
                .requestMatchers(HttpMethod.DELETE, USERS_ID_URL).hasRole(ADMIN_ROLE)
                .anyRequest().authenticated())
            .addFilter(new JwtAuthFilter(null))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

}
