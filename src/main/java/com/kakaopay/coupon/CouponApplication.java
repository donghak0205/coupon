package com.kakaopay.coupon;

import com.kakaopay.coupon.repository.CouponRepository;
import com.mongodb.BasicDBObject;
import org.h2.util.TempFileDeleter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
public class CouponApplication {


    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class, args);
    }

}
