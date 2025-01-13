package com.arutech.mftracker.InvestmentService.controller;

import com.arutech.mftracker.InvestmentService.model.Investment;
import com.arutech.mftracker.InvestmentService.service.InvestmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/investments")
@Slf4j
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;

    }

    @PostMapping
    public ResponseEntity<Investment> createInvestment(@PathVariable String userId, @RequestBody Investment investment) {
        Investment savedInvestment = investmentService.addInvestment(userId, investment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInvestment);

    }

    @PostMapping("/batch")
    public ResponseEntity<List<Investment>> createInvestments(@PathVariable String userId, @RequestBody List<Investment> investments) {
         investmentService.saveInvestments(userId, investments);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Investment>> getInvestmentsByUser(@PathVariable String userId) {
        List<Investment> investments = investmentService.getUserInvestments(userId);
        return ResponseEntity.ok(investments);
    }

    @PutMapping("/{investmentId}")
    public ResponseEntity<Investment> updateInvestment(@PathVariable String userId, @PathVariable String investmentId, @RequestBody Investment investment) {
        Investment updatedInvestment = investmentService.updateInvestment(userId, investmentId, investment);
        return ResponseEntity.ok(updatedInvestment);
    }

    @DeleteMapping("/{investmentId}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable String userId, @PathVariable String investmentId) {
        investmentService.deleteInvestment(userId, investmentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/loadfile")
    public ResponseEntity<Void> uploadFile(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        List<Investment> investments = investmentService.extractInvestmentsFromExcel(userId,file);
        log.info("saved {} investments",investments.size());
        investmentService.saveInvestments(userId,investments);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}