package com.arutech.mftracker.InvestmentService.dto.cas;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class EcasReport {
    private InvestorDetails investorDetails;
    private List<MutualFundScheme> schemes;
    private String reportDate;
}
