package supermartket.entity;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    private Integer supplierID;
    private String supplierName;
    private String phone;
    private String address;
}
