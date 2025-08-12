/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package supermartket.dao;

import java.util.List;


public interface JPanelManager<T, ID> {
    void delete(ID id);
    void update(ID id);
}
