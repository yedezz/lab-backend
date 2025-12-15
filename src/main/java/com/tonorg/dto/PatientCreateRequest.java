package com.tonorg.dto;

import java.time.LocalDate;

public class PatientCreateRequest {

    public String username;

    public String firstName;
    public String lastName;
    public LocalDate dateOfBirth;

    public String email;
    public String phone;
    public String cin;
    public String socialSecurityNumber;
}
