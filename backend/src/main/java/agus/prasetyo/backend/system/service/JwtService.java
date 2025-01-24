package agus.prasetyo.backend.system.service;

import agus.prasetyo.backend.apps.model.db.User;
import agus.prasetyo.backend.apps.repository.UserRepository;
import agus.prasetyo.backend.system.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public UUID getUserIdToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            User userData = userRepository.findByUsername(jwtUtil.extractUsername(token)).get();
            return userData.getId();
        }
        return null;
    }

    public User getUserToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            User userData = userRepository.findByUsername(jwtUtil.extractUsername(token)).get();
            return userData;
        }
        return null;
    }

}

