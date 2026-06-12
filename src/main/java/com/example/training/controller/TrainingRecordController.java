package com.example.training.controller;

import com.example.training.entity.TrainingRecord;
import com.example.training.entity.User;
import com.example.training.service.TrainingRecordService;
import com.example.training.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class TrainingRecordController {

    @Autowired
    private TrainingRecordService recordService;

    @Autowired
    private UserRepository userRepository;

    // ① 仮のユーザー取得（後で認証機能に置き換える）
    private User getTemporaryUser() {
        return userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("ユーザーが存在しません")
                );
    }

    // ② 記録一覧取得
    @GetMapping
    public List<TrainingRecord> getRecords() {
        User user = getTemporaryUser();
        return recordService.getUserRecords(user);
    }

    // ③ 記録を1件取得
    @GetMapping("/{id}")
    public TrainingRecord getRecord(@PathVariable Long id) {
        User user = getTemporaryUser();
        return recordService.getRecord(id, user);
    }

    // ④ 記録を作成
    @PostMapping
    public TrainingRecord createRecord(
            @RequestBody TrainingRecord record
    ) {
        User user = getTemporaryUser();
        record.setUser(user);
        return recordService.createRecord(record);
    }

    // ⑤ 記録を更新
    @PutMapping("/{id}")
    public TrainingRecord updateRecord(
            @PathVariable Long id,
            @RequestBody TrainingRecord record
    ) {
        User user = getTemporaryUser();
        return recordService.updateRecord(id, user, record);
    }

    // ⑥ 記録を削除
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long id
    ) {
        User user = getTemporaryUser();
        recordService.deleteRecord(id, user);
        return ResponseEntity.noContent().build();
    }
}