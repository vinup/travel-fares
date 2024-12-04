package com.ltlpay.travel_fares.service;

import com.ltlpay.travel_fares.repository.TapRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ActiveProfiles(value = "test")
class TapDataFileProcessorTest {
    @Autowired
    TapDataFileProcessor tapDataFileProcessor;
    @Autowired
    TapRepository tapRepository;

    @Test
    void processTapDataFile() throws Exception {

        String filePath = "src/test/resources/test-tap-data.csv";
        tapDataFileProcessor.processTapDataFile(filePath);

    }


}