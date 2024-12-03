package com.ltlpay.travel_fares.service;

import com.ltlpay.travel_fares.entity.Tap;
import com.ltlpay.travel_fares.entity.TapType;
import com.ltlpay.travel_fares.entity.Trip;
import com.ltlpay.travel_fares.entity.TripType;
import com.ltlpay.travel_fares.exception.InvalidTripException;
import com.ltlpay.travel_fares.repository.TapRepository;
import com.ltlpay.travel_fares.repository.TripRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class TripServiceImpl implements TripService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TapRepository tapRepository;
    @Autowired
    private FareServiceImpl fareServiceImpl;

    @Override
    public Trip createTripFromTheTaps(Optional<Tap> tapOn, Optional<Tap> tapOff) throws InvalidTripException {

        if (tapOn.isEmpty() && tapOff.isEmpty()) {
            throw new InvalidTripException("Not Valid Tap Scenario");
        } else if (tapOn.isPresent() && tapOff.isPresent()) {
            return createCompletedTrip(tapOn, tapOff);
        } else {
            Tap tripTap = tapOn.isPresent() ? tapOn.get() : tapOff.get();
            return createIncompleteTrip(tripTap);
        }

    }

    private Trip createCompletedTrip(Optional<Tap> tapOn, Optional<Tap> tapOff) {
        Tap tripTapOn = tapOn.get();
        Tap tripTapOff = tapOff.get();

        Trip trip = Trip.builder()
                .started(tripTapOn.getTapTime())
                .finished(tripTapOff.getTapTime())
                .fromStopId(tripTapOn.getStopId())
                .toStopId(tripTapOff.getStopId())
                .companyId(tripTapOn.getCompanyId())
                .busId(tripTapOn.getBusId())
                .pan(tripTapOn.getPan())
                .status(TripType.COMPLETED)
                .build();
        if (Objects.equals(trip.getFromStopId(), trip.getToStopId())) {
            trip.setStatus(TripType.CANCELLED);
            trip.setChargeAmount(BigDecimal.valueOf(0.00));
        } else {
            trip.setChargeAmount(fareServiceImpl.calculateFare(tripTapOn.getStopId(), tripTapOff.getStopId()));
        }
        tripTapOn.setIsMappedToTrip(Boolean.TRUE);
        tripTapOff.setIsMappedToTrip(Boolean.TRUE);
        tapRepository.save(tripTapOn);
        tapRepository.save(tripTapOff);
        tripRepository.save(trip);
        return trip;
    }

    private Trip createIncompleteTrip(Tap tripTap) {
        Trip trip = Trip.builder()
                .started(tripTap.getTapTime())
                .companyId(tripTap.getCompanyId())
                .busId(tripTap.getBusId())
                .pan(tripTap.getPan())
                .status(TripType.INCOMPLETE)
                .build();
        if (tripTap.getTapType().equals(TapType.ON)) {
            trip.setFromStopId(tripTap.getStopId());
            trip.setStarted(tripTap.getTapTime());
        } else {
            trip.setToStopId(tripTap.getStopId());
            trip.setFinished(tripTap.getTapTime());
        }
        trip.setChargeAmount(fareServiceImpl.calculateMaxPossibleFare(tripTap.getStopId()));
        tripTap.setIsMappedToTrip(Boolean.TRUE);
        tripRepository.save(trip);
        tapRepository.save(tripTap);
        return trip;
    }
}


