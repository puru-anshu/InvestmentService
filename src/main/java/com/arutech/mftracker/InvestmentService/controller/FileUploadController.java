package com.arutech.mftracker.InvestmentService.controller;

import com.arutech.mftracker.InvestmentService.model.Portfolio;
import com.arutech.mftracker.InvestmentService.service.CASInvestmentExtractionService;
import com.arutech.mftracker.InvestmentService.service.InvestmentService;
import com.arutech.mftracker.InvestmentService.service.PortfolioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/upload")
@Slf4j
public class FileUploadController {
    private final CASInvestmentExtractionService investmentExtractionService;
    private final PortfolioService portfolioService;

    public FileUploadController(CASInvestmentExtractionService investmentExtractionService,
                                InvestmentService investmentService , PortfolioService portfolioService) {
        this.investmentExtractionService = investmentExtractionService;
        this.portfolioService =portfolioService;
    }

    @PostMapping
    public ResponseEntity<Void> uploadFile(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        List<Portfolio> portfolios = investmentExtractionService.extractPortfolioFromExcel(userId,file);
        log.info("saved {} portfolios",portfolios.size());
        portfolioService.savePortfolio(userId,portfolios);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}