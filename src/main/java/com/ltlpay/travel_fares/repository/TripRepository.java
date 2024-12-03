package com.ltlpay.travel_fares.repository;

import com.ltlpay.travel_fares.entity.Tap;
import com.ltlpay.travel_fares.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;
@Repository
public interface TripRepository extends JpaRepository<Trip, BigInteger> {
}
