package com.ltlpay.travel_fares.domain;

import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RawTap {
    private BigInteger tapId;
    private BigInteger stopId;
    private String tapTime;
    private String tapType;
    private String pan;
    private String busId;
    private String companyId;
}
