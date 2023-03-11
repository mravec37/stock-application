package com.tomas.stockapplication.repository;

import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.StockIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository<StockIndex, Long> {
    public StockIndex findBySymbol(String symbol);

    @Query("SELECT s FROM StockEntity s JOIN s.stockIndexes si WHERE si.symbol = 'DJI'")
    List<StockEntity> findAllStocksByDowJonesIndex();
}
