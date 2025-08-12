/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.dao.dto;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDTO {
    private String invoiceID;
    private Date invoiceDate;
    private Time invoiceTime;
    private String employeeID;
    private String customerID; 
    private BigDecimal totalAmount;
    private Integer totalQuantity;
    private String paymentMethod;
    private String status;
    private Integer total;
}
