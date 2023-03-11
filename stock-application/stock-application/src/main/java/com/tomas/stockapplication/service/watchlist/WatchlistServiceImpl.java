package com.tomas.stockapplication.service.watchlist;

import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.User;
import com.tomas.stockapplication.entity.Watchlist;
import com.tomas.stockapplication.repository.WatchlistRepository;
import com.tomas.stockapplication.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class WatchlistServiceImpl implements WatchlistService{

    @Autowired
    private WatchlistRepository watchlistRepository;
    @Autowired
    private UserService userService;

    @Override
    public void addStockToWatchlist(User user, StockEntity stock) {
       Optional<Watchlist> watchlistOptional = watchlistRepository.findByUser(user);
        Watchlist watchlist;
       if (watchlistOptional.isEmpty()) {
           watchlist = new Watchlist();
           watchlist.setUser(user);
           user.setWatchlist(watchlist);
           watchlistRepository.save(watchlist);
       } else {
           watchlist = watchlistOptional.get();
       }
       watchlist.addStock(stock);
       stock.addWatchlist(watchlist);
    }

    @Override
    public Optional<Watchlist> getWatchlistByUsername(String username) {
        Optional<User> user = userService.findByUserName(username);
        if (user.isEmpty()) {
          return Optional.empty();
        }
        return watchlistRepository.findByUser(user.get());
    }

    @Override
    public void deleteStockFromWatchlist(StockEntity stock, Watchlist watchlist) {
        watchlistRepository.deleteStockFromWatchlist(stock.getId(), watchlist.getId());
    }
}
