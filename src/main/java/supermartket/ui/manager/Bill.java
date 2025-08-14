/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package supermartket.ui.manager;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import supermartket.dao.CreateListener;
import supermartket.dao.CustomerDAO;
import supermartket.dao.EmployeeDAO;
import supermartket.dao.JPanelManager;
import supermartket.dao.dto.SearchInvoiceDTO;
import supermartket.dao.impl.CustomerDAOImpl;
import supermartket.dao.impl.EmployeeDAOImpl;
import supermartket.dao.impl.InvoiceDAOImpl;
import supermartket.entity.Customer;
import supermartket.entity.Employee;
import supermartket.entity.Invoice;
import supermartket.excel.ExcelExporterInvoice;
import supermartket.pagination.EventPagination;
import supermartket.ui.comp.ActionCellImportReceiptEditor;
import supermartket.ui.comp.ActionCellImportReceiptRenderer;
import supermartket.ui.form.InvoiceDetailJDialog;
import supermartket.util.XDialog;

public final class Bill extends javax.swing.JPanel implements JPanelManager<Invoice, String> {
    int currentPage = 1;
    InvoiceDAOImpl dao = new InvoiceDAOImpl();
    EmployeeDAO empDao = new EmployeeDAOImpl();
    CustomerDAO cusDao = new CustomerDAOImpl();
    List<Invoice> list = List.of();
    List<Employee> listEmp = empDao.findAll();
    List<Customer> listCus = cusDao.findAll();

    DatePickerSettings startDateSettings = new DatePickerSettings();
    DatePickerSettings endDateSettings = new DatePickerSettings();

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    /**
     * Creates new form Bill
     */
    public Bill() {
        initComponents();
        ActionCellImportReceiptEditor editor = new ActionCellImportReceiptEditor(tblInvoice, this, "Chi tiết");
        tblInvoice.setRowHeight(60);
        tblInvoice.getColumnModel().getColumn(11).setCellRenderer(new ActionCellImportReceiptRenderer("Chi tiết"));
        tblInvoice.getColumnModel().getColumn(11).setCellEditor(editor);

        startDateSettings.setLocale(new Locale("vi", "VN"));
        endDateSettings.setLocale(new Locale("vi", "VN"));

        startDatePicker = new DatePicker(startDateSettings);
        startDate.setLayout(new BorderLayout());
        startDate.add(startDatePicker, BorderLayout.CENTER);
        startDatePicker.setPreferredSize(new Dimension(170, 30));

        endDatePicker = new DatePicker(endDateSettings);
        endDate.setLayout(new BorderLayout());
        endDate.add(endDatePicker, BorderLayout.CENTER);
        endDatePicker.setPreferredSize(new Dimension(170, 30));

        for (Employee emp : listEmp) {
            cboEmployee.addItem(emp.getFullName());
        }

        for (Customer cus : listCus) {
            cboCustomer.addItem(cus.getFullName());
        }
        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                fill(page);
                currentPage = page;
            }

        });

        fill(currentPage);
    }

    private void fill(int page) {
        String search = txtSearch.getText().trim();
        Integer indexEmployee = cboEmployee.getSelectedIndex() == 0 ? null : cboEmployee.getSelectedIndex() - 1;
        Integer indexCustomer = cboCustomer.getSelectedIndex() == 0 ? null : cboCustomer.getSelectedIndex() - 1;

        String employeeId = (indexEmployee != null && indexEmployee >= 0 && indexEmployee < listEmp.size())
                ? listEmp.get(indexEmployee).getEmployeeID()
                : null;

        String customerId = (indexCustomer != null && indexCustomer >= 0 && indexCustomer < listCus.size())
                ? listCus.get(indexCustomer).getCustomerID()
                : null;
        String status = cboStatus.getSelectedItem().toString();
        status = status.equalsIgnoreCase("Trạng thái") ? null : status;

        LocalDate start = startDatePicker.getDate();
        LocalDate end = endDatePicker.getDate();
        Date startDate = start == null ? null : Date.valueOf(start);
        Date endDate = end == null ? null : Date.valueOf(end);

        if (start != null && end != null && start.isAfter(end)) {
            XDialog.alert("Ngày bắt đầu không được sau ngày kết thúc.");
            return;
        }

        SearchInvoiceDTO dto = new SearchInvoiceDTO(search, employeeId, customerId, status, startDate, endDate, page);
        List<Invoice> listInvoice = dao.findBySearchEmployeeAndDate(dto);
        int count = dao.getTotalItem(dto).getCount();
        int limit = 10;
        int totalPage = (int) Math.ceil((double) count / limit);
        pagination1.setPagegination(page, totalPage);
        fillToTable(listInvoice);
    }

    public void fillToTable(List<Invoice> listInvoice) {
        DefaultTableModel tbl = (DefaultTableModel) tblInvoice.getModel();
        tbl.setNumRows(0);
        for (Invoice invoice : listInvoice) {
            String emplyeeName = listEmp.stream().filter(e -> e.getEmployeeID().equalsIgnoreCase(invoice.getEmployeeID())).findFirst().map(e -> e.getFullName()).orElse(null);
            String customerName = listCus.stream().filter(c -> c.getCustomerID().equalsIgnoreCase(invoice.getCustomerID())).findFirst().map(c -> c.getFullName()).orElse(null);
            Object[] values = {
                invoice.getInvoiceID(),
                invoice.getInvoiceDate(),
                invoice.getInvoiceTime(),
                customerName,
                emplyeeName,
                invoice.getTotalQuantity(),
                invoice.getTotalAmount(),
                invoice.getDiscountApplied(),
                invoice.getFinalAmount(),
                invoice.getPaymentMethod(),
                invoice.getStatus()
            };
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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnExportExcel = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        cboEmployee = new javax.swing.JComboBox<>();
        btnSearch = new javax.swing.JButton();
        pagination1 = new supermartket.pagination.Pagination();
        startDate = new javax.swing.JPanel();
        endDate = new javax.swing.JPanel();
        cboCustomer = new javax.swing.JComboBox<>();
        cboStatus = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvoice = new javax.swing.JTable();

        jPanel1.setPreferredSize(new java.awt.Dimension(1098, 829));

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 22)); // NOI18N
        jLabel2.setText("Quản lý hóa đơn");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Lịch sử và chi tiết các giao dịch bán hàng");

        btnExportExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/supermartket/icons/icons8-plus-24.png"))); // NOI18N
        btnExportExcel.setText("Xuất Excel");
        btnExportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExportExcel)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExportExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        cboEmployee.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả nhân viên" }));

        btnSearch.setText("Lọc");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout startDateLayout = new javax.swing.GroupLayout(startDate);
        startDate.setLayout(startDateLayout);
        startDateLayout.setHorizontalGroup(
            startDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 167, Short.MAX_VALUE)
        );
        startDateLayout.setVerticalGroup(
            startDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout endDateLayout = new javax.swing.GroupLayout(endDate);
        endDate.setLayout(endDateLayout);
        endDateLayout.setHorizontalGroup(
            endDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 155, Short.MAX_VALUE)
        );
        endDateLayout.setVerticalGroup(
            endDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 31, Short.MAX_VALUE)
        );

        cboCustomer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả khách hàng" }));

        cboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trạng thái", "Đã thanh toán", "Chưa thanh toán" }));
        cboStatus.setToolTipText("");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(endDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(pagination1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(endDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pagination1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(startDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        tblInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã HĐ", "Ngày", "Giờ", "Khách hàng", "Nhân viên", "Số lượng", "Tổng tiền đơn hàng", "Tiền giảm giá", "Tổng tiền thanh toán", "Thanh toán", "Trạng thái", "Thao tác"
            }
        ));
        tblInvoice.setRowHeight(60);
        jScrollPane1.setViewportView(tblInvoice);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1399, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        fill(1);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnExportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportExcelActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            ExcelExporterInvoice.exportTableToExcel(tblInvoice, dao.findAll(), filePath);
        }
    }//GEN-LAST:event_btnExportExcelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportExcel;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cboCustomer;
    private javax.swing.JComboBox<String> cboEmployee;
    private javax.swing.JComboBox<String> cboStatus;
    private javax.swing.JPanel endDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private supermartket.pagination.Pagination pagination1;
    private javax.swing.JPanel startDate;
    private javax.swing.JTable tblInvoice;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    @Override
    public void delete(String id) {
    }

    @Override
    public void update(String id) {
        Invoice ip = dao.findById(id);
        InvoiceDetailJDialog dialog = new InvoiceDetailJDialog(null, true, ip, new CreateListener() {
            @Override
            public void onCreate() {
                fill(currentPage);
            }
        });
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
