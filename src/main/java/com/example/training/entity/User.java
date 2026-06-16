package com.example.training.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    //主キー
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //メールアドレス
    @Column(nullable = false, unique = true)
    private String email;

    //パスワード
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    // ユーザ名
    @Column(nullable = false)
    private String username;

    //作成日時
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //更新日時
    @Column
    private LocalDateTime updatedAt;

    //保存前に自動実行
    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    //更新前に自動実行
    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    //--- Getter / Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
