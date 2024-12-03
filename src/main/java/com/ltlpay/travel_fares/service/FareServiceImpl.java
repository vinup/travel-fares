package com.ltlpay.travel_fares.service;

import com.ltlpay.travel_fares.entity.Fare;
import com.ltlpay.travel_fares.repository.FareRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@Component
@Slf4j
public class FareServiceImpl {
    @Autowired
    private FareRepository fareRepository;

    public BigDecimal calculateFare(BigInteger tapOnStop, BigInteger tapOffStop) {
        BigInteger sourceStopId = tapOffStop.min(tapOnStop);
        BigInteger destinationStopId = tapOffStop.max(tapOnStop);
        log.info("sourceStopId: " + sourceStopId + " destinationStopId: " + destinationStopId);
        try {
            Optional<Fare> fare = fareRepository.findBySourceStopIdAndDestinationStopId(sourceStopId, destinationStopId);
            if (fare.isPresent()) {
                return fare.get().getFare();
            } else {
                log.error("Fare not found for sourceStop: {} and destinationStop: {}", sourceStopId, destinationStopId);
                return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public BigDecimal calculateMaxPossibleFare(BigInteger tappedStopId) {
        try {
            BigDecimal maxPossibleFare = fareRepository.findMaxFareByStop(tappedStopId);
            if (maxPossibleFare != null) {
                return maxPossibleFare;
            } else {
                log.error("Fare not found for stop: {}", tappedStopId);
                return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
