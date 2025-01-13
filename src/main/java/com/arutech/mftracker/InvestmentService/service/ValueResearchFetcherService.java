package com.arutech.mftracker.InvestmentService.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ValueResearchFetcherService {

    public void fetchMutualFunds() {
        String paramEndType = "end-type";
        String paramPrimaryCategory = "primary-category";
        String paramCategory = "category";
        String amc = "ALL";
        String navDate = "nav-date";

    }

    public void fetchMutualPerformanceDetails(String endType, String primaryCategory, String category, String amc, String navDate) {
        String url = "https://www.valueresearchonline.com/amfi/fund-performance-data/?" +
                "end-type=" +
                "#end-type#" +
                "&primary-category=#primary-category#" +
                "&category=#category#" +
                "&amc=#amc#" +
                "&nav-date=#nav-date#";
        url = url.replace("#end-type#", endType);
        url = url.replace("#primary-category#", primaryCategory);
        url = url.replace("#category#", category);
        url = url.replace("#amc#", amc);
        url = url.replace("#nav-date#", navDate);

        log.info("Fetching data from url {}", url);
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//        headers.add("Accept-Language", "en-US,en;q=0.5");
//        headers.add("Accept-Encoding", "gzip, deflate, br");
//        headers.add("Connection", "keep-alive");
        headers.add("Cookie", "vropopup=1");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);
        log.info(response.getBody());

    }

    public static void main(String[] args) {
        new ValueResearchFetcherService().fetchMutualPerformanceDetails("0",
                "SEQ", "SEQ_LMC", "ALL", "27-Dec-2024");

    }

}
