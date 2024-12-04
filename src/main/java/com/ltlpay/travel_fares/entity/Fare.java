package com.ltlpay.travel_fares.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "fares")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fare {
    @Id
    @Generated
    private Long id;
    private Integer fareId;
    private BigDecimal fare;
    private Integer sourceStopId;
    private Integer destinationStopId;
}
