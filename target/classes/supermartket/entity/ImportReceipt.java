package supermartket.entity;


import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportReceipt {
    private String receiptID;
    private Date importDate;
    private String supplierID;
    private String status;
}
