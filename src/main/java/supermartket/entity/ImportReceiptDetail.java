package supermartket.entity;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportReceiptDetail {
    private String receiptID;
    private String productID;
    private Integer quantity;
    private BigDecimal unitPrice;
}
