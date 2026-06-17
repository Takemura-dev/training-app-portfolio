package com.example.training.controller;

import com.example.training.entity.TrainingRecord;
import com.example.training.entity.User;
import com.example.training.service.TrainingRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class TrainingRecordController {

    @Autowired
    private TrainingRecordService recordService;
    private User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        return (User) authentication.getPrincipal();
    }
    @GetMapping
    public List<TrainingRecord> getRecords() {
        User user = getCurrentUser();
        return recordService.getUserRecords(user);
    }
    @GetMapping("/{id}")
    public TrainingRecord getRecord(@PathVariable Long id) {
        User user = getCurrentUser();
        return recordService.getRecord(id, user);
    }
    @PostMapping
    public TrainingRecord createRecord(
            @Valid @RequestBody TrainingRecord record
    ) {
        User user = getCurrentUser();
        record.setUser(user);
        return recordService.createRecord(record);
    }
    @PutMapping("/{id}")
    public TrainingRecord updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody TrainingRecord record
    ) {
        User user = getCurrentUser();
        return recordService.updateRecord(id, user, record);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long id
    ) {
        User user = getCurrentUser();
        recordService.deleteRecord(id, user);
        return ResponseEntity.noContent().build();
    }
}