package com.tomas.stockapplication.service.stock;

import com.tomas.stockapplication.builder.StockEntityBuilder;
import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.StockIndex;
import com.tomas.stockapplication.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Transactional
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private RestTemplate restTemplate;
    private static final String API_KEY = "JJE4JJTLXX0GFIHH";

    @Override
    public StockEntity saveStock(StockEntity stock) {
        return stockRepository.save(stock);
    }

    @Override
    public StockEntity saveStock(String symbol) {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" +
                API_KEY;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, String> quote = (Map<String, String>) response.get("Global Quote");
        if (quote == null)
            return null;

        StockEntity stock = StockEntityBuilder.buildStock(quote);
        return stockRepository.save(stock);
    }

    @Override
    public StockEntity updateStock(String symbol) {
        StockEntity existingStock = stockRepository.findBySymbol(symbol);
        if (existingStock == null)
            return null;

        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" +
                API_KEY;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, String> quote = (Map<String, String>) response.get("Global Quote");
        if (quote == null) {
            System.out.println("API NOT REACHABLE, FETCHING STOCK FROM DATABASE");
            return existingStock;
        }

        StockEntity stock = StockEntityBuilder.buildStock(quote);
        existingStock = setStock(existingStock, stock);
        return stockRepository.save(existingStock);
    }

    @Override
    public void addIndexToStock(StockIndex index, String symbol) {
        stockRepository.findBySymbol(symbol).addIndex(index);
    }

    @Override
    public StockEntity findStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }

    private StockEntity setStock(StockEntity existingStock, StockEntity stock) {
        existingStock.setChangePercent(stock.getChangePercent());
        existingStock.setHigh(stock.getHigh());
        existingStock.setLow(stock.getLow());
        existingStock.setOpen(stock.getOpen());
        existingStock.setChangeValue(stock.getChangeValue());
        existingStock.setPreviousClose(stock.getPreviousClose());
        existingStock.setPrice(stock.getPrice());
        //existingStock.setPrice(980.0);
        existingStock.setLatestTradingDay(stock.getLatestTradingDay());
        existingStock.setSymbol(stock.getSymbol());
        existingStock.setVolume(stock.getVolume());
        return existingStock;
    }

}
