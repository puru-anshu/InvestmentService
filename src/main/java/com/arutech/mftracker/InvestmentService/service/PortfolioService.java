package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.model.Portfolio;
import com.arutech.mftracker.InvestmentService.repository.PortfolioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PortfolioService {

    @Autowired
    private PortfolioRepository repository;

    public Portfolio addPortfolio(String userId, Portfolio portfolio) {
        return repository.save(portfolio);
    }

    public List<Portfolio> getUserPortfolio(String userId) {
        log.info("getting user portfolio for {}", userId);
        return repository.findByUserIdAndUnitsGreaterThan(userId, 0);
    }

    public Portfolio updateInvestment(String userId, String investmentId, Portfolio portfolio) {
        return repository.save(portfolio);
    }

    public void deleteInvestment(String userId, String investmentId) {
        Portfolio portfolio = repository.findByUserIdAndId(userId, investmentId);
        if (null != portfolio)
            repository.delete(portfolio);
    }

    public void savePortfolio(String userId, List<Portfolio> portfolios) {
        for (Portfolio portfolio : portfolios) {
            Portfolio folio = repository.findBySchemeNameAndFolioNumber(portfolio.getSchemeName(), portfolio.getFolioNumber());
            if (null != folio) {
                repository.delete(folio);
            }
            repository.save(portfolio);

        }

    }

    public List<Portfolio> getPortfoliosWithNullSchemeCode() {
        return repository.findBySchemeCodeIsNull();
    }

    public Optional<Portfolio> getPortfolioByFolio(String userId, String folioNumber) {
        List<Portfolio> portfolios = getUserPortfolio(userId).stream()
                .filter(portfolio -> portfolio.getFolioNumber().equals(folioNumber)).toList();
        if (!portfolios.isEmpty())
            return Optional.of(portfolios.get(0));
        else
           return Optional.empty();

    }
}