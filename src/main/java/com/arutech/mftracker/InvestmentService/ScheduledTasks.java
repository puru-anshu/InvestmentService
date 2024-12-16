package com.arutech.mftracker.InvestmentService;


import com.arutech.mftracker.InvestmentService.model.MFInstrument;
import com.arutech.mftracker.InvestmentService.model.Portfolio;
import com.arutech.mftracker.InvestmentService.repository.InstrumentRepository;
import com.arutech.mftracker.InvestmentService.repository.PortfolioRepository;
import com.arutech.mftracker.InvestmentService.service.MFInstrumentService;
import com.arutech.mftracker.InvestmentService.util.CosineSimilarity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {
    private final MFInstrumentService mutualFundService;


    public ScheduledTasks(MFInstrumentService mutualFundService) {
        this.mutualFundService = mutualFundService;
    }

    @Scheduled(fixedRate = 3600000) // Run every hour (adjust as needed)
    public void fetchAndSaveMutualFunds() {
        log.info("Fetching all mutual fund details");
        try {
            mutualFundService.fetchAndStoreInstruments();
        } catch (IOException e) {
            log.warn("Error fetching mutual funds: {}", e.getMessage());
        }
    }

    /**
     * Every 2 minutes, look for portfolios without a scheme code,
     * and try to find the corresponding scheme code in the
     * fund metadata database. If we can find a unique match,
     * update the portfolio with the scheme code.
     */
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private InstrumentRepository instrumentRepository;


    @Scheduled(fixedRate = 1000000000)
    public void updateSchemeCode() {
        List<Portfolio> portfolios = portfolioRepository.findByTradingsymbolIsNull();
        log.info("updating scheme code for {} portfolios", portfolios.size());
        for (Portfolio portfolio : portfolios) {
            log.info("updating scheme code for {}", portfolio.getSchemeName());
            String schemeName = portfolio.getSchemeName();
            MFInstrument instrument = findBestMatch(schemeName,
                    instrumentRepository.findAll());
            if (null != instrument) {
                log.info("found best match for {} == {}", schemeName, instrument.getName());
                portfolio.setTradingsymbol(instrument.getTradingsymbol());
                portfolio.setCurrentValue(instrument.getLastPrice() * portfolio.getUnits());
                portfolio.setLastUpdateDate(instrument.getLastPriceDate());
                portfolio.setSchemeCode(instrument.getSchemeCode());
                portfolioRepository.save(portfolio);
            }

        }
    }


    private MFInstrument findBestMatch(String name, List<MFInstrument> portfolios) {

        MFInstrument bestMatch = null;
        double bestScore = 0.5d;
        for (MFInstrument portfolio : portfolios) {
            double score = CosineSimilarity.positionalWeightedSimilarity(name.toLowerCase().trim(), portfolio.getName().toLowerCase().trim());
           // log.info("score for {} and {} is {}", name, portfolio.getName(), score);
            if (score > bestScore) {
                bestScore = score;
                bestMatch = portfolio;
            }
        }
        return bestMatch;
    }
}