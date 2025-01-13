package com.arutech.mftracker.InvestmentService.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "FundDetail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FundDetail {
    @Id
    String id;
    String schemeName;
    String dataPlanId;

    String schemeCode;
    String amcName;
    String amcCode;
    String isin;

    String endType;
    String category;
    String primaryCategory;
    String investmentType;//direct or regular

    String benchmark;

    String riskometerScheme;
    String riskometerBenchmark;

    String nav;
    String navDate;

    String dailyAum;


    List<Return> returns;
    List<Return> returnsBenchmark;

    List<Return> returnsDirect;
    List<Return> returnsBenchmarkDirect;


}
