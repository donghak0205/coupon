package com.kakaopay.coupon.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document("Coupon")
@Builder
public class CouponResponse {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private String id;

    //쿠폰코드
    private String couponCode;

    @NotNull
    private String emailAddress;

    //쿠폰지급
    private Boolean couponPay;
    
    //쿠폰사용
    private Boolean couponUse;

    //만기일
    private LocalDate expireDate;

    //생성일시
    @CreatedDate
    private LocalDateTime createdDate;

    //수정일시
    @LastModifiedDate
    private LocalDateTime updatedDate;

    //생성자
    @CreatedBy
    private String createdUserId;

}
