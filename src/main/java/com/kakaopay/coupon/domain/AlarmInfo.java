package com.kakaopay.coupon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlarmInfo {

    private String userName;

    private String couponCode;

    private String emailAddress;
}
