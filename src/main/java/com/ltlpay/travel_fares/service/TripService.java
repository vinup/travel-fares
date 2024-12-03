package com.ltlpay.travel_fares.service;

import com.ltlpay.travel_fares.entity.Tap;
import com.ltlpay.travel_fares.entity.Trip;
import com.ltlpay.travel_fares.exception.InvalidTripException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TripService {
    public Trip createTripFromTheTaps(Optional<Tap> tapOn, Optional<Tap> tapOff) throws InvalidTripException;
}
