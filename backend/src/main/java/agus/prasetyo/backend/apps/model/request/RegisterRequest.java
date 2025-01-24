package agus.prasetyo.backend.apps.model.request;

import java.util.Set;
import java.util.UUID;

public class RegisterRequest {
    private UUID id;
    private String fullname;
    private String email;
    private String username;
    private String password;
    private Set<String> roles;

    public UUID getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
