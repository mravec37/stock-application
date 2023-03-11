package com.tomas.stockapplication.builder;

import com.tomas.stockapplication.entity.StockIndex;

public class IndexBuilder {

    public static StockIndex buildIndex(Double value) {
        return StockIndex.builder()
                .indexValue(value)
                .build();
    }
}
