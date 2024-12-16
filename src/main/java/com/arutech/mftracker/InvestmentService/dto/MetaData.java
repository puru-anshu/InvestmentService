package com.arutech.mftracker.InvestmentService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MetaData {
    @JsonProperty("scheme_code")
    private String schemeCode;

    @JsonProperty("scheme_name")
    private String schemeName;

    @JsonProperty("scheme_type")
    private String schemeType;

    @JsonProperty("scheme_category")
    private String schemeCategory;

    @JsonProperty("fund_house")
    private String fundHouse;
    @JsonProperty("nav")
    private double nav;
    @JsonProperty("aum")
    private double aum ;
    @JsonProperty("expense_ratio")
    private double expenseRatio;
    @JsonProperty("risk_level")
    private String riskLevel;


}
