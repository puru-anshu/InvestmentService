package com.arutech.mftracker.InvestmentService.controller;

import com.arutech.mftracker.InvestmentService.dto.MutualFundDetails;
import com.arutech.mftracker.InvestmentService.dto.MutualFundSearchResult;
import com.arutech.mftracker.InvestmentService.dto.NavData;
import com.arutech.mftracker.InvestmentService.dto.SearchResult;
import com.arutech.mftracker.InvestmentService.service.FundMetadataFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MutualFundSearchController {
    @Autowired
    private FundMetadataFetcherService searchService;

    @GetMapping("/fund/search")
    public List<SearchResult> searchMutualFunds(
            @RequestParam(name = "query") String query) {
        return searchService.searchMutualFunds(query);

    }

    @GetMapping("/fund/latestnav")
    public ResponseEntity<NavData> getLatestNav(@RequestParam(name = "schemeCode") String schemeCode) {
        NavData navData = searchService.getLatestNavData(schemeCode);
        return navData != null ? ResponseEntity.ok(navData) : ResponseEntity.notFound().build();
    }

    @GetMapping("/fund/nav")
    public ResponseEntity<MutualFundDetails> getNavDetails(@RequestParam(name = "schemeCode") String schemeCode) {
        MutualFundDetails navData = searchService.fetchMutualFundDetails(schemeCode);
        return navData != null ? ResponseEntity.ok(navData) : ResponseEntity.notFound().build();
    }

}
