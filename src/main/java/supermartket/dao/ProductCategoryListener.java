/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package supermartket.dao;

import supermartket.entity.ProductCategory;
import supermartket.ui.comp.ProductCategoryItem;


public interface ProductCategoryListener {
    void onDelete(ProductCategoryItem category);
    void onUpdate(ProductCategoryItem category, ProductCategory item);
}
