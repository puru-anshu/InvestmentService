package com.arutech.mftracker.InvestmentService;


import com.arutech.mftracker.InvestmentService.service.FundMetadataFetcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {
    private final FundMetadataFetcherService mutualFundService;
    public ScheduledTasks(FundMetadataFetcherService mutualFundService) {
        this.mutualFundService = mutualFundService;
    }

    @Scheduled(fixedRate = 3600000) // Run every hour (adjust as needed)
    public void fetchAndSaveMutualFunds() {
        log.info("Fetching all mutual fund details");
        mutualFundService.fetchAndSaveMutualFunds();
    }
}