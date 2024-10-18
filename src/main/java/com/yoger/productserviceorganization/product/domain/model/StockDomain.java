package com.yoger.productserviceorganization.product.domain.model;

import com.yoger.productserviceorganization.product.domain.exception.InsufficientStockException;
import com.yoger.productserviceorganization.product.domain.exception.InvalidStockException;
import lombok.AccessLevel;
import lombok.Getter;

//package-private 접근 제어로 패키지 밖에서는 이 StockDomain 클래스에 접근 불가
@Getter(AccessLevel.PACKAGE)
class StockDomain {
    private final int initialStockQuantity;
    private int stockQuantity;

    StockDomain(int initialStockQuantity, int stockQuantity) {
        if (initialStockQuantity < 0) {
            throw new InvalidStockException("초기 재고 수량은 0보다 작을 수 없습니다.");
        }
        if (stockQuantity < 0) {
            throw new InvalidStockException("현재 재고 수량은 0보다 작을 수 없습니다.");
        }
        if (initialStockQuantity < stockQuantity) {
            throw new InvalidStockException("현재 재고가 초기 재고 수량보다 많을 수 없습니다.");
        }
        this.initialStockQuantity = initialStockQuantity;
        this.stockQuantity = stockQuantity;
    }

    void decrease(int amount) {
        if (amount <= 0) {
            throw new InvalidStockException("감소할 수량은 0보다 커야 합니다.");
        }
        if (this.stockQuantity < amount) {
            throw new InsufficientStockException("재고 수량이 부족합니다.");
        }
        this.stockQuantity -= amount;
    }

    void validateByState(ProductState state) {
        switch (state) {
            case SELLABLE -> validateSellableState();
            case DEMO -> validateDemoState();
            case SALE_ENDED -> {
                // 검증이 필요 없는 상태는 처리하지 않음
            }
        }
    }

    private void validateSellableState() {
        if (initialStockQuantity == 0) {
            throw new InvalidStockException("판매 가능 상품의 초기 재고 수량은 0보다 커야 합니다.");
        }
    }

    private void validateDemoState() {
        if (initialStockQuantity != 0 || stockQuantity != 0) {
            throw new InvalidStockException("데모 상품은 초기 재고 수량 및 현재 재고 수량을 가질 수 없습니다.");
        }
    }
}
