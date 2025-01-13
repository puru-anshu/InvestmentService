package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.dto.FundSearchCriteria;
import com.arutech.mftracker.InvestmentService.model.FundDetail;
import com.arutech.mftracker.InvestmentService.repository.FundDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundDetailService {
    private final FundDetailRepository repository;
    private final MongoTemplate mongoTemplate;

    public FundDetail getFundById(String id) {
        log.info("Searching for Dataplan Id {}", id);
        return repository.findByDataPlanId(id).get(0);
//                .orElseThrow(() -> new RuntimeException("Fund not found"));
    }

    public List<FundDetail> searchFunds(FundSearchCriteria criteria) {
        Query query = new Query();

        if (criteria.getSchemeName() != null) {
            query.addCriteria(Criteria.where("schemeName")
                    .regex(criteria.getSchemeName(), "i"));
        }

        if (criteria.getCategory() != null) {
            query.addCriteria(Criteria.where("category")
                    .is(criteria.getCategory()));
        }

        if (criteria.getPrimaryCategory() != null) {
            query.addCriteria(Criteria.where("primaryCategory")
                    .is(criteria.getPrimaryCategory()));
        }

        if (criteria.getAmcName() != null) {
            query.addCriteria(Criteria.where("amcName")
                    .is(criteria.getAmcName()));
        }

        if (criteria.getInvestmentType() != null) {
            query.addCriteria(Criteria.where("investmentType")
                    .is(criteria.getInvestmentType()));
        }

        // Add return based filters if specified
        if (criteria.getMinReturn1Y() != null) {
            query.addCriteria(Criteria.where("returns.value")
                    .gte(criteria.getMinReturn1Y())
                    .and("returns.period").is("1Y"));
        }

        if (criteria.getMaxReturn1Y() != null) {
            query.addCriteria(Criteria.where("returns.value")
                    .lte(criteria.getMaxReturn1Y())
                    .and("returns.period").is("1Y"));
        }

        // Add pagination
        if (criteria.getPage() != null && criteria.getSize() != null) {
            query.with(PageRequest.of(criteria.getPage(), criteria.getSize()));
        }

        return mongoTemplate.find(query, FundDetail.class);
    }

    public FundDetail saveFund(FundDetail fund) {
        return repository.save(fund);
    }

    public void deleteFund(String id) {
        repository.deleteById(id);
    }
}
