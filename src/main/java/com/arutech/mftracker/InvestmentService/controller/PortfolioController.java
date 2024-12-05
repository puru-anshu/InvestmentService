package com.arutech.mftracker.InvestmentService.controller;

import com.arutech.mftracker.InvestmentService.model.Investment;
import com.arutech.mftracker.InvestmentService.model.Portfolio;
import com.arutech.mftracker.InvestmentService.service.InvestmentService;
import com.arutech.mftracker.InvestmentService.service.PortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;

    }

    @GetMapping
    public ResponseEntity<List<Portfolio>> getPortfolioByUser(@PathVariable String userId) {
        List<Portfolio> investments = portfolioService.getUserPortfolio(userId);
        return ResponseEntity.ok(investments);
    }


}