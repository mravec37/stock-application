package com.tomas.stockapplication.service.stock;

import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.StockIndex;

public interface StockService {
    public StockEntity saveStock(StockEntity stock);
    public StockEntity saveStock(String symbol);
    public StockEntity updateStock(String symbol);
    public void addIndexToStock(StockIndex index, String symbol);
    public StockEntity findStockBySymbol(String symbol);

}
