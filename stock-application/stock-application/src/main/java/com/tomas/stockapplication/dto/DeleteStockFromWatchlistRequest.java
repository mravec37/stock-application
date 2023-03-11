package com.tomas.stockapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteStockFromWatchlistRequest {
   private String username;
   private String symbol;
}
