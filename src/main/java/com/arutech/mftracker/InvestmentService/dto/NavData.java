package com.arutech.mftracker.InvestmentService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NavData {
    @JsonProperty("date")
    private String date;

    @JsonProperty("nav")
    private double nav;
}
