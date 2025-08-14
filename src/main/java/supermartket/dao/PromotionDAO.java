/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package supermartket.dao;

import java.util.List;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.PromotionDTO;
import supermartket.entity.Promotion;


public interface PromotionDAO extends CrudDAO<Promotion, String>{
    List<Promotion> findAll(PromotionDTO dto);
    PageDTO getTotalItem(PromotionDTO dto);
    Promotion getPromotionForCustomer(String customerId);
}
