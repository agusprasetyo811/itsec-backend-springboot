package agus.prasetyo.backend.apps.service;

import agus.prasetyo.backend.apps.model.db.Role;
import agus.prasetyo.backend.apps.model.db.User;
import agus.prasetyo.backend.apps.model.dto.UserDTO;
import agus.prasetyo.backend.apps.model.request.AuthRequest;
import agus.prasetyo.backend.apps.model.request.RegisterRequest;
import agus.prasetyo.backend.apps.model.request.VerificationRequest;
import agus.prasetyo.backend.apps.repository.RoleRepository;
import agus.prasetyo.backend.apps.repository.UserRepository;
import agus.prasetyo.backend.apps.repository.UserRoleRepository;
import agus.prasetyo.backend.system.service.EmailService;
import agus.prasetyo.backend.system.utils.DataManager;
import agus.prasetyo.backend.system.utils.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@Service
public class UserService {

    @Value("${spring.mail.username}")
    private String mailsender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserDTO checkUserAndSendOtp(AuthRequest authRequest) throws ResourceNotFoundException {

        Optional<User> optionalUser = userRepository.findByUsername(authRequest.getUsername());
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException("User not found with username: " + authRequest.getUsername());
        }
        User user = optionalUser.get();

        boolean isMatch = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        if (!isMatch) {
            throw new ResourceNotFoundException("Invalid password for user: " + authRequest.getUsername());
        }

        DataManager.getInstance().set("pass", authRequest.getPassword());

        Random random = new Random();
        int randomSixDigit = 100000 + random.nextInt(900000);

        user.setOtp(String.valueOf(randomSixDigit));
        userRepository.save(user);

        emailService.sendEmail(
                mailsender,
                user.getEmail(),
                "OTP",
                "Your OTP is: " + randomSixDigit
        );

        return modelMapper.map(user, UserDTO.class);
    }


    public User verifyOpt(VerificationRequest verificationRequest) throws ResourceNotFoundException {
        return userRepository.findByIdAndOtp(verificationRequest.getId(), verificationRequest.getOtp()).orElseThrow(() -> new ResourceNotFoundException("Verificatio failed with ID: " + verificationRequest.getId()));
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "all_user")
    public List<UserDTO> findAll(String sortBy, String sortDirection) {
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = sortDirection.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
        }

        // Fetch data dari repository dengan sorting
        return userRepository.findAll(sort).stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    // @Cacheable(value = "detail_user", key = "#id")
    public UserDTO findByIdOrThrow(UUID id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .map(el -> modelMapper.map(el, UserDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    public Optional<UserDTO> findById(UUID id) {
        return userRepository.findById(id).map(user -> modelMapper.map(user, UserDTO.class));
    }

    public UserDTO create(RegisterRequest registerRequest) {

        User user = modelMapper.map(registerRequest, User.class);

        // Set Hash Password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Find and Set Roles
        Set<Role> roles = registerRequest.getRoles().stream()
                .map(roleName -> {
                    return roleRepository.findByName(roleName).get();
                })
                .collect(Collectors.toSet());

        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    public UserDTO update(UUID id, RegisterRequest registerRequest) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFullname(registerRequest.getFullname());
                    user.setUsername(registerRequest.getUsername());
                    user.setEmail(registerRequest.getEmail());

                    Set<Role> roles = registerRequest.getRoles().stream()
                            .map(el -> {
                                return roleRepository.findByName(el).get();
                            })
                            .collect(Collectors.toSet());

                    user.setRoles(roles);
                    User updatedUser = userRepository.save(user);
                    return modelMapper.map(updatedUser, UserDTO.class);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
