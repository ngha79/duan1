package supermartket.entity;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    private Integer employeeID;
    private String fullName;
    private String phone;
    private Boolean gender;
    private Date birthDate;
    private Date startDate;
    private Boolean status;
    private String address;
    private String role;
    private String password;
}
