package com.arutech.mftracker.InvestmentService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MutualFundDetails {
    @JsonProperty("meta")
    private MetaData metaData;
    @JsonProperty("data")
    private List<NavData> historicalData;
}
