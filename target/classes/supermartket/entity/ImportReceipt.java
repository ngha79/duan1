package supermartket.entity;


import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportReceipt {
    private Integer receiptID;
    private Date importDate;
    private Integer employeeID;
    private Integer supplierID;
}
