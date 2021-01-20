package com.kakaopay.coupon.service;

import com.kakaopay.coupon.domain.CouponResponse;
import com.kakaopay.coupon.domain.User;
import com.kakaopay.coupon.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.kakaopay.coupon.funciton.GetCouponCode.getCouponCode;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    @Autowired
    private CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public String create(User user, Long numberCoupon){

        for (int i = 0; i < numberCoupon; i++) {
            CouponResponse couponResponse = CouponResponse.builder()
                    .couponCode(getCouponCode().trim()) //쿠폰 Code
                    .couponPay(false)                   //쿠폰 지급여부
                    .couponUse(false)                   //쿠폰 사용여부
                    .createdUserId(user.getUsername())
                    .emailAddress(user.getEmailAddress())
                    .expireDate(LocalDateTime.now().plusHours(9).toLocalDate().plusDays(4))
                    .createdDate(LocalDateTime.now().plusHours(9))
                    .updatedDate(LocalDateTime.now().plusHours(9))
                    .build();
            couponRepository.save(couponResponse).subscribe();
        }
        return user.getUsername() + "님의 쿠폰 " + numberCoupon + "개가 생성되었습니다." ;

    }

    public Flux<CouponResponse> selectCoupon(String loginUserName){
        return couponRepository.findByCreatedUserId(loginUserName);
    }


    //3. 지급 & 미지급 쿠폰 조회
    public Flux<CouponResponse> selectCoupon(String userName, Boolean couponPay,Boolean couponUse){
        return couponRepository.findByCreatedUserIdAndCouponPayAndCouponUse(userName, couponPay, couponUse);
    }

    //2-1. 쿠폰코드 유효성 조회
    public Mono<CouponResponse> selectCouponCode(String couponCode){
        return couponRepository.findByCouponCode(couponCode);
    }

    //2-2. 쿠폰 지급 & 사용
    public void update(String userName, CouponResponse cr, Boolean couponPay, Boolean couponUse){

        CouponResponse couponResponse = CouponResponse.builder()
                .id(cr.getId())
                .couponCode(cr.getCouponCode()) //쿠폰 Code
                .couponPay(couponPay)           //쿠폰 지급여부
                .couponUse(couponUse)           //쿠폰 사용여부
                .expireDate(cr.getExpireDate())
                .createdDate(cr.getCreatedDate())
                .updatedDate(LocalDateTime.now())
                .createdUserId(cr.getCreatedUserId())
                .build();
        couponRepository.save(couponResponse).subscribe();
    }

    //3. 당일 만료된 쿠폰 조회
    public Flux<CouponResponse> selectExpireToday(String userName){
        return couponRepository.findByCreatedUserIdAndExpireDate(userName,LocalDateTime.now().plusHours(9).toLocalDate());
    }

    public Flux<CouponResponse> selectExpireThreeDays(String userName){
        return couponRepository.findByCreatedUserIdAndExpireDate(userName,LocalDateTime.now().plusHours(9).toLocalDate());
    }


    public void deleteAll() {
        couponRepository.deleteAll();
    }
}
