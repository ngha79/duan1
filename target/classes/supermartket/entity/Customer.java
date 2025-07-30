package supermartket.entity;

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
    private Double totalPayment;  
}
