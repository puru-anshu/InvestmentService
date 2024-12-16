package com.arutech.mftracker.InvestmentService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MutualFundSearchResponse {
    @JsonProperty("mutual_funds")
    private List<MutualFundSearchResult> results;

    public List<MutualFundSearchResult> getResults() {
        return results;
    }
}
