package com.tonorg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleTestController {

    @GetMapping("/admin")
    public String admin() {
        return "Zone ADMIN";
    }

    @GetMapping("/lab/admin")
    public String adminLabo() {
        return "Zone ADMIN LABO";
    }

    @GetMapping("/lab")
    public String userLabo() {
        return "Zone USER LABO";
    }

    @GetMapping("/patient")
    public String patient() {
        return "Zone PATIENT";
    }
}
