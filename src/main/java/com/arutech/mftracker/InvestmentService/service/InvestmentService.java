package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.model.Investment;
import com.arutech.mftracker.InvestmentService.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    public Investment addInvestment(String userId, Investment investment) {
        return investmentRepository.save(investment);
    }

    public List<Investment> getUserInvestments(String userId) {
        return investmentRepository.findByUserId(userId);
    }

    public Investment updateInvestment(String userId, String investmentId, Investment investment) {
        return investmentRepository.save(investment);
    }

    public void deleteInvestment(String userId, String investmentId) {
        Investment investment = investmentRepository.findByUserIdAndInvestmentId(userId, investmentId);
        if (null != investment)
            investmentRepository.delete(investment);
    }

    public void saveInvestments(String userId, List<Investment> investments) {
        investmentRepository.saveAll(investments);
    }


}