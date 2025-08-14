/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.excel;

import supermartket.dao.impl.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import supermartket.entity.Invoice;

public class ExcelExporterInvoice {

    public static void exportTableToExcel(JTable table, List<Invoice> list, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < table.getColumnCount() - 1; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(table.getColumnName(col));
        }

        CellStyle dateStyle = workbook.createCellStyle();
        short dateFormat = workbook.getCreationHelper()
                .createDataFormat()
                .getFormat("dd/MM/yyyy");
        dateStyle.setDataFormat(dateFormat);

        CellStyle timeStyle = workbook.createCellStyle();
        short timeFormat = workbook.getCreationHelper()
                .createDataFormat()
                .getFormat("HH:mm:ss");
        timeStyle.setDataFormat(timeFormat);

        for (int row = 0; row < list.size(); row++) {
            Row excelRow = sheet.createRow(row + 1);

            excelRow.createCell(0).setCellValue(list.get(row).getInvoiceID());

            // Ngày
            Cell dateCell = excelRow.createCell(1);
            dateCell.setCellValue(new java.util.Date(list.get(row).getInvoiceDate().getTime()));
            dateCell.setCellStyle(dateStyle);

            // Giờ
            Cell timeCell = excelRow.createCell(2);
            timeCell.setCellValue(new java.util.Date(list.get(row).getInvoiceTime().getTime()));
            timeCell.setCellStyle(timeStyle);

            excelRow.createCell(3).setCellValue(list.get(row).getCustomerID());
            excelRow.createCell(4).setCellValue(list.get(row).getEmployeeID());
            excelRow.createCell(5).setCellValue(list.get(row).getTotalQuantity());
            excelRow.createCell(6).setCellValue(list.get(row).getTotalAmount().doubleValue());
            excelRow.createCell(7).setCellValue(list.get(row).getDiscountApplied().doubleValue());
            excelRow.createCell(8).setCellValue(list.get(row).getFinalAmount().doubleValue());
            excelRow.createCell(9).setCellValue(list.get(row).getPaymentMethod());
            excelRow.createCell(10).setCellValue(list.get(row).getStatus());
        }

        for (int col = 0; col < table.getColumnCount(); col++) {
            sheet.autoSizeColumn(col);
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            workbook.close();
            JOptionPane.showMessageDialog(null, "Xuất Excel thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất Excel!");
        }
    }
}
