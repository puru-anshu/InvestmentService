package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.dto.FundSearchCriteria;
import com.arutech.mftracker.InvestmentService.model.Scheme;
import com.arutech.mftracker.InvestmentService.repository.SchemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchemeService {
    private final SchemeRepository repository;
    private final MongoTemplate mongoTemplate;

    public Scheme getFundById(String id) {
        log.info("Searching for Dataplan Id {}", id);
        return repository.findByDataPlanId(id).get(0);

    }

    public List<Scheme> searchFunds(FundSearchCriteria criteria) {
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
        if (criteria.getIsin() != null) {
            query.addCriteria(Criteria.where("regular_isin").
                    is(criteria.getIsin())
                    .orOperator(
                            Criteria.where("direct_isin").is(criteria.getIsin())
                    )
            );
        }

//        if (criteria.getAmcName() != null) {
//            query.addCriteria(Criteria.where("amcName")
//                    .is(criteria.getAmcName()));
//        }

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

        return mongoTemplate.find(query, Scheme.class);
    }

    public Scheme saveFund(Scheme fund) {
        return repository.save(fund);
    }

    public void deleteFund(String id) {
        repository.deleteById(id);
    }

    public Optional<Scheme> getFundByIsin(String isin) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().orOperator(
                        Criteria.where("regular_isin").is(isin),
                        Criteria.where("direct_isin").is(isin)
                )
        );
        List<Scheme> schemes = mongoTemplate.find(query, Scheme.class);
        if (!schemes.isEmpty()) {
            return Optional.of(schemes.get(0));
        }
        return Optional.empty();

    }
}
