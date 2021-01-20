package com.kakaopay.coupon.controller;

import com.kakaopay.coupon.config.JwtTokenProvider;
import com.kakaopay.coupon.domain.User;
import com.kakaopay.coupon.repository.CouponRepository;
import com.kakaopay.coupon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    private final CouponRepository couponRepository;

    // 회원가입
    @PostMapping("/join")
    public Long join(@RequestBody User user) {
        return userRepository.save(User.builder()
                .userId(user.getUserId())
                .password(passwordEncoder.encode(user.getPassword()))
                .emailAddress(user.getEmailAddress())
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getId();
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        User member = userRepository.findByUserId(user.get("userId"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 id 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }


    @PostMapping("/deleteAll")
    public void  deleteAll() {
        couponRepository.deleteAll().subscribe();
    }
}