package com.fraus.spring.user.web;

import com.fraus.spring.user.repository.RoleRepository;
import com.fraus.spring.user.repository.UserRepository;
import com.fraus.spring.user.repository.entity.Role;
import com.fraus.spring.user.repository.entity.User;
import com.fraus.spring.user.repository.entity.UserRole;
import com.fraus.spring.user.security.JwtUtils;
import com.fraus.spring.user.security.UserDetailsImpl;
import com.fraus.spring.user.web.Dto.JwtResponse;
import com.fraus.spring.user.web.Dto.LoginRequest;
import com.fraus.spring.user.web.Dto.SignupRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Controller authenticateUser is called");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(),
                        loginRequest.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        log.info("Controller registerUser is called");

        if (userRepository.existsByUsername(signupRequest.username())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        if (userRepository.existsByEmail(signupRequest.email())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        Role role = roleRepository.findRoleByName(UserRole.USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User(
                null,
                signupRequest.username(),
                signupRequest.email(),
                encoder.encode(signupRequest.password()),
                Set.of(role)
        );

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
