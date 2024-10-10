package com.yoger.productserviceorganization.product.domain.model;

public class StockDomain {
    private final int initialStockQuantity;
    private int stockQuantity;

    StockDomain(int initialStockQuantity, int stockQuantity) {
        if (initialStockQuantity < 0) {
            throw new IllegalArgumentException("초기 재고 수량은 0보다 작을 수 없습니다.");
        }
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("현재 재고 수량은 0보다 작을 수 없습니다.");
        }
        if (initialStockQuantity < stockQuantity) {
            throw new IllegalArgumentException("현재 재고가 초기 재고 수량보다 많을 수 없습니다.");
        }
        this.initialStockQuantity = initialStockQuantity;
        this.stockQuantity = stockQuantity;
    }

    void decrease(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("감소할 수량은 0보다 커야 합니다.");
        }
        if (this.stockQuantity < amount) {
            throw new IllegalStateException("재고 수량이 부족합니다.");
        }
        this.stockQuantity-=amount;
    }

    int getStockQuantity() {
        return this.stockQuantity;
    }

    int getInitialStockQuantity() {
        return this.initialStockQuantity;
    }
}
