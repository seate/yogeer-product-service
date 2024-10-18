package com.yoger.productserviceorganization.product.domain.model;

import com.yoger.productserviceorganization.product.domain.exception.InvalidProductException;
import com.yoger.productserviceorganization.product.domain.exception.InvalidTimeSetException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class Product {
    private final Long id;
    private String name;
    private final List<PriceByQuantity> priceByQuantities;
    private String description;
    private String imageUrl;
    private final ProductState state;
    private String thumbnailImageUrl;
    private final Long creatorId;
    private final String creatorName;
    private final LocalDateTime dueDate;
    private final StockDomain stockDomain;

    private Product(
            Long id,
            String name,
            List<PriceByQuantity> priceByQuantities,
            String description,
            String imageUrl,
            ProductState state,
            String thumbnailImageUrl,
            Long creatorId,
            String creatorName,
            LocalDateTime dueDate,
            StockDomain stockDomain
    ) {
        this.id = id;
        this.name = name;
        this.priceByQuantities = priceByQuantities;
        this.description = description;
        this.imageUrl = imageUrl;
        this.state = state;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.dueDate = dueDate;
        this.stockDomain = stockDomain;
    }

    // 정적 팩토리 메서드
    public static Product of(
            Long id,
            String name,
            List<PriceByQuantity> priceByQuantities,
            String description,
            String imageUrl,
            ProductState state,
            String thumbnailImageUrl,
            Long creatorId,
            String creatorName,
            LocalDateTime dueDate,
            int initialStockQuantity,
            int stockQuantity
    ) {
        validateFieldsByState(state, priceByQuantities, dueDate);
        StockDomain stockDomain = new StockDomain(initialStockQuantity, stockQuantity);
        stockDomain.validateByState(state);
        return new Product(
                id,
                name,
                priceByQuantities,
                description,
                imageUrl,
                state,
                thumbnailImageUrl,
                creatorId,
                creatorName,
                dueDate,
                stockDomain
        );
    }

    private static void validateFieldsByState(ProductState state, List<PriceByQuantity> priceByQuantities,
                                              LocalDateTime dueDate) {
        switch (state) {
            case SELLABLE -> validateFieldsInSellableState(priceByQuantities, dueDate);
            case DEMO -> validateFieldsInDemoState(priceByQuantities, dueDate);
            case SALE_ENDED -> {
                // SALE_ENDED 상태에서는 별도의 검증 추후에 추가
            }
            default -> throw new InvalidProductException("잘못된 상태입니다.");
        }
    }

    private static void validateFieldsInSellableState(List<PriceByQuantity> priceByQuantities, LocalDateTime dueDate) {
        //판매 가능 상품 필드들에 대해 필요한 유효성 검증 로직 작성
        if (priceByQuantities == null || priceByQuantities.isEmpty()) {
            throw new InvalidProductException("판매 가능 상품에 가격 정보가 비어 있을 수 없습니다.");
        }
        if (dueDate == null) {
            throw new InvalidTimeSetException("판매 가능 상품은 기한이 필요합니다.");
        }
        if (dueDate.isBefore(LocalDateTime.now())) {
            throw new InvalidTimeSetException("상품의 기한은 과거일 수 없습니다.");
        }
    }

    private static void validateFieldsInDemoState(List<PriceByQuantity> priceByQuantities, LocalDateTime dueDate) {
        //데모 상품 필드들에 대해 필요한 유효성 검증 로직 작성
        if (priceByQuantities != null) {
            throw new InvalidProductException("데모 상품은 가격 정보를 가질 수 없습니다.");
        }
        if (dueDate != null) {
            throw new InvalidTimeSetException("데모 상품은 기한을 가질 수 없습니다.");
        }
    }

    public void decreaseStockQuantity(int amount) {
        this.stockDomain.decrease(amount);
    }

    public int getStockQuantity() {
        return stockDomain.getStockQuantity();
    }

    public int getInitialStockQuantity() {
        return stockDomain.getInitialStockQuantity();
    }

    public void validateUnexpectedState(ProductState expectedState) {
        if (isUnexpectedState(expectedState)) {
            throw new InvalidProductException("상품이 예상된 상태가 아닙니다.");
        }
    }

    private boolean isUnexpectedState(ProductState expectedState) {
        return !this.state.equals(expectedState);
    }

    public void updateName(String newName) {
        this.name = newName;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public void updateImageUrl(String newImageUrl) {
        this.imageUrl = newImageUrl;
    }

    public void updateThumbnailImageUrl(String newThumbnailImageUrl) {
        this.thumbnailImageUrl = newThumbnailImageUrl;
    }
}
