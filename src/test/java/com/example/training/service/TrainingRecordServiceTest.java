package com.example.training.service;

import com.example.training.entity.TrainingRecord;
import com.example.training.entity.User;
import com.example.training.exception.ResourceNotFoundException;
import com.example.training.repository.TrainingRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingRecordServiceTest{
    @Mock
    private TrainingRecordRepository recordRepository;

    @InjectMocks
    private TrainingRecordService recordService;

    private User testUser;
    private TrainingRecord testRecord;

    //準備処理
    @BeforeEach
    void setUp(){
        //テスト用ユーザを作成
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setUsername("テストユーザー");
        testUser.setPassword("password123");

        //テスト用記録を作成
        testRecord = new TrainingRecord();
        testRecord.setExerciseName("ベンチプレス");
        testRecord.setWeight(100.0);
        testRecord.setReps(10);
        testRecord.setSets(3);
        testRecord.setTrainDate(LocalDate.now());
        testRecord.setUser(testUser);
    }

    // 記録作成のテスト
    @Test
    void createRecord_正常に保存できる(){

        // Arrange
        //「save が呼ばれたら testRecord を返す」設定
        when(recordRepository.save(any(TrainingRecord.class)))
                .thenReturn(testRecord);

        // Act
        TrainingRecord result = recordService.createRecord(testRecord);

        // Assert
        assertNotNull(result);
        assertEquals("ベンチプレス", result.getExerciseName());
        assertEquals(100.0, result.getWeight());

        // saveが1回呼ばれたことを確認
        verify(recordRepository, times(1)).save(any(TrainingRecord.class));
    }

    // 記録取得のテスト（正常系）
    @Test
    void getRecord_存在する記録を取得できる(){

        // Arrange
        when(recordRepository.findByIdAndUser(1L, testUser))
                .thenReturn(Optional.of(testRecord));

        // Act
        TrainingRecord result = recordService.getRecord(1L, testUser);

        // Assert
        assertNotNull(result);
        assertEquals("ベンチプレス", result.getExerciseName());
    }

    // 記録取得のテスト（異常系）
    @Test
    void getRecord_存在しない記録は例外が発生する(){

        // Arrange
        // 「findByIdAndUser が呼ばれたら空を返す」設定
        when(recordRepository.findByIdAndUser(999l, testUser))
                .thenReturn(Optional.empty());

        // Act & Assert
        // 例外が発生することを確認
        assertThrows(
                ResourceNotFoundException.class,
                () -> recordService.getRecord(999l, testUser)
        );
    }

    // 全件取得テスト
    @Test
    void getUserRecords_正常に取得できる() {
        // Arrange
        List<TrainingRecord> expectedRecords = List.of(testRecord);
        when(recordRepository.findByUserOrderByTrainDateDesc(testUser))
                .thenReturn(expectedRecords);

        // Act
        List<TrainingRecord> result =
                recordService.getUserRecords(testUser);

        // Assert
        assertEquals(1, result.size());
        assertEquals("ベンチプレス",
                result.get(0).getExerciseName());
    }

    // 記録削除のテスト
    @Test
    void deleteRecord_存在する記録を削除できる() {
        // Arrange
        when(recordRepository.existsByIdAndUser(1L, testUser))
                .thenReturn(true);

        // Act
        recordService.deleteRecord(1L, testUser);

        // Assert
        // deleteById が1回呼ばれたか確認
        verify(recordRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRecord_存在しない記録は例外が発生する() {
        // Arrange
        when(recordRepository.existsByIdAndUser(999L, testUser))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> recordService.deleteRecord(999L, testUser)
        );

        // deleteById が呼ばれていないことを確認
        verify(recordRepository, never()).deleteById(any());
    }

}

