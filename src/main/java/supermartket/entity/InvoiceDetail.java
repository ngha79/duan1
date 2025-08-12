package supermartket.entity;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDetail {
    private String invoiceID;
    private String productID;
    private Integer quantity;
    private BigDecimal unitPrice;
}