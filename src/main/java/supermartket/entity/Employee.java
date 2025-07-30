package supermartket.entity;

import java.sql.Date;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    private String employeeID;
    private String fullName;
    private String phone;
    private Boolean gender;
    private Date startDate;
    private String status;
    private String email;
    private String role;
}
