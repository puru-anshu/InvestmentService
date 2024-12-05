package com.arutech.mftracker.InvestmentService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FundMetadata {
    @Id
    private  String schemeCode;
    private  String schemeName;
    private  String isin ;
    private  String isinR;
    private  String fundHouse;
}
