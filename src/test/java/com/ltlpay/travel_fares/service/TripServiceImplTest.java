package com.ltlpay.travel_fares.service;

import com.ltlpay.travel_fares.entity.Tap;
import com.ltlpay.travel_fares.entity.TapType;
import com.ltlpay.travel_fares.entity.Trip;
import com.ltlpay.travel_fares.entity.TripType;
import com.ltlpay.travel_fares.exception.InvalidTripException;
import com.ltlpay.travel_fares.repository.TapRepository;
import com.ltlpay.travel_fares.repository.TripRepository;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class TripServiceImplTest {
    @Mock
    private TripRepository tripRepository;

    @Mock
    private TapRepository tapRepository;

    @Mock
    private FareServiceImpl fareServiceImpl;

    @InjectMocks
    private TripServiceImpl tripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTripFromTheTaps_BothTapsAbsent() {
        // Arrange
        Optional<Tap> tapOn = Optional.empty();
        Optional<Tap> tapOff = Optional.empty();

        // Act & Assert
        InvalidTripException exception = assertThrows(InvalidTripException.class,
                () -> tripService.createTripFromTheTaps(tapOn, tapOff));

        assertEquals("Not Valid Tap Scenario", exception.getMessage());
    }

    @Test
    void testCreateTripForCompletedTrip() throws InvalidTripException {
        Tap tapOn = Tap.builder()
                .tapId(BigInteger.ONE)
                .tapTime(LocalDateTime.now())
                .tapType(TapType.ON)
                .stopId(BigInteger.valueOf(1))
                .companyId("C1")
                .busId("B1")
                .pan("PAN1")
                .build();

        Tap tapOff = Tap.builder()
                .tapId(BigInteger.TWO)
                .tapTime(LocalDateTime.now().plusMinutes(10))
                .tapType(TapType.OFF)
                .stopId(BigInteger.valueOf(2))
                .companyId("C1")
                .busId("B1")
                .pan("PAN1")
                .build();

        when(fareServiceImpl.calculateFare(tapOn.getStopId(), tapOff.getStopId()))
                .thenReturn(BigDecimal.valueOf(5.00));

        Trip trip = tripService.createTripFromTheTaps(Optional.of(tapOn), Optional.of(tapOff));


        assertNotNull(trip);
        assertEquals(TripType.COMPLETED, trip.getStatus());
        assertEquals(BigDecimal.valueOf(5.00), trip.getChargeAmount());
        verify(tapRepository, times(1)).save(tapOn);
        verify(tapRepository, times(1)).save(tapOff);
        verify(tripRepository, times(1)).save(trip);
    }

    @Test
    void testCreateTripForCancelledTrip() throws InvalidTripException {

        Tap tapOn = Tap.builder()
                .tapId(BigInteger.ONE)
                .tapTime(LocalDateTime.now())
                .tapType(TapType.ON)
                .stopId(BigInteger.valueOf(1))
                .companyId("C1")
                .busId("B1")
                .pan("PAN1")
                .build();

        Tap tapOff = Tap.builder()
                .tapId(BigInteger.TWO)
                .tapTime(LocalDateTime.now().plusMinutes(10))
                .tapType(TapType.OFF)
                .stopId(BigInteger.valueOf(1))
                .companyId("C1")
                .busId("B1")
                .pan("PAN1")
                .build();

        Trip trip = tripService.createTripFromTheTaps(Optional.of(tapOn), Optional.of(tapOff));

        assertNotNull(trip);
        assertEquals(TripType.CANCELLED, trip.getStatus());
        assertEquals(BigDecimal.valueOf(0.00), trip.getChargeAmount());
        verify(tapRepository, times(1)).save(tapOn);
        verify(tapRepository, times(1)).save(tapOff);
        verify(tripRepository, times(1)).save(trip);
    }

    @Test
    void testCreateTripFromOnlyTapOn() throws InvalidTripException {
        Tap tapOn = Tap.builder()
                .tapId(BigInteger.ONE)
                .tapTime(LocalDateTime.now())
                .tapType(TapType.ON)
                .stopId(BigInteger.valueOf(1))
                .companyId("C1")
                .busId("B1")
                .pan("PAN1")
                .build();

        when(fareServiceImpl.calculateMaxPossibleFare(tapOn.getStopId()))
                .thenReturn(BigDecimal.valueOf(10.00));


        Trip trip = tripService.createTripFromTheTaps(Optional.of(tapOn), Optional.empty());

        assertNotNull(trip);
        assertEquals(TripType.INCOMPLETE, trip.getStatus());
        assertEquals(BigDecimal.valueOf(10.00), trip.getChargeAmount());
        verify(tapRepository, times(1)).save(tapOn);
        verify(tripRepository, times(1)).save(trip);
    }

    @Test
    void testCreateTripFromOnlyTapOff() throws InvalidTripException {
        Tap tapOff = Tap.builder()
                .tapId(BigInteger.TWO)
                .tapTime(LocalDateTime.now().plusMinutes(10))
                .tapType(TapType.OFF)
                .stopId(BigInteger.valueOf(1))
                .companyId("C1")
                .busId("B1")
                .pan("PAN1")
                .build();

        when(fareServiceImpl.calculateMaxPossibleFare(tapOff.getStopId()))
                .thenReturn(BigDecimal.valueOf(10.00));

        Trip trip = tripService.createTripFromTheTaps(Optional.empty(), Optional.of(tapOff));

        assertNotNull(trip);
        assertEquals(TripType.INCOMPLETE, trip.getStatus());
        assertEquals(BigDecimal.valueOf(10.00), trip.getChargeAmount());
        verify(tapRepository, times(1)).save(tapOff);
        verify(tripRepository, times(1)).save(trip);
    }

}