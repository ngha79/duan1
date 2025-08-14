/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package supermartket.ui.form;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Locale;
import supermartket.dao.CreateListener;
import supermartket.dao.PromotionDAO;
import supermartket.dao.impl.PromotionDAOImpl;
import supermartket.entity.Promotion;
import supermartket.util.XDialog;

public class CreatePromotionJDialog extends javax.swing.JDialog {

    PromotionDAO dao = new PromotionDAOImpl();

    DatePickerSettings startDateSettings = new DatePickerSettings();
    DatePickerSettings endDateSettings = new DatePickerSettings();

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    /**
     * Creates new form CreatePromotionJDialog
     */
    public CreatePromotionJDialog(java.awt.Frame parent, boolean modal, CreateListener listener) {
        super(parent, modal);
        initComponents();

        startDateSettings.setLocale(new Locale("vi", "VN"));
        endDateSettings.setLocale(new Locale("vi", "VN"));

        startDatePicker = new DatePicker(startDateSettings);
        startDate.setLayout(new BorderLayout());
        startDate.add(startDatePicker, BorderLayout.CENTER);
        startDatePicker.setPreferredSize(new Dimension(200, 34));

        endDatePicker = new DatePicker(endDateSettings);
        endDate.setLayout(new BorderLayout());
        endDate.add(endDatePicker, BorderLayout.CENTER);
        endDatePicker.setPreferredSize(new Dimension(200, 34));

        btnAdd.addActionListener(e -> {
            if (XDialog.confirm("Bạn có xác nhận muốn thêm khách hàng mới.")) {
                Promotion pro = dao.create(getFormData());
                if (pro != null) {
                    listener.onCreate();
                    this.dispose();
                }
            }
        });
    }

    public Promotion getFormData() {

        String name = txtPromotionName.getText().trim();
        if (name.isEmpty()) {
            XDialog.alert("Tên khuyến mãi không được để trống.");
            return null;
        }

        BigDecimal minTotal;
        try {
            minTotal = new BigDecimal(txtMinTotalAmount.getText().trim());
            if (minTotal.compareTo(BigDecimal.ZERO) <= 0) {
                XDialog.alert("Tổng tiền tối thiểu phải lớn hơn 0.");
                return null;

            }
        } catch (NumberFormatException e) {
            XDialog.alert("Tổng tiền tối thiểu phải là số hợp lệ.");
            return null;

        }

        BigDecimal percent;
        try {
            String percentStr = txtDiscountPercent.getText().trim();
            percent = percentStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(percentStr);
            if (percent.compareTo(BigDecimal.ZERO) < 0 || percent.compareTo(new BigDecimal("100")) > 0) {
                XDialog.alert("Phần trăm giảm phải từ 0 đến 100.");
                return null;

            }
        } catch (NumberFormatException e) {
            XDialog.alert("Phần trăm giảm không hợp lệ.");
            return null;

        }

        BigDecimal amount;
        try {
            String amountStr = txtDiscountAmount.getText().trim();
            amount = amountStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                XDialog.alert("Số tiền giảm không được âm.");
                return null;

            }
        } catch (NumberFormatException e) {
            XDialog.alert("Số tiền giảm không hợp lệ.");
            return null;
        }

        BigDecimal maxAmount;
        try {
            String amountStr = txtMaxDiscountAmount.getText().trim();
            maxAmount = amountStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                XDialog.alert("Số tiền giảm không được âm.");
                return null;

            }
        } catch (NumberFormatException e) {
            XDialog.alert("Số tiền giảm không hợp lệ.");
            return null;
        }

        LocalDate start = startDatePicker.getDate();
        LocalDate end = endDatePicker.getDate();
        if (start == null || end == null) {
            XDialog.alert("Vui lòng chọn ngày bắt đầu và ngày kết thúc.");
            return null;

        }
        if (start.isAfter(end)) {
            XDialog.alert("Ngày bắt đầu không được sau ngày kết thúc.");
            return null;
        }

        return Promotion.builder()
                .PromotionName(name)
                .MinTotalAmount(minTotal)
                .DiscountPercent(percent)
                .DiscountAmount(amount)
                .MaxDiscountAmount(maxAmount)
                .StartDate(Date.valueOf(start))
                .EndDate(Date.valueOf(end))
                .Status(cboStatus.getSelectedItem().toString().equalsIgnoreCase("Hoạt động"))
                .build();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtPromotionName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMinTotalAmount = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDiscountPercent = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDiscountAmount = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cboStatus = new javax.swing.JComboBox<>();
        startDate = new javax.swing.JPanel();
        endDate = new javax.swing.JPanel();
        txtMaxDiscountAmount = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Thêm ưu đãi mới cho khách hàng");

        jLabel2.setText("Ưu đãi khi mua hàng");

        jLabel3.setText("Tên ");

        jLabel4.setText("Giá tối thiểu áp dụng");

        jLabel5.setText("Phần trăm giảm giá");

        jLabel6.setText("Số tiền giảm trực tiếp");

        jLabel7.setText("Ngày bắt đầu");

        jLabel8.setText("Ngày kết thúc");

        jLabel9.setText("Trạng thái");

        btnAdd.setText("Thêm");

        btnCancel.setText("Hủy");

        cboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hoạt động", "Ngừng hoạt động" }));

        javax.swing.GroupLayout startDateLayout = new javax.swing.GroupLayout(startDate);
        startDate.setLayout(startDateLayout);
        startDateLayout.setHorizontalGroup(
            startDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 235, Short.MAX_VALUE)
        );
        startDateLayout.setVerticalGroup(
            startDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout endDateLayout = new javax.swing.GroupLayout(endDate);
        endDate.setLayout(endDateLayout);
        endDateLayout.setHorizontalGroup(
            endDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        endDateLayout.setVerticalGroup(
            endDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        jLabel10.setText("Giảm tối đa");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPromotionName, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMinTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDiscountPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiscountAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(btnCancel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnAdd))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(99, 99, 99)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(81, 81, 81))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(startDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(endDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaxDiscountAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPromotionName, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMinTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiscountPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiscountAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaxDiscountAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CreatePromotionJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreatePromotionJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreatePromotionJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreatePromotionJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CreatePromotionJDialog dialog = new CreatePromotionJDialog(new javax.swing.JFrame(), true, new CreateListener() {
                    @Override
                    public void onCreate() {
                    }

                });
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JComboBox<String> cboStatus;
    private javax.swing.JPanel endDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel startDate;
    private javax.swing.JTextField txtDiscountAmount;
    private javax.swing.JTextField txtDiscountPercent;
    private javax.swing.JTextField txtMaxDiscountAmount;
    private javax.swing.JTextField txtMinTotalAmount;
    private javax.swing.JTextField txtPromotionName;
    // End of variables declaration//GEN-END:variables
}
