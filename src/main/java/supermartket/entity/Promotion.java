/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.entity;

import java.math.BigDecimal;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {
    private String PromotionID;
    private String PromotionName;
    private BigDecimal TotalPaymentPrice;
    private BigDecimal MinTotalAmount;
    private BigDecimal DiscountPercent;
    private BigDecimal DiscountAmount;
    private BigDecimal MaxDiscountAmount;
    private Date StartDate;
    private Date EndDate;
    private Boolean Status;
    private Date createdAt;
    private Date updatedAt;
}
