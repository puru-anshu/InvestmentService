package com.arutech.mftracker.InvestmentService.repository;

import com.arutech.mftracker.InvestmentService.model.FundDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FundDetailRepository extends MongoRepository<FundDetail, String> {


    @Query("{ '_id' : ?0 }")
    Optional<FundDetail> findByIdT(ObjectId id);

    List<FundDetail> findBySchemeNameLike(String schemeName);


    @Query("{'schemeName': {$regex: ?0, $options: 'i'}}")
    List<FundDetail> findBySchemeNameContainingIgnoreCase(String schemeName);

    List<FundDetail> findByCategory(String category);

    List<FundDetail> findByAmcName(String amcName);

    List<FundDetail> findByDataPlanId(String dataPlanId);
}
