// src/main/java/com/tonorg/dto/CreateLabRequest.java
package com.tonorg.dto;

public class CreateLabRequest {

    private String labName;
    private String labCity;
    private String labContactEmail;

    private String adminUsername;
    private String adminEmail;        // éventuellement pour plus tard
    private String adminTempPassword;

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getLabCity() {
        return labCity;
    }

    public void setLabCity(String labCity) {
        this.labCity = labCity;
    }

    public String getLabContactEmail() {
        return labContactEmail;
    }

    public void setLabContactEmail(String labContactEmail) {
        this.labContactEmail = labContactEmail;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminTempPassword() {
        return adminTempPassword;
    }

    public void setAdminTempPassword(String adminTempPassword) {
        this.adminTempPassword = adminTempPassword;
    }
}
