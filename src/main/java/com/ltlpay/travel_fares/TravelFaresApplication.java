package com.ltlpay.travel_fares;

import com.ltlpay.travel_fares.service.TapDataFileProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class TravelFaresApplication implements CommandLineRunner {
    @Autowired
    TapDataFileProcessor tapDataFileProcessor;

    public static void main(String[] args) {
        SpringApplication.run(TravelFaresApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args != null && args.length > 0) {
            try {
                tapDataFileProcessor.processTapDataFile(args[0]);
            } catch (Exception exception) {
                log.error("Unable to process input file: {}", args[0], exception);

            }
        } else {
            log.error("No inputfile provided!");
        }


    }
}
