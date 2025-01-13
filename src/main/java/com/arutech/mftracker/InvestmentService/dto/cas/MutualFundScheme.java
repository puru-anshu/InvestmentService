package com.arutech.mftracker.InvestmentService.dto.cas;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MutualFundScheme {
    private String folioNo;
    private String isin;
    private String schemeName;
    private double costValue;
    private double unitBalance;
    private String navDate;
    private double nav;
    private double marketValue;
    private String registrar;
}
