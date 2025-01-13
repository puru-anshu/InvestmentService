package com.arutech.mftracker.InvestmentService.controller;

import com.arutech.mftracker.InvestmentService.dto.FundSearchCriteria;
import com.arutech.mftracker.InvestmentService.model.Scheme;
import com.arutech.mftracker.InvestmentService.service.SchemeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schemes")
@RequiredArgsConstructor
@Slf4j
public class SchemeController {
    private final SchemeService service;

    @GetMapping("/{id}")
    public ResponseEntity<Scheme> getFund(@PathVariable String id) {
        return ResponseEntity.ok(service.getFundById(id));
    }

    @PostMapping("/search")
    public ResponseEntity<List<Scheme>> searchFunds(@RequestBody FundSearchCriteria criteria) {
        return ResponseEntity.ok(service.searchFunds(criteria));
    }


    @GetMapping("/isin/{isin}")
    public ResponseEntity<Scheme> getFundByIsin(@PathVariable String isin) {
        log.info("Getting scheme for isin "+isin);
        Optional<Scheme> fundByIsin = service.getFundByIsin(isin);
        log.info("Scheme " +fundByIsin );
        return fundByIsin.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }
}
