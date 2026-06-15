package com.example.training.repository;

import com.example.training.entity.TrainingRecord;
import com.example.training.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TrainingRecordRepositoryTest{

    @Autowired
    private TrainingRecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    // テスト用データ
    private User testUser;
    private TrainingRecord record1;
    private TrainingRecord record2;

    @BeforeEach
    void setUp(){
        // テスト用ユーザーをDBに保存
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setUsername("テストユーザー");
        userRepository.save(testUser);

        // テスト用記録1
        record1 = new TrainingRecord();
        record1.setUser(testUser);
        record1.setExerciseName("ベンチプレス");
        record1.setWeight(100.0);
        record1.setReps(10);
        record1.setSets(3);
        record1.setTrainDate(LocalDate.of(2026, 6, 1));
        record1.setMuscleGroup("胸");
        recordRepository.save(record1);

        // テスト用記録2
        record2 = new TrainingRecord();
        record2.setUser(testUser);
        record2.setExerciseName("スクワット");
        record2.setWeight(120.0);
        record2.setReps(8);
        record2.setSets(4);
        record2.setTrainDate(LocalDate.of(2026, 6, 10));
        record2.setMuscleGroup("脚");
        recordRepository.save(record2);
    }

    @Test
    @DisplayName("日付降順で全記録を取得できる")
    void findByUserOrderByTrainDateDesc_日付降順で取得できる() {
        // Act
        List<TrainingRecord> results =
                recordRepository.findByUserOrderByTrainDateDesc(testUser);

        // Assert
        assertEquals(2, results.size());

        // 新しい日付（6/10）が先に来る
        assertEquals("スクワット",
                results.get(0).getExerciseName());

        // 古い日付（6/1）が後に来る
        assertEquals("ベンチプレス",
                results.get(1).getExerciseName());
    }

    @Test
    @DisplayName("期間を指定して記録を取得できる")
    void findByUserAndTrainDateBetween_期間内の記録を取得できる() {
        // Act
        List<TrainingRecord> results =
                recordRepository.findByUserAndTrainDateBetween(
                        testUser,
                        LocalDate.of(2026, 6, 5),  // 6/5以降
                        LocalDate.of(2026, 6, 15)  // 6/15以前
                );

        // Assert
        // 6/1 は範囲外、6/10 は範囲内
        assertEquals(1, results.size());
        assertEquals("スクワット",
                results.get(0).getExerciseName());
    }

    @Test
    @DisplayName("期間外の記録は取得されない")
    void findByUserAndTrainDateBetween_期間外の記録は取得されない() {
        // Act
        List<TrainingRecord> results =
                recordRepository.findByUserAndTrainDateBetween(
                        testUser,
                        LocalDate.of(2026, 7, 1),  // 7/1以降
                        LocalDate.of(2026, 7, 31)  // 7/31以前
                );

        // Assert
        // 6月の記録は7月の範囲外
        assertEquals(0, results.size());
    }

    @Test
    @DisplayName("IDとユーザーで記録を取得できる")
    void findByIdAndUser_正しいユーザーの記録を取得できる() {
        // Act
        Optional<TrainingRecord> result =
                recordRepository.findByIdAndUser(
                        record1.getId(), testUser
                );

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ベンチプレス",
                result.get().getExerciseName());
    }

    @Test
    @DisplayName("他のユーザーの記録は取得できない")
    void findByIdAndUser_他ユーザーの記録は取得できない() {
        // Arrange
        // 別のユーザーを作成
        User otherUser = new User();
        otherUser.setEmail("other@example.com");
        otherUser.setPassword("password123");
        otherUser.setUsername("別ユーザー");
        userRepository.save(otherUser);

        // Act
        // otherUser で testUser の記録を取得しようとする
        Optional<TrainingRecord> result =
                recordRepository.findByIdAndUser(
                        record1.getId(), otherUser
                );

        // Assert
        // 取得できないことを確認（セキュリティ確認）
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("IDとユーザーで記録の存在を確認できる")
    void existsByIdAndUser_存在する記録はtrueを返す() {
        // Act
        boolean exists = recordRepository
                .existsByIdAndUser(record1.getId(), testUser);

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("存在しない記録はfalseを返す")
    void existsByIdAndUser_存在しない記録はfalseを返す() {
        // Act
        boolean exists = recordRepository
                .existsByIdAndUser(999L, testUser);

        // Assert
        assertFalse(exists);
    }
}
