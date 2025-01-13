package com.arutech.mftracker.InvestmentService.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Getter
@Setter
//@CompoundIndexes({
//        @CompoundIndex(name = "unique_idx_investment",
//                def = "{'userId': 1, 'schemeName': 1, 'units': 1,'date': 1,'transactionDescription':1}", unique = true)
//})
public class Investment {

    @Id
    private String investmentId;
    private String userId;
    private String schemeName;
    private String folioNumber;
    private String productCode;
    private String amcName;
    private String fundType;
    private String tradeDate;
    private String transactionType;
    private double amount;
    private double units;
    private double price;
    private String brokerName;
    private String transactionDescription;
    private String investorName;
    private String PAN;
    private String broker;

}