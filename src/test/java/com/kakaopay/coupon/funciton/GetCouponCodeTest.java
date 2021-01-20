package com.kakaopay.coupon.funciton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.kakaopay.coupon.funciton.GetCouponCode.getCouponCode;

class GetCouponCodeTest {

    @Test
    public void couponCodeTest (){
        String couponCode = getCouponCode();
        Assertions.assertThat(couponCode.length()).isEqualTo(36); //36자리인지 체크
    }

}