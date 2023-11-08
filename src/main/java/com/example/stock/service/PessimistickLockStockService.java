package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessimistickLockStockService {
    private StockRepository stockRepository;

    public PessimistickLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public  void decrease(Long id, Long quantity){

        Stock stock = stockRepository.findByIdWithPerssimisticLock(id);

        stock.decrease(quantity);

        stockRepository.save(stock);
    }
}
