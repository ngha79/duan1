package supermartket.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportReceiptDetail {
    private Integer receiptID;
    private Integer productID;
    private Integer quantity;
    private Double unitPrice;
}
