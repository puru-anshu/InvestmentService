package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.exception.MutualFundNotFoundException;
import com.arutech.mftracker.InvestmentService.model.MFInstrument;
import com.arutech.mftracker.InvestmentService.repository.InstrumentRepository;
import org.springframework.stereotype.Service;

@Service
public class MutualFundService {
    private InstrumentRepository repository;

    public MutualFundService(InstrumentRepository repository) {
        this.repository = repository;
    }

    public MFInstrument getMutualFundDetails(String tradingSymbol) throws MutualFundNotFoundException {
       return repository.findByTradingsymbol(tradingSymbol);
    }


}
