package com.tomas.stockapplication.repository;

import com.tomas.stockapplication.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    public StockEntity findBySymbol(String symbol);
}
