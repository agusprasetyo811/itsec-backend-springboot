package agus.prasetyo.backend.apps.model.request;

import java.util.Set;
import java.util.UUID;

public class VerificationRequest {
    private UUID id;
    private String otp;

    public UUID getId() {
        return id;
    }

    public String getOtp() {
        return otp;
    }
}
