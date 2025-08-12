/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.ui.comp;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class ActionCellImportReceiptRenderer extends JPanel implements TableCellRenderer {

    public ActionCellImportReceiptRenderer(String text) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5)); // 👈 căn giữa, padding 5
        JButton btnEdit = new JButton(text);
        add(btnEdit);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }

}
