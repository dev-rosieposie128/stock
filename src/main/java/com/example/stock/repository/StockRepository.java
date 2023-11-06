package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

// 재고에 대한 crud
public interface StockRepository extends JpaRepository<Stock, Long> {

}
