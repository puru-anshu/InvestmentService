package com.arutech.mftracker.InvestmentService.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mf_instruments")
@Setter
@Getter
@NoArgsConstructor

public class MFInstrument {
    @Id
    private String id; // Assuming 'id' is present and unique for each instrument
    private String name;
    private String amc;
    private String schemeType;
    private String tradingsymbol;
    private String plan;
    private String settlementtype;
    private double lastPrice;
    private String lastPriceDate;

}
