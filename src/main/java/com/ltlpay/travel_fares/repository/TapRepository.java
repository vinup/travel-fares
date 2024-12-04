package com.ltlpay.travel_fares.repository;

import com.ltlpay.travel_fares.entity.Tap;
import com.ltlpay.travel_fares.entity.TapType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface TapRepository extends JpaRepository<Tap, BigInteger> {

    Optional<Tap> findFirstByPanAndBusIdAndTapDataFileIdAndTapTypeAndIsMappedToTripOrderByTapTimeAsc(
            String pan,
            String busId,
            String tapDataFileId,
            TapType tapType,
            Boolean isMappedToTrip
    );

    Optional<Tap> findById(BigInteger tapId);
}
