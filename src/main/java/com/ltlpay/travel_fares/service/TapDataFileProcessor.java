package com.ltlpay.travel_fares.service;

import com.ltlpay.travel_fares.domain.RawTap;
import com.ltlpay.travel_fares.entity.Tap;
import com.ltlpay.travel_fares.entity.TapType;
import com.ltlpay.travel_fares.entity.Trip;
import com.ltlpay.travel_fares.entity.TripType;
import com.ltlpay.travel_fares.repository.TapRepository;
import com.ltlpay.travel_fares.repository.TripRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.Reader;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class TapDataFileProcessor {

    @Autowired
    TapRepository tapRepository;
    @Autowired
    TripService tripService;

    protected void processTapDataFile(String filePath) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String tapDataFileId = UUID.randomUUID().toString();
        List<Tap> tapList = new ArrayList<>();
        try (Reader reader = new FileReader(filePath)) {
            CsvToBean<RawTap> csvToBean = new CsvToBeanBuilder<RawTap>(reader)
                    .withType(RawTap.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<RawTap> rawTaps = csvToBean.parse();
            for (RawTap rawTap : rawTaps) {
                Tap tap = Tap.builder()
                        .pan(rawTap.getPan())
                        .tapDataFileId(tapDataFileId)
                        .tapId(rawTap.getTapId())
                        .tapType(TapType.valueOf(rawTap.getTapType()))
                        .tapTime(LocalDateTime.parse(rawTap.getTapTime(), formatter))
                        .companyId(rawTap.getCompanyId())
                        .busId(rawTap.getBusId())
                        .stopId(rawTap.getStopId())
                        .isMappedToTrip(Boolean.FALSE)
                        .build();
                tapList.add(tap);
            }
        } catch (Exception e) {
            log.error("Unable to process file {}", filePath, e);
        }

        List<Trip> processedTrips = processTaps(tapList);
        logTrips(processedTrips);
    }


    List<Trip> processTaps(List<Tap> tapList) {
        List<Trip> tripList = new ArrayList<>();
        tapList.sort(Comparator.comparing(Tap::getTapTime));
        tapRepository.saveAll(tapList);
        try {
            for (Tap tap : tapList) {
                log.info("Processing tap {}", tap.toString());
                Optional<Tap> tapToProcessOptional = tapRepository.findById(tap.getTapId());
                Tap tapToProcess = tapToProcessOptional.get();
                if (tapToProcess.getIsMappedToTrip() == null || Boolean.FALSE.equals(tapToProcess.getIsMappedToTrip())) {
                    Optional<Tap> mappedTap = Optional.empty();
                    if (tapToProcess.getTapType().equals(TapType.ON)) {
                        mappedTap = tapRepository.findFirstByPanAndBusIdAndTapDataFileIdAndTapTypeAndIsMappedToTripOrderByTapTimeAsc(
                                tapToProcess.getPan(),
                                tapToProcess.getBusId(),
                                tapToProcess.getTapDataFileId(),
                                TapType.OFF,
                                Boolean.FALSE);
                    }
                    if (TapType.ON.equals(tapToProcess.getTapType())) {
                        Trip trip = tripService.createTripFromTheTaps(tapToProcessOptional, mappedTap);
                        tripList.add(trip);
                    } else {
                        Trip trip = tripService.createTripFromTheTaps(mappedTap, tapToProcessOptional);
                        tripList.add(trip);
                    }
                } else {
                    log.info("tap: {} has already been mapped to a trip", tap.getTapId());
                }

            }
        } catch (Exception e) {
            log.error("Unable to process taps", e);
        }
        return tripList;
    }

    private void logTrips(List<Trip> processedTrips) {
        if (!processedTrips.isEmpty()) {
            processedTrips.sort(Comparator.comparing(Trip::getPan));
            for (Trip trip : processedTrips) {
                log.info("Trip: {} ", trip.toString());
            }

        }
    }


}
