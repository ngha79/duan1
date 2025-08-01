package supermartket.ui.comp;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.*;
import java.awt.*;
import supermartket.ui.comp.CartItem;

public class FlowScrollPanel {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
            
            // Thêm nhiều component vào panel
            for (int i = 0; i < 30; i++) {
                contentPanel.add(new CartItem());
            }

            // Tạo JScrollPane để bao quanh contentPanel
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            // Cho phép scroll ngang và dọc
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            // Tạo frame
            JFrame frame = new JFrame("FlowLayout with Scroll");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(scrollPane);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
