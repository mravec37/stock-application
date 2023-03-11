package com.tomas.stockapplication.controller;

import com.tomas.stockapplication.dto.AddStockToWatchlistRequest;
import com.tomas.stockapplication.dto.DeleteStockFromWatchlistRequest;
import com.tomas.stockapplication.dto.UserRegistrationRequest;
import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.User;
import com.tomas.stockapplication.entity.Watchlist;
import com.tomas.stockapplication.exception.StockNotFoundException;
import com.tomas.stockapplication.exception.UserNotFoundException;
import com.tomas.stockapplication.exception.WatchlistNotFoundException;
import com.tomas.stockapplication.service.stock.StockService;
import com.tomas.stockapplication.service.user.UserService;
import com.tomas.stockapplication.service.watchlist.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private StockService stockService;
    @Autowired
    private UserService userService;


    @PreAuthorize("permitAll()")
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/getWatchlist", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Set<StockEntity>> getUserWatchlist(@RequestBody String username) {
        return ResponseEntity.status(HttpStatus.OK).body(watchlistService.getWatchlistByUsername(username).get().getStocks());
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/addStock")
    public ResponseEntity<String> addStockToWatchlist(@RequestBody AddStockToWatchlistRequest addStockToWatchlistRequest) {
        try {
            String username = addStockToWatchlistRequest.getUsername();
            String symbol = addStockToWatchlistRequest.getSymbol();

            Optional<User> userOptional = userService.findByUserName(username);
            System.out.println(username);
            if (userOptional.isEmpty()) {
               throw new UserNotFoundException("User was not found");
            }
            User user = userOptional.get();

            StockEntity stock = stockService.updateStock(symbol);
            if (stock == null) {
                stock = stockService.saveStock(symbol);
                if (stock == null)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock not found");
            }

            watchlistService.addStockToWatchlist(user, stock);

            return ResponseEntity.ok("Stock added to watchlist");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("permitAll()")
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/deleteStock")
    public ResponseEntity<String> deleteStockFromWatchlist(@RequestBody DeleteStockFromWatchlistRequest request) {
       StockEntity stock = stockService.findStockBySymbol(request.getSymbol());
       if(stock == null) {
           throw new StockNotFoundException("Stock not found");
       }
       Optional<Watchlist> watchlistOptional = watchlistService.getWatchlistByUsername(request.getUsername());
       if (watchlistOptional.isEmpty()) {
           throw new WatchlistNotFoundException("Watchlist not found");
       }
       watchlistService.deleteStockFromWatchlist(stock, watchlistOptional.get());
       return ResponseEntity.status(HttpStatus.OK).body("Stock " + request.getSymbol() + " deleted from Watchlist");
    }

}
