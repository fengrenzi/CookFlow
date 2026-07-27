package com.hnit.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShoppingCart {
    private String id;
    private Long userId;
    private String itemType;     // recipe, book, book_recipe
    private String itemId;
    private Integer quantity;
    private LocalDateTime addedTime;
}