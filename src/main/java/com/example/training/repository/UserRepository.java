package com.example.training.repository;

import com.example.training.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    //メールアドレスでユーザ検索（ログイン時）
    Optional<User>findByEmail(String email);

    //メールアドレスがすでに存在するか確認（登録時）
    boolean existsByEmail(String email);
}