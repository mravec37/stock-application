package com.tomas.stockapplication.service.index;

import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.StockIndex;
import com.tomas.stockapplication.service.stock.StockService;
import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class DowJonesIndexUpdater implements Runnable {
    private final String API_KEY = "JJE4JJTLXX0GFIHH";
    private final String DOW_JONES_INDEX_SYMBOL = "DJI";
    private final Double DOW_JONES_INDEX_DIVISOR = 0.15172752595384;
    private Double dowJonesValue;

    @Autowired
    private StockService stockService;
    @Autowired
    private IndexService indexService;
    private final ScheduledExecutorService executorService =
            Executors.newSingleThreadScheduledExecutor();

   @Autowired
   private RestTemplate restTemplate;

    @PostConstruct
    public void startUpdater() {
        executorService.scheduleAtFixedRate(this,
                3,
                5,
                TimeUnit.HOURS);
    }

    @Override
    public void run() {
        updateDowJonesIndex();
    }
    private Elements getRowsFromTable(String websiteLink) throws IOException {
        // Connect to the Wikipedia page containing the table
        Document doc = Jsoup.connect(websiteLink).get();
        // Select the table using its class attribute
        Element table = doc.select("table.table-hover.table-borderless.table-sm").first();
        // Select the rows from the table body
        return table.select("tbody tr");
    }
    private void updateDowJonesIndex() {
        Double sum = 0.0;
        dowJonesValue = 0.0;
        try {
            Elements rows = getRowsFromTable("https://www.slickcharts.com/dowjones");
            String symbol = "";
            String companyName = "";
            int i = 0;
            for (Element row : rows) {
                if (row.select("td").text().equals("")) {
                    continue;
                }
                // Select the first column in the row
                symbol = row.select("td:nth-of-type(3)").first().text();
                companyName = row.select("td:nth-of-type(2)").text();

                System.out.println("Updating " + symbol);
                updateDowJonesCompanyStock(symbol);
                Thread.sleep(13000);
                i++;

            }
            Double dj = sum/DOW_JONES_INDEX_DIVISOR;
            dowJonesValue /= DOW_JONES_INDEX_DIVISOR;
            System.out.println("Dow index: " + dj);
            System.out.println("Dow index from API: " + dowJonesValue);

            updateIndexDatabase(dowJonesValue);

        } catch (IOException e) {
            e.printStackTrace();

      } catch (InterruptedException e) {
           throw new RuntimeException(e);
      }
    }

    private void updateIndexDatabase(Double value) {
        StockIndex newStockIndex = StockIndex.builder().indexValue(value).symbol(DOW_JONES_INDEX_SYMBOL).build();

       if (indexService.updateIndex(DOW_JONES_INDEX_SYMBOL, newStockIndex) == null) {
          indexService.saveIndex(newStockIndex);
       }
    }

    private void updateDowJonesCompanyStock(String symbol) {
//        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" +
//                API_KEY;
//
//        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
//        Map<String, String> quote = (Map<String, String>) response.get("Global Quote");
//
//        if (quote != null) {
//            StockEntity stock = StockEntityBuilder.buildStock(quote);
//            StockEntity stock1 = stockService.updateStock(symbol);
//            dowJonesValue += stock.getPrice();
//            System.out.println("Symbol: " + symbol + " Price: " + stock.getPrice());
//        } else {
//            StockEntity stock = stockService.findStockBySymbol(symbol);
//            dowJonesValue += stock.getPrice();
//            System.out.println("API NOT RESPONDING, FETCHING FROM DATABASE: Symbol : " + symbol + " Price: " + stock.getPrice());
//        }
        StockEntity existingStock = stockService.updateStock(symbol);
        StockIndex index = indexService.findIndexBySymbol(DOW_JONES_INDEX_SYMBOL);
        if (existingStock == null) {
            System.out.println("4");
            StockEntity stock = stockService.saveStock(symbol);
            indexService.addStockToIndex(stock, DOW_JONES_INDEX_SYMBOL);
            stockService.addIndexToStock(index, symbol);
            dowJonesValue += stock.getPrice();
            System.out.println("Symbol: " + symbol + " Price: " + stock.getPrice());
        } else {
            indexService.addStockToIndex(existingStock, DOW_JONES_INDEX_SYMBOL);
            stockService.addIndexToStock(index, symbol);
            dowJonesValue += existingStock.getPrice();
            System.out.println("Symbol: " + symbol + " Price: " + existingStock.getPrice());
        }
    }


}
