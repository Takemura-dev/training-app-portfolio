package com.example.training.service;

import com.example.training.entity.TrainingRecord;
import com.example.training.entity.User;
import com.example.training.exception.ResourceNotFoundException;
import com.example.training.repository.TrainingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrainingRecordService {

    @Autowired
    private TrainingRecordRepository recordRepository;

    // ① 全記録取得（日付の新しい順）
    public List<TrainingRecord> getUserRecords(User user) {
        return recordRepository.findByUserOrderByTrainDateDesc(user);
    }

    // ② 期間を指定して記録を取得
    public List<TrainingRecord> getRecordsByDateRange(
            User user,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return recordRepository.findByUserAndTrainDateBetween(
                user, startDate, endDate
        );
    }

    // ③ 記録を1件取得
    public TrainingRecord getRecord(Long id, User user) {
        return recordRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("記録が見つかりません id: " + id)
                );
    }

    // ④ 記録を作成
    @Transactional
    public TrainingRecord createRecord(TrainingRecord record) {
        return recordRepository.save(record);
    }

    // ⑤ 記録を更新
    @Transactional
    public TrainingRecord updateRecord(
            Long id,
            User user,
            TrainingRecord updated
    ) {
        // 既存の記録を取得（存在チェック＋権限チェック）
        TrainingRecord existing = getRecord(id, user);

        // 更新内容を反映
        existing.setExerciseName(updated.getExerciseName());
        existing.setWeight(updated.getWeight());
        existing.setReps(updated.getReps());
        existing.setSets(updated.getSets());
        existing.setTrainDate(updated.getTrainDate());
        existing.setMuscleGroup(updated.getMuscleGroup());
        existing.setMemo(updated.getMemo());

        return recordRepository.save(existing);
    }

    // ⑥ 記録を削除
    @Transactional
    public void deleteRecord(Long id, User user) {
        // 存在チェック＋権限チェック
        boolean exists = recordRepository.existsByIdAndUser(id, user);
        if (!exists) {
            throw new ResourceNotFoundException("記録が見つかりません id: " + id);
        }
        recordRepository.deleteById(id);
    }
}
