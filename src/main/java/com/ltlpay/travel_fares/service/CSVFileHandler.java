package com.ltlpay.travel_fares.service;

import com.ltlpay.travel_fares.domain.RawTap;
import com.ltlpay.travel_fares.entity.Tap;
import com.ltlpay.travel_fares.entity.TapType;
import com.ltlpay.travel_fares.entity.Trip;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CSVFileHandler {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public List<Tap> readFromInputFile(String inputFilePath, String tapDataFileId) throws Exception {
        List<Tap> tapList = new ArrayList<>();
        try {
            Reader reader = new FileReader(inputFilePath);
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
                        .tapTime(LocalDateTime.parse(rawTap.getTapTime(), FORMATTER))
                        .companyId(rawTap.getCompanyId())
                        .busId(rawTap.getBusId())
                        .stopId(rawTap.getStopId())
                        .isMappedToTrip(Boolean.FALSE)
                        .build();
                tapList.add(tap);
            }
            reader.close();
        } catch (IOException exception) {
            log.error("Error generating Tap Data from the input file: {}", inputFilePath, exception);
            throw exception;
        }
        return tapList;
    }

    public void writeToOutputFile(List<Trip> trips, String outputFilePath) throws Exception {
        try {
            Writer writer = new FileWriter(outputFilePath);
            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeNext(new String[]{"Started", "Finished", "FromStopId", "ToStopId", "ChargeAmount", "CompanyId", "BusId", "Pan"});
            for (Trip trip : trips) {
                csvWriter.writeNext(new String[]{
                        String.valueOf(trip.getStarted()),
                        String.valueOf(trip.getFinished()),
                        String.valueOf(trip.getFromStopId()),
                        String.valueOf(trip.getToStopId()),
                        String.valueOf(trip.getChargeAmount()),
                        trip.getCompanyId(),
                        trip.getBusId(),
                        trip.getStatus().getValue(),
                        trip.getPan()
                });
            }
            log.info("Trips written to {}", outputFilePath);
            csvWriter.close();
            writer.close();

        } catch (Exception exception) {
            log.error("Error writing to output file: {}", outputFilePath, exception);
        }
    }
}
