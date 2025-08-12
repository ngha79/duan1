/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.entity;


public class XAuth {
    public static User user = User.builder()
            .employeeCode("EMP001")
            .username("user1")
            .password("123213")
            .build(); // biến user này sẽ được thay thế sau khi đăng nhập
}
