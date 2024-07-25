package dev.angelcruzl.users.app.auth.filter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.angelcruzl.users.app.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static dev.angelcruzl.users.app.auth.JwtConfig.*;

@AllArgsConstructor
public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {

        String username = null;
        String password = null;

        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (StreamReadException | DatabindException e) {
            log.info("Error reading user from request");
        } catch (IOException e) {
            log.error("Error reading user from request");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        Claims claims = Jwts.claims()
            .add("authorities", new ObjectMapper().writeValueAsString(roles))
            .add("username", username)
            .build();

        String jwt = Jwts.builder()
            .subject(username)
            .claims(claims)
            .signWith(SECRET_KEY)
            .issuedAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
            .compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + jwt);

        Map<String, String> body = new HashMap<>();
        body.put("token", jwt);
        body.put("username", username);
        body.put("message", "User authenticated successfully");

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        Map<String, String> body = new HashMap<>();
        body.put("message", "Authentication failed");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }
}
