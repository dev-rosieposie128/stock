package com.example.stock;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import com.example.stock.service.PessimistickLockStockService;
import com.example.stock.service.StockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StockApplicationTests {

	@Autowired
	private PessimistickLockStockService stockService;
	//private StockService stockService;

	@Autowired
	private StockRepository stockRepository;

	// 테스트 전에 데이터베이스에 재고가 있어야 하므로 데이터 생성
	@BeforeEach
	public void before(){
		stockRepository.saveAndFlush(new Stock(1L,100L));
	}
	// 테스트 후 데이터 삭제
	@AfterEach
	public void after(){
		stockRepository.deleteAll();
	}

	@Test
	public void 재고감소() {
		stockService.decrease(1L, 1L);

		Stock stock = stockRepository.findById(1L).orElseThrow();

		assertEquals(99, stock.getQuantity());
	}

	@Test
	public void 동시에_100개의_요청() throws InterruptedException {

		// 100개의 요청을 보낼 것임
		int threadCount = 100;

		// 멀티 스레드를 이용하기 위해 ExecutorService 사용 - (비동기로 실행하는 작업을 단순화하여 사용하게 하는 자바 api)
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);
		/*
		 1OO개의 요청이 모두 끝날때까지 기다려야 하므로 CountDownLatch 사용
		 CountDownLatch는 다른 스레드에서 수행죽인 작업이 완료될 때까지 대기할 수 있도록 도와주는 클래스 */

		for(int i=0; i< threadCount; i++){
			executorService.submit(()->{

				try{
					stockService.decrease(1L, 1L);
				}finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Stock stock = stockRepository.findById(1L).orElseThrow();

		// 100 - (1*00) = 0 : 예상 값
		assertEquals(0, stock.getQuantity());
	}
}
