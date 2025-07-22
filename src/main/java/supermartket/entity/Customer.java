package supermartket.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    private Integer customerID;
    private String fullName;
    private String phone;
    private Integer loyaltyPoints;
}
