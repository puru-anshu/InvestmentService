package com.arutech.mftracker.InvestmentService.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


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
//    private String amcName;
//    private String category;
//    private String folioNo;
//    private double investedValue;
//    private double currentValue;
//    private double returns;
    private String units;
    private String transactionDescription;
    private String date;
    private String nav;
    private String amount;
}