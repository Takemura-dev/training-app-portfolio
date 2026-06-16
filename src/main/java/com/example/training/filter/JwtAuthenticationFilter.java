package com.example.training.filter;

import com.example.training.repository.UserRepository;
import com.example.training.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // ① Authorization ヘッダーを取得
        String authHeader = request.getHeader("Authorization");

        // ② Bearer トークンがない場合はスルー
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ③ トークンを取り出す（"Bearer " の後ろ）
        String token = authHeader.substring(7);

        // ④ トークンが有効か確認
        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // ⑤ トークンからメールアドレスを取り出す
        String email = jwtUtil.extractEmail(token);

        // ⑥ ユーザーを DB から取得
        userRepository.findByEmail(email).ifPresent(user -> {

            // ⑦ 認証情報を Spring Security にセット
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user, null, new ArrayList<>()
                    );
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        });

        // ⑧ 次のフィルターへ
        filterChain.doFilter(request, response);
    }
}
