package com.tomas.stockapplication.service.watchlist;

import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.User;
import com.tomas.stockapplication.entity.Watchlist;

import java.util.Optional;

public interface WatchlistService {
    void addStockToWatchlist(User user, StockEntity stock);

    Optional<Watchlist> getWatchlistByUsername(String username);

    void deleteStockFromWatchlist(StockEntity stock, Watchlist watchlist);
}
