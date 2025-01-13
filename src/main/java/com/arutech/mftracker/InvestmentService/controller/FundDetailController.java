package com.arutech.mftracker.InvestmentService.controller;

import com.arutech.mftracker.InvestmentService.dto.FundSearchCriteria;
import com.arutech.mftracker.InvestmentService.model.FundDetail;
import com.arutech.mftracker.InvestmentService.service.FundDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mfunds")
@RequiredArgsConstructor
public class FundDetailController {
    private final FundDetailService service;

    @GetMapping("/{id}")
    public ResponseEntity<FundDetail> getFund(@PathVariable String id) {
        return ResponseEntity.ok(service.getFundById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<List<FundDetail>> searchFunds(@RequestBody FundSearchCriteria criteria) {
        return ResponseEntity.ok(service.searchFunds(criteria));
    }

    @PostMapping
    public ResponseEntity<FundDetail> createFund(@RequestBody FundDetail fund) {
        return ResponseEntity.ok(service.saveFund(fund));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FundDetail> updateFund(
            @PathVariable String id,
            @RequestBody FundDetail fund) {
        fund.setId(id);
        return ResponseEntity.ok(service.saveFund(fund));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFund(@PathVariable String id) {
        service.deleteFund(id);
        return ResponseEntity.noContent().build();
    }
}
