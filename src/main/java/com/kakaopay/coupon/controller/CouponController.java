package com.kakaopay.coupon.controller;


import com.kakaopay.coupon.aop.LogExecutionTime;
import com.kakaopay.coupon.domain.CouponResponse;
import com.kakaopay.coupon.domain.User;
import com.kakaopay.coupon.network.Header;
import com.kakaopay.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/coupon")
@RestController
public class CouponController {

    private final CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    /**
     * 1. 쿠폰생성
     */
    @PostMapping("/create/{num}")
    @LogExecutionTime
    public ResponseEntity<Header<String>> createCoupon(Authentication auth, @PathVariable Long num) {
        User user = (User) auth.getPrincipal();

        if(num>0) {
            return ok().body(Header.OK(couponService.create(user, num)));
        } else {
            return ok().body(Header.ERROR("1이상 숫자를 입력해주세요!"));
        }
    }

    /**
     * 2. 생성된 쿠폰중 지급하기
     * @return
     */
    @PostMapping("/pay")
    public Mono<Header<String>> payCoupon(Authentication auth) {

        UserDetails user = (UserDetails) auth.getPrincipal();
        Flux<CouponResponse> selectedFlux = couponService.selectCoupon(user.getUsername());

        if(selectedFlux.count().block()>0){

            return selectedFlux.filter(f->f.getCouponPay().equals(false))
                    .flatMap(fm->Mono.just(fm)).next()
                    .map(m->{
                                couponService.update(user.getUsername(), m, true, false);
                                return Header.OK(m.getCouponCode());
                            }
                    ).defaultIfEmpty(Header.ERROR(user.getUsername() + "님은 생성된 쿠폰을 다 사용하셨습니다. 생성 후 지급해주세요.")).log();
        } else {
            return Mono.just(Header.ERROR(user.getUsername() + "님의 생성된 쿠폰이 없습니다. 확인바랍니다."));
        }



      /*
      원래 소스
      StringBuilder sb = new StringBuilder();

        //selectedFlux.filter(f-> f.getCouponPay().equals(false)).map(m->m.getCouponCode());


        selectedFlux.filter(f->f.getCouponPay().equals(false));



        return selectedFlux.map(m->{
                        if(m.getCouponPay().equals(false)) {
                            couponService.update(user.getUsername(), m, true, false);
                            return m.getCouponCode();
                        }
                        else {
                            return "생성된 쿠폰을 다 사용하셨습니다. 확인바랍니다.";
                        }
                     }).map(n->Header.OK(n)).defaultIfEmpty(Header.ERROR("생성된 쿠폰이 없습니다. 확인바랍니다."));*/

    }

    /**
     * 3. 지급된 쿠폰 조회
     */
    @GetMapping("/selectPay")
    public Flux<Header<String>> selectPayCoupon(Authentication auth) {
        UserDetails user = (UserDetails) auth.getPrincipal();
        Flux<CouponResponse> selectFlux = couponService.selectCoupon(user.getUsername(), true, false).log();
        return selectFlux.map(n -> n.getCouponCode()).map(m->Header.OK(m)).defaultIfEmpty(Header.ERROR("지급된 쿠폰이 없습니다. 확인바랍니다."));
    }

    /**
     * 4. (지급된 쿠폰) 사용하기
     */
    @PostMapping("/use/{couponCode}")
    public Mono<Header<String>> useCoupon(Authentication auth, @PathVariable String couponCode) {

        UserDetails user = (UserDetails) auth.getPrincipal();

        if(couponCode.length()!=36) {
            return Mono.just(Header.ERROR("쿠폰코드 자리수가 틀렸습니다. 확인바랍니다."));
        }

        Mono<CouponResponse> selectedMono = couponService.selectCouponCode(couponCode);

        return selectedMono.map(i -> {

                if(!i.getCreatedUserId().equals(user.getUsername())){
                    return Header.OK("쿠폰코드는 발급한 사용자만 사용할 수 있습니다. 확인바랍니다.");
                }
                else if(i.getCouponPay()==false){
                    return Header.OK("아직 지급이 안된 쿠폰입니다. 확인바랍니다.");

                }
                else{
                    if(i.getCouponUse() ==false) {
                        couponService.update(user.getUsername(), i, true, true);
                        return Header.OK("쿠폰코드 : " + i.getCouponCode() + " 을 사용하였습니다.");
                    }
                    return Header.OK("사용된 쿠폰입니다. 확인바랍니다.");

                }
                }).defaultIfEmpty(Header.ERROR("존재하지 않는 쿠폰코드 입니다. 확인바랍니다."));

    }

    /**
     * 5. 사용한 쿠폰 취소하기
     */
    @PostMapping("/cancelUse/{couponCode}")
    public Mono<Header<String>> cancelUseCoupon(Authentication auth, @PathVariable String couponCode) {
        UserDetails user = (UserDetails) auth.getPrincipal();

        if(couponCode.length()!=36) {
            return Mono.just(Header.ERROR("쿠폰코드 자리수가 틀렸습니다. 확인바랍니다."));
        }

        Mono<CouponResponse> selectedMono = couponService.selectCouponCode(couponCode);

        return selectedMono.map(i -> {

            if(!i.getCreatedUserId().equals(user.getUsername())){
                return Header.OK("쿠폰코드는 발급한 사용자만 취소할 수 있습니다. 확인바랍니다.");
            }
            else if(i.getCouponPay()==false){
                return Header.OK("아직 지급이 안된 쿠폰입니다. 확인바랍니다.");

            }
            else{
                if(i.getCouponUse() ==true) {
                    couponService.update(user.getUsername(), i, true, false);
                    return Header.OK("쿠폰코드 : " + i.getCouponCode() + " 을 사용 취소하였습니다.");
                }
                return Header.OK("사용된 쿠폰이 아닙니다. 확인바랍니다.");

            }
        }).defaultIfEmpty(Header.ERROR("존재하지 않는 쿠폰코드 입니다. 확인바랍니다."));
    }

    /**
     * 6. 당일 만료된 쿠폰 조회
     */
    @GetMapping("/selectExpireToday")
    public Flux<Header<String>> selectExpireToday(Authentication auth) {
        UserDetails user = (UserDetails) auth.getPrincipal();
        Flux<CouponResponse> selectFlux = couponService.selectExpireToday(user.getUsername()).log();
        return selectFlux.map(n -> Header.OK(n.getCouponCode())).defaultIfEmpty(Header.ERROR("더 이상 받아갈 쿠폰이 없습니다. 확인바랍니다."));
    }

}
