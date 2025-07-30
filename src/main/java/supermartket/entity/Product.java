package supermartket.entity;


import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private String productID;
    private String productName;
    private String categoryID;
    private String supplierID;
    private BigDecimal price;
    private Integer quantity;
    private String status;
    private String unit;
}

