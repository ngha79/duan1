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
import java.util.List;
import supermartket.dao.dto.ImportReceiptDTO;
import supermartket.entity.Supplier;

public class ExcelExporterReceipt {
    
    public static void exportTableToExcel(JTable table, List<ImportReceiptDTO> list, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < table.getColumnCount() - 1; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(table.getColumnName(col));
        }

        for (int row = 0; row < list.size(); row++) {
            Row excelRow = sheet.createRow(row + 1);
            excelRow.createCell(0).setCellValue(list.get(row).getReceiptID());
            excelRow.createCell(1).setCellValue(list.get(row).getImportDate());
            excelRow.createCell(2).setCellValue(list.get(row).getSupplierID());
            excelRow.createCell(3).setCellValue(list.get(row).getQuantity());
            excelRow.createCell(4).setCellValue(String.valueOf(list.get(row).getTotalPrice()));
            excelRow.createCell(5).setCellValue(list.get(row).getStatus());
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
