package com.arutech.mftracker.InvestmentService.repository;

import com.arutech.mftracker.InvestmentService.model.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PortfolioRepository extends MongoRepository<Portfolio,String> {
    List<Portfolio> findByUserId(String userId);
    List<Portfolio> findByUserIdAndUnitsGreaterThan(String userId,int units);
    Portfolio findByUserIdAndId(String userId, String id);
    Portfolio findBySchemeNameAndFolioNumber(String schemeName, String folioNumber);
}
