package supermartket.entity;

import java.math.BigDecimal;
import java.sql.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    private String customerID;
    private String fullName;
    private String phone;
    private String email;
    private BigDecimal totalAmount;  
    private Date createdAt;
    private Date updatedAt;
}
