/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package supermartket.dao;

import supermartket.dao.impl.CartItemSell;

public interface ActionCartItem {
    void delete(CartItemSell item);
    void update(CartItemSell item);
}
