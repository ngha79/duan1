package supermartket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    private String username;
    private String password;
    private boolean enabled;
    private String fullname;
    private String employeeID;
    @Builder.Default
    private String photo = "trump-small.png";
    private boolean manager;
}