package com.kakaopay.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.coupon.config.JwtTokenProvider;
import com.kakaopay.coupon.domain.User;
import com.kakaopay.coupon.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;

    @BeforeEach
    public void createJwtToken(){
        /** Given, When **/
        //User create
        userRepository.save(User.builder()
                .userId("dh")
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getId();

        //Token create
        token = jwtTokenProvider.createToken("dh", Collections.singletonList("ROLE_USER"));
        assertNotNull(token);


    }

    @Test
    @BeforeEach
    public void createTest() throws Exception {

        String requestBody= "{\"numberCoupon\":\"1\",\"numberCoupon\":\"1\"}";

        mvc.perform(MockMvcRequestBuilders.post("/coupon/create")
                .header("X-AUTH-TOKEN", token).content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @BeforeEach
    public void payTest() throws Exception {

        ResultActions resultActions =  mvc.perform(MockMvcRequestBuilders.post("/coupon/pay")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void selectPayCoupon() throws Exception {
        ResultActions resultActions =  mvc.perform(MockMvcRequestBuilders.get("/coupon/selectPay")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void useTest() throws Exception {
        ResultActions resultActions =  mvc.perform(MockMvcRequestBuilders.post("/coupon/use")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}