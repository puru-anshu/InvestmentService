package com.arutech.mftracker.InvestmentService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MutualFundSearchResult {
    @JsonProperty("scheme_code")
    private String schemeCode;

    @JsonProperty("scheme_name")
    private String schemeName;

    @JsonProperty("fund_house")
    private String fundHouse;

    @JsonProperty("scheme_type")
    private String schemeType;

    @JsonProperty("scheme_category")
    private String schemeCategory;
}
