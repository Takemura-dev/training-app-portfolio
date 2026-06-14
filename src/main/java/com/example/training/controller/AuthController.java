package com.example.training.controller;

import com.example.training.dto.LoginRequest;
import com.example.training.dto.LoginResponse;
import com.example.training.dto.RegisterRequest;
import com.example.training.entity.User;
import com.example.training.repository.UserRepository;
import com.example.training.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ① ユーザー登録
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        // メールアドレスの重複チェック
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("このメールアドレスは既に使用されています");
        }

        // ユーザーを作成
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword()) // ハッシュ化！
        );
        user.setUsername(request.getUsername());

        userRepository.save(user);

        return ResponseEntity.ok("ユーザー登録が完了しました");
    }

    // ② ログイン
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        // メールアドレスでユーザーを検索
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("メールアドレスまたはパスワードが正しくありません")
                );

        // パスワードを検証
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            return ResponseEntity
                    .badRequest()
                    .body(null);
        }

        // JWT トークンを発行
        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(
                new LoginResponse(token, user.getEmail(), user.getUsername())
        );
    }
}
