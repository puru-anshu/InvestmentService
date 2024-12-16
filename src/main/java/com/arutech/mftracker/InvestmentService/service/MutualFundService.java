package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.dto.MutualFundDetails;
import com.arutech.mftracker.InvestmentService.exception.MutualFundNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MutualFundService {
    private RestTemplate restTemplate;

    public MutualFundService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MutualFundDetails getMutualFundDetails(String tradingSymbol) throws MutualFundNotFoundException {
        return null;
    }
}
