package com.kakaopay.coupon.funciton;

import java.util.UUID;

public class GetCouponCode {

    /**쿠폰 코드를 생성합니다.(36자리) **/
    public static String getCouponCode() {
        return UUID.randomUUID().toString();
    }
}
