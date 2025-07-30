/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.entity;


public class XAuth {
    public static User user = User.builder()
            .username("user1")
            .password("123213")
            .enabled(true)
            .manager(false)
            .fullname("Nguyễn Văn Tèo")
            .photo("trump.png")
            .build(); // biến user này sẽ được thay thế sau khi đăng nhập
}
