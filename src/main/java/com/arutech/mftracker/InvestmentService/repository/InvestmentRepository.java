package com.arutech.mftracker.InvestmentService.repository;

import com.arutech.mftracker.InvestmentService.model.Investment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InvestmentRepository extends MongoRepository<Investment, String> {
    List<Investment> findByUserId(String userId);
    Investment findByUserIdAndInvestmentId(String userId, String investmentId);
}