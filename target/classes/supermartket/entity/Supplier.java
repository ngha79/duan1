package supermartket.entity;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    private String supplierID;
    private String supplierName;
    private String address;
    private String phone;
    private String email;
    private Boolean status;
}

