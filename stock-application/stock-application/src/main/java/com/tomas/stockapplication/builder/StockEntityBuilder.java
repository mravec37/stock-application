package com.tomas.stockapplication.builder;
import com.tomas.stockapplication.entity.StockEntity;

import java.util.Map;

public class StockEntityBuilder {

    public static StockEntity buildStock(Map<String, String> quote) {
        StockEntity stock = StockEntity.builder()
                .symbol(quote.get("01. symbol"))
                .open(Double.valueOf(quote.get("02. open")))
                .high(Double.valueOf(quote.get("03. high")))
                .low(Double.valueOf(quote.get("04. low")))
                .price(Double.valueOf(quote.get("05. price")))
                .volume(Long.valueOf(quote.get("06. volume")))
                .latestTradingDay(quote.get("07. latest trading day"))
                .previousClose(Double.valueOf(quote.get("08. previous close")))
                .changeValue(Double.valueOf(quote.get("09. change")))
                .changePercent((quote.get("10. change percent")))
                .build();
        return stock;


    }
}

