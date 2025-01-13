package com.arutech.mftracker.InvestmentService.repository;

import com.arutech.mftracker.InvestmentService.model.Scheme;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SchemeRepository extends MongoRepository<Scheme, String> {


    @Query("{ '_id' : ?0 }")
    Optional<Scheme> findById(ObjectId id);

    List<Scheme> findBySchemeLike(String scheme);


    @Query("{'scheme': {$regex: ?0, $options: 'i'}}")
    List<Scheme> findBySchemeNameContainingIgnoreCase(String scheme);

    List<Scheme> findByCategory(String category);

    List<Scheme> findByPrimaryCategory(String primaryCategory);

    List<Scheme> findByPrimaryCategoryAndCategory(String primaryCategory, String category);

    List<Scheme> findByDataPlanId(String dataPlanId);

    List<Scheme> findByRegularIsin(String isin);
    List<Scheme> findByDirectIsin(String isin);
}
