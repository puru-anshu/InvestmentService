package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.dto.MutualFundDetails;
import com.arutech.mftracker.InvestmentService.dto.NavData;
import com.arutech.mftracker.InvestmentService.dto.SearchResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FundMetadataFetcherService {
//    private static final String API_URL = "https://api.mfapi.in/mf";
//    private static final String ISIN_URL = "https://www.amfiindia.com/spages/NAVOpen.txt";

    private static final String AMFI_NAV_URL = "https://www.amfiindia.com/spages/NAVAll.txt";
    //    private static final String AMFI_SEARCH_URL = "https://api.mfapi.in/mf/search?q=";
    private static final String AMFI_API_URL = "https://api.mfapi.in/mf/";
    //    private static final String AMFI_API_LATEST_URL = "https://api.mfapi.in/mf/{scheme_code}/latest";

    private static final String SEARCH_BASE_URL = "https://api.mfapi.in/mf/search?";


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;



    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper= new ObjectMapper();
    }



    public MutualFundDetails fetchMutualFundDetails(String schemeCode) {
        String url = AMFI_API_URL + schemeCode;
        return restTemplate.getForObject(url, MutualFundDetails.class);
    }

    public NavData getLatestNavData(String schemeCode) {
        MutualFundDetails details = fetchMutualFundDetails(schemeCode);
        return details != null && !details.getHistoricalData().isEmpty()
                ? details.getHistoricalData().get(0)
                : null;
    }



    public List<SearchResult> searchMutualFunds(String query) {
        // Build URL with query parameter
        String url = UriComponentsBuilder.fromHttpUrl(SEARCH_BASE_URL)
                .queryParam("q", query)
                .toUriString();
        log.info(url);
        try {
            // Fetch raw response as String
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            // Parse response body to JSON node
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // Initialize list to hold search results
            List<SearchResult> searchResults = new ArrayList<>();

            // Iterate through JSON array and map each object to SearchResult
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    SearchResult result = objectMapper.treeToValue(node, SearchResult.class);
                    searchResults.add(result);
                }
            }

            return searchResults;

        } catch (Exception e) {
            // Log the error
            log.warn("Error searching mutual funds: {}", e.getMessage());
            return new ArrayList<>();
        }
    }


}