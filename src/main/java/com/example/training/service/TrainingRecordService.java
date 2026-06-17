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

    public List<TrainingRecord> getUserRecords(User user) {
        return recordRepository.findByUserOrderByTrainDateDesc(user);
    }

    public List<TrainingRecord> getRecordsByDateRange(
            User user,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return recordRepository.findByUserAndTrainDateBetween(
                user, startDate, endDate
        );
    }

    public TrainingRecord getRecord(Long id, User user) {
        return recordRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("記録が見つかりません id: " + id)
                );
    }

    @Transactional
    public TrainingRecord createRecord(TrainingRecord record) {
        return recordRepository.save(record);
    }

    @Transactional
    public TrainingRecord updateRecord(
            Long id,
            User user,
            TrainingRecord updated
    ) {
        // getRecord() 内で存在チェック・権限チェックを実施
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
