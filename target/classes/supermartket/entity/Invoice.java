package supermartket.entity;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    private Integer invoiceID;
    private Date invoiceDate;
    private Integer employeeID;
    private Integer customerID; // nullable
    private Double totalAmount;
}