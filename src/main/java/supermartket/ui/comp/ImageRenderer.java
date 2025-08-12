/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermartket.ui.comp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ImageRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        if (value instanceof ImageIcon) {
            ImageIcon icon = (ImageIcon) value;
            // Resize ảnh để vừa ô
            Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        }
        label.setHorizontalAlignment(CENTER);
        label.setVerticalAlignment(CENTER);
        return label;
    }
}

