package com.kakaopay.coupon.service;

import com.kakaopay.coupon.config.ScheduledTasks;
import com.kakaopay.coupon.domain.AlarmInfo;
import com.kakaopay.coupon.domain.CouponResponse;
import com.kakaopay.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
public class AlarmService {

    private final ScheduledTasks scheduledTasks;
    private final CouponRepository couponRepository;

    @Autowired
    public AlarmService(ScheduledTasks scheduledTasks, CouponRepository couponRepository) {
        this.scheduledTasks = scheduledTasks;
        this.couponRepository = couponRepository;
    }


    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {

        Flux<CouponResponse> noticeList = couponRepository.findByExpireDate(LocalDateTime.now().plusHours(9).toLocalDate().plusDays(3)).log();

        if (noticeList.count().block() > 0) {
            noticeList.map(m -> {
                AlarmInfo alarmInfo = AlarmInfo.builder()
                        .userName(m.getCreatedUserId())
                        .couponCode(m.getCouponCode())
                        .emailAddress(m.getEmailAddress())
                        .build();
                return alarmInfo;
            }).subscribe(i -> {
                scheduledTasks.send("test", i);
                log.info("send Success!");
            });
        }
//                return alarmInfo;
//                    i.
//                 AlarmInfo alarmInfo = AlarmInfo.builder()
//                            .userName(i.getCreatedUserId())
//                            .couponCode(i.getCouponCode())
//                            .emailAddress(i.getEmailAddress())
//                            .build();
//                return alarmInfo;
//                }).defaultIfEmpty(System.out.println("aa")).s

        //scheduledTasks.send("test",alarmInfo);
    }
}
