package supermartket.entity;

import java.math.BigDecimal;
import java.sql.Time;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    private String invoiceID;
    private Date invoiceDate;
    private Time invoiceTime;
    private String employeeID;
    private String customerID; // nullable
    private BigDecimal totalAmount;
    private Integer totalQuantity;
    private String paymentMethod;
    private String status;
}