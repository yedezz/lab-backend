package com.tonorg.dto;

public class LabAdminDto {

    private Long labId;
    private String labName;
    private String labCity;

    private Long adminId;
    private String adminUsername;

    public LabAdminDto(Long labId,
                       String labName,
                       String labCity,
                       Long adminId,
                       String adminUsername) {
        this.labId = labId;
        this.labName = labName;
        this.labCity = labCity;
        this.adminId = adminId;
        this.adminUsername = adminUsername;
    }

    public Long getLabId() {
        return labId;
    }

    public String getLabName() {
        return labName;
    }

    public String getLabCity() {
        return labCity;
    }

    public Long getAdminId() {
        return adminId;
    }

    public String getAdminUsername() {
        return adminUsername;
    }
}
