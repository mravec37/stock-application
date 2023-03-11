package com.tomas.stockapplication.service.index;

import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.StockIndex;
import com.tomas.stockapplication.repository.IndexRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class IndexServiceImpl implements IndexService {

    @Autowired
    IndexRepository indexRepository;

    @Override
    public StockIndex saveIndex(StockIndex index) {
        return indexRepository.save(index);
    }

    @Override
    public StockIndex updateIndex(String symbol, StockIndex index) {
        StockIndex existingIndex = indexRepository.findBySymbol(symbol);
        if (existingIndex == null)
            return null;

        existingIndex.setIndexValue(index.getIndexValue());
        existingIndex.setSymbol(index.getSymbol());
        return indexRepository.save(existingIndex);
    }

    @Override
    public StockIndex findIndexBySymbol(String symbol) {
       return indexRepository.findBySymbol(symbol);
    }

    @Override
    public void addStockToIndex(StockEntity stock, String indexSymbol) {
        indexRepository.findBySymbol(indexSymbol).addStock(stock);
    }
}
