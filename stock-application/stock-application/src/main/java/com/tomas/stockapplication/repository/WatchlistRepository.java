package com.tomas.stockapplication.repository;

import com.tomas.stockapplication.entity.User;
import com.tomas.stockapplication.entity.Watchlist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    Optional<Watchlist> findByUser(User user);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM watchlist_stocks WHERE stock_id = :stockId AND watchlist_id = :watchlistId", nativeQuery = true)
    void deleteStockFromWatchlist(@Param("stockId") Long stockId, @Param("watchlistId") Long watchlistId);

}
