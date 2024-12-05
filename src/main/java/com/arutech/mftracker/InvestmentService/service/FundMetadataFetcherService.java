package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.model.FundMetadata;
import com.arutech.mftracker.InvestmentService.repository.FundMetadataRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class FundMetadataFetcherService {
    private static final String API_URL = "https://api.mfapi.in/mf";
    private static final String ISIN_URL = "https://www.amfiindia.com/spages/NAVOpen.txt";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FundMetadataRepository mfSchemeRepository;

    @PostConstruct
    public void initializeMutualFunds() {
        readISINFile();
    }

    public void readISINFile() {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(ISIN_URL).openStream()));) {
            String line;
            int count = 0;
            List<FundMetadata> schemeList = new ArrayList<>();
            String fundHouse = null;
            while ((line = reader.readLine()) != null) {
                // Process each line as needed
                if (++count == 0) continue;
                if (line.endsWith("Fund")) {
                    fundHouse = line.trim();
                }

                String[] parts = line.split(";");

                if (parts.length > 3) {
                    FundMetadata mfScheme = new FundMetadata();
                    mfScheme.setSchemeCode(parts[0]);
                    mfScheme.setIsin(parts[1]);
                    mfScheme.setIsinR(parts[2]);
                    mfScheme.setSchemeName(parts[3]);
                    mfScheme.setFundHouse(fundHouse);
                    schemeList.add(mfScheme);

                }
            }
            log.info("Scheme list size" + schemeList.size());
            mfSchemeRepository.saveAll(schemeList);
            reader.close();
        } catch (IOException e) {
            // Handle exceptions (e.g., MalformedURLException, IOException)
            log.warn("Could not load fund metadata ", e);
        }
    }


    public void fetchAndSaveMutualFunds() {
        FundMetadata[] mutualFundsArray = restTemplate.getForObject(API_URL, FundMetadata[].class);
        if (mutualFundsArray != null) {
            List<FundMetadata> mutualFunds = Arrays.asList(mutualFundsArray);
            mfSchemeRepository.saveAll(mutualFunds);
        }
    }
}
