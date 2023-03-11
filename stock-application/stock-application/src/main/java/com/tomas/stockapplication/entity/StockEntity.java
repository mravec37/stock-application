package com.tomas.stockapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    private Double open;
    private Double high;
    private Double low;
    private Double price;
    private Long volume;
    private String latestTradingDay;
    private Double previousClose;
    private Double changeValue;
    private String changePercent;

    @ManyToMany(mappedBy = "stocks")
    @JsonIgnore
    private Set<StockIndex> stockIndexes = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "stocks")
    private Set<Watchlist> watchlists = new HashSet<>();
    public void addIndex(StockIndex stockIndex) {
        stockIndexes.add(stockIndex);
    }

    public void addWatchlist(Watchlist watchlist) {
        watchlists.add(watchlist);
    }
}


