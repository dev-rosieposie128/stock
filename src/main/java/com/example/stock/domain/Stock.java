package com.example.stock.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//entity는 database의 테이블이라고 생각
@Entity
public class Stock {

    // id, productId, quantity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Long quantity;

    public Stock() {
    }

    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    //test case를 위한 수량 확인
    public Long getQuantity() {
        return quantity;
    }

    // 재고 감소 - 수량
    public void decrease(Long quantity){
        if(this.quantity - quantity < 0){
            throw new RuntimeException("0개 미만");
        }
        this.quantity = this.quantity-quantity;
    }
}
