package agus.prasetyo.backend.apps.controller;

import agus.prasetyo.backend.apps.model.db.User;
import agus.prasetyo.backend.apps.model.dto.UserDTO;
import agus.prasetyo.backend.apps.model.request.AuthRequest;
import agus.prasetyo.backend.apps.model.request.RegisterRequest;
import agus.prasetyo.backend.apps.model.request.VerificationRequest;
import agus.prasetyo.backend.apps.service.UserService;
import agus.prasetyo.backend.system.security.JwtUtil;
import agus.prasetyo.backend.system.service.CustomUserDetailService;
import agus.prasetyo.backend.system.service.EmailService;
import agus.prasetyo.backend.system.utils.ApiResponse;
import agus.prasetyo.backend.system.utils.DataManager;
import agus.prasetyo.backend.system.utils.ResourceNotFoundException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService userDetailService;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody AuthRequest authRequest) throws ResourceNotFoundException {
        UserDTO user = userService.checkUserAndSendOtp(authRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success, We have sent a verification code to your email.", String.valueOf(user.getId())));
    }

    @PostMapping("/verification")
    public ResponseEntity<ApiResponse<String>> verification(@RequestBody VerificationRequest verificationRequest) throws ResourceNotFoundException {

        User user = userService.verifyOpt(verificationRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), DataManager.getInstance().get("pass", String.class))
        );
        UserDetails userDetail = userDetailService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetail);
        // Remove Data manager
        DataManager.getInstance().remove("pass");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Success", token));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody RegisterRequest registerRequest) {
        UserDTO savedUser = userService.create(registerRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "User created successfully", savedUser));
    }
}
