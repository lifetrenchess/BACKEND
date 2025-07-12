package cts.travelpackagebookingsystem.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDTO {
    private Long packageId;
    private String packageName;
    private String destination;
    private String description;
    private Double price;
    private Integer duration;
    private Boolean active;
} 