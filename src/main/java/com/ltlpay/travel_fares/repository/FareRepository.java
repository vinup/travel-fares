package com.ltlpay.travel_fares.repository;

import com.ltlpay.travel_fares.entity.Fare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface FareRepository extends JpaRepository<Fare, BigInteger> {
    Optional<Fare> findBySourceStopIdAndDestinationStopId(BigInteger sourceStopId, BigInteger destinationStopId);

    @Query("SELECT MAX(fare) FROM Fare  WHERE sourceStopId = :stopId OR destinationStopId = :stopId")
    BigDecimal findMaxFareByStop(BigInteger stopId);
}
