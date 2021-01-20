package com.kakaopay.coupon.repository;

import com.kakaopay.coupon.domain.CouponResponse;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface CouponRepository extends ReactiveCrudRepository<CouponResponse,Long> {

    //couponCode 조회
    Flux<CouponResponse> findByCreatedUserId(String createdUserId);


    Mono<CouponResponse> findByCreatedUserIdAndCouponCode(String createdUserId, String couponCode);




    Mono<CouponResponse> findByCouponCode(String couponCode);



    //생성된 쿠폰 조회
    Flux<CouponResponse> findByCreatedUserIdAndCouponPayAndCouponUse(String createdUserId, Boolean couponPay, Boolean couponUse);

    //당일 만료쿠폰조회
    Flux<CouponResponse> findByCreatedUserIdAndExpireDate(String userName,LocalDate currentDate);

    Flux<CouponResponse> findByExpireDate(LocalDate ThreeAfterDate);

}
