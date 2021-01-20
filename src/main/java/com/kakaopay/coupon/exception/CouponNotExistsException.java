package com.kakaopay.coupon.exception;

public class CouponNotExistsException extends RuntimeException {
    public CouponNotExistsException(){
        super("쿠폰번호가 없습니다.");
    }

    public CouponNotExistsException(String couponNum){
        super("존재하지 않은 쿠폰번호 입니다.[쿠폰번호 : "+couponNum+"]");
    }

    public CouponNotExistsException(String couponNum, String expDate){
        super("이미 유효기간이 지난 쿠폰번호 입니다.[쿠폰번호 : "+couponNum+", 유효기간 : "+expDate+"]");
    }

    public CouponNotExistsException(String couponNum, Boolean hasOrEmpty){
        super("사용자가 지정되지 않은 쿠폰입니다.[쿠폰번호 : "+couponNum);
    }

    public CouponNotExistsException(Boolean useYn){
        super("아직 사용되지 않은 쿠폰입니다.");
    }

}
