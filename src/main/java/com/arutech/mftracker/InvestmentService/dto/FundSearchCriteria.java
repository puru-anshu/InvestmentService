package com.arutech.mftracker.InvestmentService.dto;

import lombok.Data;

@Data
public class FundSearchCriteria {
    private String schemeName;
    private String category;
    private String primaryCategory;
    private String amcName;
    private String investmentType;
    private String isin;
    private Double minReturn1Y;
    private Double maxReturn1Y;
    private Integer page;
    private Integer size;
}
