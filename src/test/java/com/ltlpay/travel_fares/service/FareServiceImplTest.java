package com.ltlpay.travel_fares.service;

import com.ltlpay.travel_fares.entity.Fare;
import com.ltlpay.travel_fares.repository.FareRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class FareServiceImplTest {
    @Mock
    private FareRepository fareRepository;

    @InjectMocks
    private FareServiceImpl fareService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateFare_WhenFareExists() {

        BigInteger tapOnStop = BigInteger.valueOf(1);
        BigInteger tapOffStop = BigInteger.valueOf(3);
        Fare fare = new Fare();
        fare.setFare(BigDecimal.valueOf(5.00));

        when(fareRepository.findBySourceStopIdAndDestinationStopId(
                BigInteger.valueOf(1), BigInteger.valueOf(3)))
                .thenReturn(Optional.of(fare));


        BigDecimal result = fareService.calculateFare(tapOnStop, tapOffStop);

        assertEquals(BigDecimal.valueOf(5.00), result);
        verify(fareRepository, times(1)).findBySourceStopIdAndDestinationStopId(
                BigInteger.valueOf(1), BigInteger.valueOf(3));
    }

    @Test
    void testCalculateFare_WhenFareDoesNotExist() {
        BigInteger tapOnStop = BigInteger.valueOf(1);
        BigInteger tapOffStop = BigInteger.valueOf(2);

        when(fareRepository.findBySourceStopIdAndDestinationStopId(
                BigInteger.valueOf(1), BigInteger.valueOf(2)))
                .thenReturn(Optional.empty());

        BigDecimal result = fareService.calculateFare(tapOnStop, tapOffStop);

        assertEquals(BigDecimal.ZERO, result);
        verify(fareRepository, times(1)).findBySourceStopIdAndDestinationStopId(
                BigInteger.valueOf(1), BigInteger.valueOf(2));
    }

    @Test
    void testCalculateMaxPossibleFare_WhenFareExists() {
        // Arrange
        BigInteger tappedStopId = BigInteger.valueOf(1);
        BigDecimal maxFare = BigDecimal.valueOf(7.50);

        when(fareRepository.findMaxFareByStop(tappedStopId)).thenReturn(maxFare);

        BigDecimal result = fareService.calculateMaxPossibleFare(tappedStopId);

        assertEquals(maxFare, result);
        verify(fareRepository, times(1)).findMaxFareByStop(tappedStopId);
    }

    @Test
    void testCalculateMaxPossibleFare_WhenFareDoesNotExist() {

        BigInteger tappedStopId = BigInteger.valueOf(1);

        when(fareRepository.findMaxFareByStop(tappedStopId)).thenReturn(null);

        BigDecimal result = fareService.calculateMaxPossibleFare(tappedStopId);

        assertEquals(BigDecimal.ZERO, result);
        verify(fareRepository, times(1)).findMaxFareByStop(tappedStopId);
    }

    @Test
    void testCalculateFare_WhenExceptionOccurs() {

        BigInteger tapOnStop = BigInteger.valueOf(1);
        BigInteger tapOffStop = BigInteger.valueOf(2);
        String exceptionMessage = "Some Database error";

        when(fareRepository.findBySourceStopIdAndDestinationStopId(
                BigInteger.valueOf(1), BigInteger.valueOf(2)))
                .thenThrow(new RuntimeException(exceptionMessage));

        RuntimeException exception =
                org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                        () -> fareService.calculateFare(tapOnStop, tapOffStop));

        assertEquals(exceptionMessage, exception.getMessage());
    }


}