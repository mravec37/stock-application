package com.tomas.stockapplication.entity;

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
public class StockIndex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double indexValue;
    private String symbol;

    @ManyToMany
    @JoinTable(
            name = "index_entity",
            joinColumns = @JoinColumn(name = "index_id"),
            inverseJoinColumns = @JoinColumn(name = "stock_id")
    )
    private Set<StockEntity> stocks = new HashSet<>();

    public void addStock(StockEntity stock) {
        stocks.add(stock);
    }
}
