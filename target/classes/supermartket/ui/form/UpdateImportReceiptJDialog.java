/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package supermartket.ui.form;

import java.awt.Frame;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import supermartket.dao.CreateListener;
import supermartket.dao.CreateProductImportReceiptListener;
import supermartket.dao.ImportReceiptDAO;
import supermartket.dao.ImportReceiptDetailDAO;
import supermartket.dao.JPanelManager;
import supermartket.dao.ProductCategoryDAO;
import supermartket.dao.ProductDAO;
import supermartket.dao.SupplierDAO;
import supermartket.dao.dto.ImportReceiptDTO;
import supermartket.dao.dto.ProductImportReceipt;
import supermartket.dao.impl.ImportReceiptDAOImpl;
import supermartket.dao.impl.ImportReceiptDetailDAOImpl;
import supermartket.dao.impl.ProductCategoryDAOImpl;
import supermartket.dao.impl.ProductDAOImpl;
import supermartket.dao.impl.SupplierDAOImpl;
import supermartket.entity.ImportReceipt;
import supermartket.entity.ImportReceiptDetail;
import supermartket.entity.Product;
import supermartket.entity.ProductCategory;
import supermartket.entity.Supplier;
import supermartket.ui.comp.ActionCellEditor;
import supermartket.ui.comp.ActionCellRenderer;
import supermartket.util.XDialog;

public class UpdateImportReceiptJDialog extends javax.swing.JDialog implements JPanelManager<ImportReceipt, String> {

    ImportReceiptDAO dao = new ImportReceiptDAOImpl();
    ImportReceiptDetailDAO detailDao = new ImportReceiptDetailDAOImpl();
    SupplierDAO supDao = new SupplierDAOImpl();
    ProductCategoryDAO cateDao = new ProductCategoryDAOImpl();
    ProductDAO prodDao = new ProductDAOImpl();
    List<Supplier> listSupplier = supDao.findAll();
    List<ProductCategory> listCate = cateDao.findAll();
    ArrayList<ProductImportReceipt> listProduct = new ArrayList<>();

    public ImportReceipt getFormData() {
        String status = cboStatus.getSelectedItem().toString();
        String supplierID = listSupplier.get(cboSupplier.getSelectedIndex()).getSupplierID();

        return ImportReceipt.builder()
                .supplierID(supplierID)
                .status(status)
                .build();
    }

    /**
     * Creates new form CreateProduct
     */
    public UpdateImportReceiptJDialog(java.awt.Frame parent, boolean modal, ImportReceiptDTO importReceipt, CreateListener listener) {
        super(parent, modal);
        initComponents();
        int index = -1;
        DefaultTableModel tbl = (DefaultTableModel) tblProduct.getModel();
        tbl.setRowCount(0);
        ActionCellEditor editor = new ActionCellEditor(tblProduct, this);
        tblProduct.setRowHeight(40);
        tblProduct.getColumnModel().getColumn(5).setCellRenderer(new ActionCellRenderer());
        tblProduct.getColumnModel().getColumn(5).setCellEditor(editor);
        for (Supplier supplier : listSupplier) {
            cboSupplier.addItem(supplier.getSupplierName());
            if (importReceipt.getSupplierID().equalsIgnoreCase(supplier.getSupplierID())) {
                index = listSupplier.indexOf(supplier);
            }
        }
        List<ImportReceiptDetail> irds = detailDao.findAll(importReceipt.getReceiptID());
        for (ImportReceiptDetail ird : irds) {
            listProduct.add(new ProductImportReceipt(ird.getProductID(), null, null, ird.getQuantity(), ird.getUnitPrice()));
        }
        fillToTable(listProduct);
        btnUpdate.addActionListener(e -> {
            if (XDialog.confirm("Bạn có xác nhận muốn cập nhật dữ liệu.")) {
                ImportReceipt ir = getFormData();
                dao.update(new ImportReceipt(importReceipt.getReceiptID(), importReceipt.getImportDate(), ir.getSupplierID(), ir.getStatus()));
                detailDao.deleteByIRId(importReceipt.getReceiptID());
                for (ProductImportReceipt productImportReceipt : listProduct) {
                    detailDao.create(new ImportReceiptDetail(importReceipt.getReceiptID(), productImportReceipt.getProductId(), productImportReceipt.getQuantity(), productImportReceipt.getPrice()));
                }
                listener.onCreate();
                this.dispose();
            }
        });
        cboSupplier.setSelectedItem(listSupplier.get(index));
    }

    public void fillToTable(ArrayList<ProductImportReceipt> products) {
        DefaultTableModel tbl = (DefaultTableModel) tblProduct.getModel();
        tbl.setRowCount(0);
        for (ProductImportReceipt productImportReceipt : products) {
            Product prod = prodDao.findById(productImportReceipt.getProductId());
            ProductCategory cateName = listCate.stream().filter(c -> c.getCategoryID().equalsIgnoreCase(prod.getCategoryID())).findFirst().orElse(null);
            Object[] values = {
                prod.getProductID(),
                prod.getProductName(),
                cateName.getCategoryName(),
                productImportReceipt.getQuantity(),
                productImportReceipt.getPrice(),};
            tbl.addRow(values);
        }
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
        jLabel4 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        cboSupplier = new javax.swing.JComboBox<>();
        cboStatus = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduct = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel1.setText("Cập nhật phiếu nhập");

        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Nhập sản phẩm để bán");

        jLabel3.setText("Nhà cung cấp");

        jLabel4.setText("Trạng thái");

        btnUpdate.setText("Cập nhật");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnCancel.setText("Hủy");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        cboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chờ duyệt", "Đã duyệt", "Từ chối" }));
        cboStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStatusActionPerformed(evt);
            }
        });

        jLabel5.setText("Sản phẩm");

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Danh mục", "Số lượng", "Giá", "Thao tác"
            }
        ));
        jScrollPane1.setViewportView(tblProduct);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
        );

        btnAdd.setText("Thêm sản phẩm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(282, 282, 282)
                            .addComponent(btnAdd))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cboSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(cboStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cboStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboStatusActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        ArrayList<String> items = new ArrayList<>();
        for (ProductImportReceipt productImportReceipt : listProduct) {
            items.add(productImportReceipt.getProductId());
        }
        ListProductJDialog listProductDialog = new ListProductJDialog((Frame) SwingUtilities.getWindowAncestor(this), true, items, new CreateProductImportReceiptListener() {
            @Override
            public void onCreate(ProductImportReceipt item) {
                Product prod = prodDao.findById(item.getProductId());
                listProduct.add(new ProductImportReceipt(prod.getProductID(), prod.getProductName(), prod.getCategoryID(), item.getQuantity(), item.getPrice()));
                fillToTable(listProduct);
            }
        });
        listProductDialog.setLocationRelativeTo(null);
        listProductDialog.setVisible(true);
    }//GEN-LAST:event_btnAddActionPerformed

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
            java.util.logging.Logger.getLogger(UpdateImportReceiptJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateImportReceiptJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateImportReceiptJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateImportReceiptJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImportReceiptDTO importReceipt = new ImportReceiptDTO();
                UpdateImportReceiptJDialog dialog = new UpdateImportReceiptJDialog(new javax.swing.JFrame(), true, importReceipt, new CreateListener() {
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
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboStatus;
    private javax.swing.JComboBox<String> cboSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblProduct;
    // End of variables declaration//GEN-END:variables


    @Override
    public void delete(String id) {
        listProduct.removeIf(p -> p.getProductId().equalsIgnoreCase(id));
        fillToTable(listProduct);
    }

    @Override
    public void update(String id) {
        ProductImportReceipt product = listProduct.stream().filter(c -> c.getProductId().equalsIgnoreCase(id)).findFirst().orElse(null);
        UpdateItemImportReceiptDetailJDialog dialog = new UpdateItemImportReceiptDetailJDialog(null, true, product, new CreateProductImportReceiptListener() {
            @Override
            public void onCreate(ProductImportReceipt item) {
                listProduct.stream()
                        .filter(p -> p.getProductId().equals(item.getProductId()))
                        .findFirst()
                        .ifPresent(p -> {
                            p.setQuantity(item.getQuantity());
                            p.setPrice(item.getPrice());
                        });
                fillToTable(listProduct);
            }
        });
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
