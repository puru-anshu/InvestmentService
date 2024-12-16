package com.arutech.mftracker.InvestmentService.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "portfolio")
@Getter
@Setter
@NoArgsConstructor
@ToString
@CompoundIndexes({
        @CompoundIndex(name = "unique_idx_portfolio",
                def = "{'userId': 1, 'schemeName': 1, 'amcName': 1,'folioNumber': 1}", unique = true)
})
public class Portfolio {
    @Id
    String id;
    String userId;
    String schemeName;
    String amcName;
    String Category;
    String folioNumber;
    int units;
    double investedValue;
    String schemeCode;
    String tradingsymbol;
    double currentValue;
    String lastUpdateDate;
}
