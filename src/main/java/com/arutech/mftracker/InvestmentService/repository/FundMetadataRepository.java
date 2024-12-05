package com.arutech.mftracker.InvestmentService.repository;

import com.arutech.mftracker.InvestmentService.model.FundMetadata;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface FundMetadataRepository extends JpaRepository<FundMetadata, String> {
    List<FundMetadata> findByFundHouseAndSchemeName(String fundHouse,String schemeName);

}

