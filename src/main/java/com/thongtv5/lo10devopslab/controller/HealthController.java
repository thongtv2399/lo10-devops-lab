package com.thongtv5.lo10devopslab.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Cung cấp endpoint kiểm tra trạng thái hoạt động của ứng dụng.
@RestController
@RequestMapping("/api/health")
public class HealthController {

    // Trả về trạng thái hiện tại của ứng dụng.
    @GetMapping
    public ResponseEntity<Map<String, Object>> checkHealth() {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "status",
                "UP"
        );

        response.put(
                "service",
                "lo10-devops-lab"
        );

        response.put(
                "timestamp",
                Instant.now()
        );

        return ResponseEntity.ok(
                response
        );
    }
}