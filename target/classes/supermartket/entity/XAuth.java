/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.entity;


public class XAuth {
    public static Employee user = Employee.builder()
            .employeeID("EMP0003")
            .fullName("Phạm Văn C")
            .password("123456")
            .role("Admin")
            .build(); // biến user này sẽ được thay thế sau khi đăng nhập
}
