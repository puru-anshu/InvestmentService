package com.arutech.mftracker.InvestmentService.controller;

import com.arutech.mftracker.InvestmentService.model.Portfolio;
import com.arutech.mftracker.InvestmentService.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/folio/{folioNumber}")
    public ResponseEntity<Portfolio> getPortfolioByFolio(@PathVariable String userId, @PathVariable String folioNumber) {
        Optional<Portfolio> portfolio = portfolioService.getPortfolioByFolio(userId, folioNumber);
        return portfolio.map(ResponseEntity::ok)
                .orElseGet(() -> (ResponseEntity.notFound().build()));
    }


}