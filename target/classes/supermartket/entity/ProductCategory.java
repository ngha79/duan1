package supermartket.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {
    private String categoryID;
    private String categoryName;
    private String categoryDescription;
}


