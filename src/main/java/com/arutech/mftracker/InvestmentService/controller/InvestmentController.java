package com.arutech.mftracker.InvestmentService.controller;

import com.arutech.mftracker.InvestmentService.model.Investment;
import com.arutech.mftracker.InvestmentService.service.InvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/investments")
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
}