package supermartket.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {
    private Integer categoryID;
    private String categoryName;
}

