package com.ltlpay.travel_fares.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tapDataFile")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TapDataFile {
    @Id
    @Generated
    private String fileId;
    private String fileName;
    private LocalDateTime processedTime;
    private String status;
}
