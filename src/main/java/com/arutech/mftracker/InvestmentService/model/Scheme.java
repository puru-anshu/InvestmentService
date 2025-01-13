package com.arutech.mftracker.InvestmentService.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "schemes")
public class Scheme {

    @Id
    private String id;

    @Field("end_type")
    private String endType;

    @Field("primary_category")
    private String primaryCategory;

    @Field("category")
    private String category;

    @Field("scheme")
    private String scheme;

    @Field("Benchmark")
    private String benchmark;

    @Field("Riskometer-Scheme")
    private String riskometerScheme;

    @Field("Riskometer-Benchmark")
    private String riskometerBenchmark;

    @Field("Nav-Regular")
    private String navRegular;

    @Field("Nav-Regular_data_nav_reg")
    private String navRegularDataNavReg;

    @Field("Nav-Regular_data_prev_nav_reg")
    private String navRegularDataPrevNavReg;

    @Field("Nav-Direct")
    private String navDirect;

    @Field("Nav-Direct_data_nav_dir")
    private String navDirectDataNavDir;

    @Field("Nav-Direct_data_prev_nav_dir")
    private String navDirectDataPrevNavDir;

    @Field("Return-Regular")
    private String returnRegular;

    @Field("Return-Regular_data-1y")
    private String returnRegularData1y;

    @Field("Return-Regular_data-3y")
    private String returnRegularData3y;

    @Field("Return-Regular_data-5y")
    private String returnRegularData5y;

    @Field("Return-Regular_data-10y")
    private String returnRegularData10y;

    @Field("Return-Regular_data-sl")
    private String returnRegularDataSl;

    @Field("Return-Direct")
    private String returnDirect;

    @Field("Return-Direct_data-1y")
    private String returnDirectData1y;

    @Field("Return-Direct_data-3y")
    private String returnDirectData3y;

    @Field("Return-Direct_data-5y")
    private String returnDirectData5y;

    @Field("Return-Direct_data-10y")
    private String returnDirectData10y;

    @Field("Return-Direct_data-sl")
    private String returnDirectDataSl;

    @Field("Return-BenchMark")
    private String returnBenchmark;

    @Field("Return-BenchMark_data-1y")
    private String returnBenchmarkData1y;

    @Field("Return-BenchMark_data-3y")
    private String returnBenchmarkData3y;

    @Field("Return-BenchMark_data-5y")
    private String returnBenchmarkData5y;

    @Field("Return-BenchMark_data-10y")
    private String returnBenchmarkData10y;

    @Field("Return-BenchMark_data-sl")
    private String returnBenchmarkDataSl;

    @Field("period-return-bench-direct")
    private String periodReturnBenchDirect;

    @Field("period-return-bench-direct_data-1y")
    private String periodReturnBenchDirectData1y;

    @Field("period-return-bench-direct_data-3y")
    private String periodReturnBenchDirectData3y;

    @Field("period-return-bench-direct_data-5y")
    private String periodReturnBenchDirectData5y;

    @Field("period-return-bench-direct_data-10y")
    private String periodReturnBenchDirectData10y;

    @Field("period-return-bench-direct_data-sl")
    private String periodReturnBenchDirectDataSl;

    @Field("Daily AUM")
    private String dailyAum;

    @Field("Daily Aum_data_order")
    private String dailyAumDataOrder;

    @Field("regular_isin")
    private String regularIsin;

    @Field("direct_isin")
    private String directIsin;

    @Field("data_plan_id")
    private Integer dataPlanId;
}
