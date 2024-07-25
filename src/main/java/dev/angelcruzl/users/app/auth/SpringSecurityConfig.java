package dev.angelcruzl.users.app.auth;

import dev.angelcruzl.users.app.auth.filter.JwtAuthFilter;
import dev.angelcruzl.users.app.auth.filter.JwtValidationFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@AllArgsConstructor
public class SpringSecurityConfig {

    public static final String USERS_URL = "/api/users";
    public static final String USERS_ID_URL = "/api/users/{id}";
    public static final String USERS_ID_PASSWORD_URL = "/api/users/{id}/password";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .addFilter(new JwtAuthFilter(authenticationManager()))
            .addFilter(new JwtValidationFilter(authenticationManager()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> filter = new FilterRegistrationBean<>(
            new CorsFilter(this.corsConfigurationSource()));
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return filter;
    }

}
