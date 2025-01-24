package agus.prasetyo.backend.apps.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class LoggerDTO implements Serializable {

    private UUID id;
    private String action, status, request, response, deviceInfo, ipAddress, deviceType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private Object requestObj, responseObj;

    public LoggerDTO() {
    }

    public LoggerDTO(String action, String status, String request, String response, String deviceInfo, String ipAddress, String deviceType) {
        this.action = action;
        this.status = status;
        this.request = request;
        this.response = response;
        this.deviceInfo = deviceInfo;
        this.ipAddress = ipAddress;
        this.deviceType = deviceType;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getRequestObj() {
        return requestObj;
    }

    public Object getResponseObj() {
        return responseObj;
    }

    public void setRequestObj(Object requestObj) {
        this.requestObj = requestObj;
    }

    public void setResponseObj(Object responseObj) {
        this.responseObj = responseObj;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getAction() {
        return action;
    }


    public String getStatus() {
        return status;
    }


    public String getDeviceInfo() {
        return deviceInfo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }


}
