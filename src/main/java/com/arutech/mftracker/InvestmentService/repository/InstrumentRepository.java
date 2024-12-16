package com.arutech.mftracker.InvestmentService.repository;

import com.arutech.mftracker.InvestmentService.model.FundMetadata;
import com.arutech.mftracker.InvestmentService.model.MFInstrument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InstrumentRepository extends MongoRepository<MFInstrument, String> {
    List<MFInstrument> findByAmcIgnoreCase(String amc);


    List<MFInstrument> findByNameIgnoreCase(String name);

    MFInstrument findByTradingsymbol(String tradingsymbol);

}
