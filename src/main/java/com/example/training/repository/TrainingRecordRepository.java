package com.example.training.repository;

import com.example.training.entity.TrainingRecord;
import com.example.training.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface  TrainingRecordRepository extends JpaRepository<TrainingRecord, Long>{

    //ユーザの記録をトレーニング日の降順で取得
    List<TrainingRecord>findByUserOrderByTrainDateDesc(User user);

    //期間を指定して記録を取得
    List<TrainingRecord>findByUserAndTrainDateBetween(
            User user,
            LocalDate startDate,
            LocalDate endDate
            );

    //ユーザとIDで記録を取得
    Optional<TrainingRecord>findByIdAndUser(Long id, User user);

    //ユーザーとIDで存在確認
    boolean existsByIdAndUser(Long id, User user);

    //筋肉部位別の集計(グラフ表示用)
    @Query("""
            SELECT t.muscleGroup, Count(t)
            FROM TrainingRecord t
            WHERE t.user = :user
            AND t.trainDate BETWEEN :startDate AND :endDate
            GROUP BY t.muscleGroup
            Order BY COUNT(t) DESC
    """)

    List<Object[]> agreegateByMuscleGroup(
            @Param("user")User user,
            @Param("StartDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

