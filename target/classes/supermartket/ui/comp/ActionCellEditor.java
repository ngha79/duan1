/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.ui.comp;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import supermartket.dao.JPanelManager;


public class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final JPanelManager mainPanel;

    private final JPanel panel;
    private final JButton btnEdit;
    private final JButton btnDelete;
    private final JTable table;

    public ActionCellEditor(JTable table, JPanelManager mainPanel) {
        this.table = table;
        this.mainPanel = mainPanel;
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");

        panel.add(btnEdit);
        panel.add(btnDelete);

        btnEdit.addActionListener(e -> {
            int row = table.getEditingRow();
            fireEditingStopped();
            mainPanel.update(table.getValueAt(table.getSelectedRow(), 0).toString());
        });

        btnDelete.addActionListener(e -> {
            int row = table.getEditingRow();
            fireEditingStopped();
            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa dữ liệu trên? Sau khi xóa không thể hoàn tác.", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mainPanel.delete(table.getValueAt(table.getSelectedRow(), 0).toString());
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {

        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }

        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
