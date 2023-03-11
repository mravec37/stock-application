package com.tomas.stockapplication.controller;

import com.tomas.stockapplication.dto.UserRegistrationRequest;
import com.tomas.stockapplication.entity.StockEntity;
import com.tomas.stockapplication.entity.User;
import com.tomas.stockapplication.service.stock.StockService;
import com.tomas.stockapplication.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/stock")
@CrossOrigin
public class StockController {

    UserService userService;
    private final RestTemplate restTemplate;
    private final StockService stockService;

    public StockController(StockService stockService, RestTemplate restTemplate, UserService userService) {
        this.stockService = stockService;
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        User newUser = User.builder()
                .userName(userRegistrationRequest.getUserName())
                .email(userRegistrationRequest.getEmail())
                .password(userRegistrationRequest.getPassword())
                .roles("ROLE_USER")
                .build();
        userService.addUser(newUser);
        return ResponseEntity.status(HttpStatus.OK).body("User registered");
    }


    @GetMapping("/")
    public String returnMainPage() {
        return "Main page";
    }

    @GetMapping("/{symbol}")
    public StockEntity getStockData(@PathVariable String symbol) {
        StockEntity stock = stockService.updateStock(symbol);
        if (stock == null) {
            return stockService.saveStock(symbol);
        }
        return stock;
    }


}
