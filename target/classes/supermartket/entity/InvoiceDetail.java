package supermartket.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDetail {
    private Integer invoiceID;
    private Integer productID;
    private Integer quantity;
    private Double unitPrice;
}