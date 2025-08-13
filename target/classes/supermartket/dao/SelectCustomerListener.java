/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package supermartket.dao;

import supermartket.entity.Customer;

public interface SelectCustomerListener {

    void onSelected(Customer item);

    void onCreate(Customer item);
}
