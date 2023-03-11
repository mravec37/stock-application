package com.tomas.stockapplication.service.index;

import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.StockIndex;

public interface IndexService {
    public StockIndex saveIndex(StockIndex index);
    public StockIndex updateIndex(String symbol, StockIndex index);
    public StockIndex findIndexBySymbol(String symbol);

    void addStockToIndex(StockEntity stock, String indexSymbol);
}
