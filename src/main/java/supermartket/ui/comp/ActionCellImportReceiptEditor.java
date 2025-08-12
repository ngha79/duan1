/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.ui.comp;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import supermartket.dao.JPanelManager;


public class ActionCellImportReceiptEditor extends AbstractCellEditor implements TableCellEditor {

    private final JPanelManager mainPanel;

    private final JPanel panel;
    private final JButton btnEdit;
    private final JTable table;

    public ActionCellImportReceiptEditor(JTable table, JPanelManager mainPanel, String text) {
        this.table = table;
        this.mainPanel = mainPanel;
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnEdit = new JButton(text);

        panel.add(btnEdit);

        btnEdit.addActionListener(e -> {
            int row = table.getEditingRow();
            fireEditingStopped();
            mainPanel.update(table.getValueAt(table.getSelectedRow(), 0).toString());
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
