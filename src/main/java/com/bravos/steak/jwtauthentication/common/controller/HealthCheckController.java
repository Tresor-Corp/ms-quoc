package com.bravos.steak.jwtauthentication.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/healthz")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }

}
