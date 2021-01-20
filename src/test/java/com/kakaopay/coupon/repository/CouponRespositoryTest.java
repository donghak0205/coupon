package com.kakaopay.coupon.repository;

import com.kakaopay.coupon.domain.CouponResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("Save Test")
    public void save(){

        //Given
        CouponResponse couponResponse = CouponResponse.builder()
                .couponCode("1234567890".trim()) //쿠폰 Code
                .couponPay(false)                //쿠폰 지급여부
                .couponUse(false)                //쿠폰 사용여부
                .createdUserId("Donghak")
                .expireDate(LocalDate.now().plusDays(5))
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .id("1L")
                .build();

        //When
        Mono<CouponResponse> newCr = couponRepository.save(couponResponse);

        //Then
        StepVerifier.create(newCr)
                .assertNext(coupon -> {
                    assertThat(coupon.getCouponCode()).isEqualTo(couponResponse.getCouponCode());
                    assertThat(coupon.getCouponPay()).isEqualTo(couponResponse.getCouponPay());
                    assertThat(coupon.getCouponUse()).isEqualTo(couponResponse.getCouponUse());
                    assertThat(coupon.getCreatedUserId()).isEqualTo(couponResponse.getCreatedUserId());
                    assertThat(coupon.getExpireDate()).isEqualTo(couponResponse.getExpireDate());
                    assertThat(coupon.getCreatedDate()).isEqualTo(couponResponse.getCreatedDate());
                    assertThat(coupon.getUpdatedDate()).isEqualTo(couponResponse.getUpdatedDate());

                }).verifyComplete();
    }


    @Test
    @DisplayName("Select Test")
    public void select(){
        //Given
        CouponResponse couponResponse = CouponResponse.builder()
                .couponCode("1234567890".trim()) //쿠폰 Code
                .couponPay(false)                //쿠폰 지급여부
                .couponUse(false)                //쿠폰 사용여부
                .createdUserId("Donghak")
                .expireDate(LocalDate.now().plusDays(5))
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        //when
        Flux<CouponResponse> selectedCr = couponRepository.findByCreatedUserIdAndCouponPayAndCouponUse("Donghak",false,false).take(1).log();


        //Then
        StepVerifier.create(selectedCr)
                .assertNext(couponResponse1 -> {
                    assertThat(couponResponse1.getCouponCode()).isEqualTo("1234567890");
                    assertThat(couponResponse1.getCouponPay()).isEqualTo(false);
                    assertThat(couponResponse1.getCouponUse()).isEqualTo(false);
                    assertThat(couponResponse1.getCreatedUserId()).isEqualTo("Donghak");
                    assertThat(couponResponse1.getExpireDate()).isEqualTo(LocalDate.now().plusDays(5));
                }).verifyComplete();

    }


    @Test
    @DisplayName("Update Test")
    public void update() {

        //Given
        CouponResponse couponResponse = CouponResponse.builder()
                .couponCode("1234567890".trim()) //쿠폰 Code
                .couponPay(false)                //쿠폰 지급여부
                .couponUse(false)                //쿠폰 사용여부
                .createdUserId("Donghak")
                .expireDate(LocalDate.now().plusDays(5))
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        //When
        Flux<CouponResponse> selectedCr = couponRepository.findByCreatedUserIdAndCouponPayAndCouponUse("Donghak",false,false).take(1).log();

        CouponResponse couponResponse1 = CouponResponse.builder()
                .couponCode("1234567890".trim()) //쿠폰 Code
                .couponPay(true)                //쿠폰 지급여부 -> 변경부분
                .couponUse(true)                //쿠폰 사용여부 -> 변경부분
                .createdUserId("Donghak")
                .expireDate(LocalDate.now().plusDays(5))
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Mono<CouponResponse> newCr = couponRepository.save(couponResponse1);

        //Then
        StepVerifier.create(newCr)
                .assertNext(coupon -> {
                    assertThat(coupon.getCouponUse()).isEqualTo(true);
                    assertThat(coupon.getCouponPay()).isEqualTo(true);
                }).verifyComplete();

    }
}