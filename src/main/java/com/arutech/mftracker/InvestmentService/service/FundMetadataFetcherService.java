package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.dto.*;
import com.arutech.mftracker.InvestmentService.model.FundMetadata;
import com.arutech.mftracker.InvestmentService.repository.FundMetadataRepository;
import com.arutech.mftracker.InvestmentService.util.CosineSimilarity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
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
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FundMetadataRepository mfSchemeRepository;

    @PostConstruct
    public void initializeMutualFunds() {
        //fetchAndSaveMutualFunds();
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper= new ObjectMapper();
    }

    public void fetchAndSaveMutualFunds() {
        List<FundMetadata> mutualFunds = extractNavData();
        mfSchemeRepository.saveAll(mutualFunds);
    }

    public List<FundMetadata> extractNavData() {
        String rawData = restTemplate.getForObject(AMFI_NAV_URL, String.class);
        return parseNavData(rawData);
    }

    private List<FundMetadata> parseNavData(String rawData) {
        List<FundMetadata> navDataList = new ArrayList<>();

        if (rawData == null) {
            return navDataList;
        }
        String[] lines = rawData.split("\n");
        String currentFundHouse = null;

        for (String line : lines) {
            // Skip header or empty lines
            if (line.trim().isEmpty() || line.startsWith("Scheme")) {
                continue;
            }

            String[] fields = line.split(";");

            // Lines without commas are fund house names
            if (fields.length == 1) {
                currentFundHouse = fields[0].trim();
                continue;
            }

            // Process mutual fund data lines
            if (fields.length >= 3 && currentFundHouse != null) {
                try {
                    FundMetadata navData = new FundMetadata();
                    navData.setSchemeCode(fields[0].trim());
                    navData.setSchemeName(fields[3].trim());
                    navData.setIsin(fields[1].trim());
                    navData.setIsinR(fields[2].trim());
                    navData.setFundHouse(currentFundHouse);
                    navDataList.add(navData);
                } catch (Exception e) {
                    // Optional: Log parsing errors
                    log.warn("Error parsing line: " + line);
                }
            }
        }

        return navDataList;
    }

    public MutualFundDetails fetchMutualFundDetails(String schemeCode) {
        String url = AMFI_API_URL + schemeCode;
//        log.info(url);
        return restTemplate.getForObject(url, MutualFundDetails.class);
    }

    public NavData getLatestNavData(String schemeCode) {
        MutualFundDetails details = fetchMutualFundDetails(schemeCode);
        return details != null && !details.getHistoricalData().isEmpty()
                ? details.getHistoricalData().get(0)
                : null;
    }

    private static final String SEARCH_BASE_URL = "https://api.mfapi.in/mf/search?";

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

    public static void main(String[] args) {

        FundMetadataFetcherService service = new FundMetadataFetcherService();
        service.setRestTemplate(new RestTemplate());
        String query = "quant Active Fund".toLowerCase();
        List<SearchResult> mutualFundSearchResults = service.searchMutualFunds("quant ac");
        System.out.println("mutualFundSearchResults = " + mutualFundSearchResults);
        for(SearchResult result : mutualFundSearchResults) {
            double v = CosineSimilarity.positionalWeightedSimilarity(query, result.getSchemeName().toLowerCase());
            System.out.println(query + " <> " + result.getSchemeName() + " is " + v);
        }

    }
}