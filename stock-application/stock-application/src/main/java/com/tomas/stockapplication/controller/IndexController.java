package com.tomas.stockapplication.controller;

import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.StockIndex;
import com.tomas.stockapplication.repository.IndexRepository;
import com.tomas.stockapplication.service.index.IndexService;
import com.tomas.stockapplication.service.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/index")
@CrossOrigin
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Autowired
    StockService stockService;
    @Autowired
    private IndexRepository indexRepository;

    @GetMapping("/DJI")
    public StockIndex getDowJonesIndex() {
      return indexService.findIndexBySymbol("DJI");
    }

    @GetMapping("/DJI/{symbol}")
    public String addStockToIndex(@PathVariable String symbol) {
        StockEntity stock = stockService.findStockBySymbol(symbol);
        StockIndex index = indexService.findIndexBySymbol("DJI");
        indexService.addStockToIndex(stock, "DJI");
        stockService.addIndexToStock(index, symbol);
        return "done";
    }

    @GetMapping("/DJI/stocks")
    public List<StockEntity> getAllDowJonesIndexStocks() {
        return indexRepository.findAllStocksByDowJonesIndex();
    }
}
