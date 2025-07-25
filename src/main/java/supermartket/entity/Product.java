package supermartket.entity;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Integer productID;
    private String productName;
    private Integer categoryID;
    private Integer unitID;
    private Double purchasePrice;
    private Double salePrice;
    private Integer stockQuantity;
    private Boolean status;
}

