package agus.prasetyo.backend.apps.model.request;

import java.time.LocalDateTime;
import java.util.UUID;

public class LoggerRequest {
    private String action, status, request, response, deviceInfo, ipAddress, deviceType;

    public LoggerRequest(String action, String status, String request, String response, String deviceInfo, String ipAddress, String deviceType) {
        this.action = action;
        this.status = status;
        this.request = request;
        this.response = response;
        this.deviceInfo = deviceInfo;
        this.ipAddress = ipAddress;
        this.deviceType = deviceType;
    }

    public String getAction() {
        return action;
    }

    public String getStatus() {
        return status;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }
}
