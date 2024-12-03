package com.ltlpay.travel_fares.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "taps")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tap {
    @Id
    private BigInteger tapId;
    private BigInteger stopId;
    private LocalDateTime tapTime;
    @Enumerated(EnumType.STRING)
    private TapType tapType;
    private String tapDataFileId;
    private String pan;
    private String busId;
    private String companyId;
    private Boolean isMappedToTrip;

}
