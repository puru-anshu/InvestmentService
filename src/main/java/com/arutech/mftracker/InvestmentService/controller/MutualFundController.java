package com.arutech.mftracker.InvestmentService.controller;

import com.arutech.mftracker.InvestmentService.dto.MutualFundDetails;
import com.arutech.mftracker.InvestmentService.exception.MutualFundNotFoundException;
import com.arutech.mftracker.InvestmentService.service.MutualFundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mutual-funds")
public class MutualFundController {
    private final MutualFundService mutualFundService;

    public MutualFundController(MutualFundService mutualFundService) {
        this.mutualFundService = mutualFundService;
    }

    @GetMapping("/{tradingSymbol}")
    public ResponseEntity<MutualFundDetails> getMutualFundDetails(@PathVariable String tradingSymbol) {
        try {
            MutualFundDetails mutualFundDetails = mutualFundService.getMutualFundDetails(tradingSymbol);
            return ResponseEntity.ok(mutualFundDetails);
        } catch (MutualFundNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
