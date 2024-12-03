package com.ltlpay.travel_fares;

import com.ltlpay.travel_fares.service.TapDataFileProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class TravelFaresApplication {
    @Autowired
    TapDataFileProcessor tapDataFileProcessor;

    public static void main(String[] args) {
        SpringApplication.run(TravelFaresApplication.class, args);
    }

}
