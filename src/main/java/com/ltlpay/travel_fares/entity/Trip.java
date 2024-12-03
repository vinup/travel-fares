package com.ltlpay.travel_fares.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private BigInteger id;
    private LocalDateTime started;
    private LocalDateTime finished;
    private BigInteger fromStopId;
    private BigInteger toStopId;
    private BigDecimal chargeAmount;
    private String companyId;
    private String busId;
    private String pan;
    @Enumerated(EnumType.STRING)
    private TripType status;
}
