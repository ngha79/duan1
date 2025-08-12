/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package supermartket.ui.manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import supermartket.ui.comp.CartItem;
import net.miginfocom.swing.MigLayout;
import supermartket.dao.ActionCartItem;
import supermartket.dao.ActionSell;
import supermartket.dao.CustomerDAO;
import supermartket.dao.InvoiceDAO;
import supermartket.dao.InvoiceDetailDAO;
import supermartket.dao.ProductCategoryDAO;
import supermartket.dao.ProductDAO;
import supermartket.dao.SupplierDAO;
import supermartket.dao.dto.SearchProductDTO;
import supermartket.dao.impl.CartItemSell;
import supermartket.dao.impl.CustomerDAOImpl;
import supermartket.dao.impl.InvoiceDAOImpl;
import supermartket.dao.impl.InvoiceDetailDAOImpl;
import supermartket.dao.impl.ProductCategoryDAOImpl;
import supermartket.dao.impl.ProductDAOImpl;
import supermartket.dao.impl.SupplierDAOImpl;
import supermartket.entity.Customer;
import supermartket.entity.Invoice;
import supermartket.entity.InvoiceDetail;
import supermartket.entity.Product;
import supermartket.entity.ProductCategory;
import supermartket.entity.Supplier;
import supermartket.entity.XAuth;
import supermartket.ui.comp.CartItemDetail;
import supermartket.util.XDialog;
import supermartket.util.XJdbc;

public final class Sell extends javax.swing.JPanel {

    ProductDAO dao = new ProductDAOImpl();
    ProductCategoryDAO cateDao = new ProductCategoryDAOImpl();
    CustomerDAO cusDao = new CustomerDAOImpl();
    SupplierDAO supDao = new SupplierDAOImpl();
    InvoiceDAOImpl invoiceDao = new InvoiceDAOImpl();
    InvoiceDetailDAO invoiceDetailDao = new InvoiceDetailDAOImpl();

    List<Product> list = dao.findAll();
    List<CartItemSell> listCartItem = new ArrayList<>();
    List<ProductCategory> listCate = cateDao.findAll();
    List<Customer> listCus = cusDao.findAll();
    List<Supplier> listSup = supDao.findAll();
    ArrayList<String> listSearchProduct = new ArrayList<>();

    /**
     * Creates new form Sell
     */
    public Sell() {
        initComponents();
        paymentPanel.setPreferredSize(new Dimension(400, 0));
        for (ProductCategory cate : listCate) {
            cboCategory.addItem(cate.getCategoryName());
        }
        for (Customer cus : listCus) {
            cboCustomer.addItem(cus.getFullName());
        }
        for (Supplier sup : listSup) {
            cboSupplier.addItem(sup.getSupplierName());
        }
        fillCartItem();
    }

    public void fillCartItemSell() {
        panelItems.removeAll();
        JPanel panelCartItem = new JPanel(new MigLayout("wrap 1", "[grow]", "[]"));
        Double totalPrice = 0.0;
        Double totalPayment = 0.0;
        Double totalVat = 0.0;
        for (CartItemSell prod : listCartItem) {
            totalPrice += prod.getQuantity() * Double.valueOf(prod.getPrice().toString());
            CartItemSell cartItem = new CartItemSell(prod.getProductId(), prod.getProductImage(), prod.getName(), prod.getQuantity(), prod.getPrice());
            panelCartItem.add(new CartItemDetail(cartItem, new ActionCartItem() {
                @Override
                public void delete(CartItemSell item) {
                    Product prod = dao.findById(item.getProductId());
                    list.add(prod);
                    listCartItem.removeIf(cart -> cart.getProductId().equalsIgnoreCase(item.getProductId()));
                    listSearchProduct.remove(item.getProductId());
                    fillCartItemSell();
                    fillCartItem();
                }

                @Override
                public void update(CartItemSell item) {
                    for (CartItemSell cartItem : listCartItem) {
                        if (cartItem.getProductId().equalsIgnoreCase(item.getProductId())) {
                            cartItem.setQuantity(item.getQuantity());
                            break;
                        }
                    }
                    fillCartItemSell();
                }
            }));
        }
        totalVat = totalPrice / 10;
        totalPayment = totalPrice + totalVat;
        txtPriceVat.setText(totalVat.toString() + "đ");
        txtTotalPrice.setText(totalPrice.toString() + "đ");
        txtTotalAmount.setText(totalPayment.toString() + "đ");
        JScrollPane scrollPaneItems = new JScrollPane(panelCartItem);
        scrollPaneItems.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelItems.setLayout(new BorderLayout());
        scrollPaneItems.getVerticalScrollBar().setUnitIncrement(16);
        panelItems.add(scrollPaneItems, BorderLayout.CENTER);
        panelItems.revalidate();
        panelItems.repaint();
    }

    public void fillCartItem() {
        mainPanel.removeAll();
        JPanel panel = new JPanel(new MigLayout("wrap 5", "[grow, fill]", "[]"));
        for (Product prod : list) {
            panel.add(new CartItem(prod, (Product product) -> {
                int quantity = -1;
                do {
                    String input = JOptionPane.showInputDialog(null, "Nhập số lượng:");
                    if (input == null) {
                        JOptionPane.showMessageDialog(null, "Hủy nhập số lượng.");
                        break;
                    }
                    try {
                        quantity = Integer.parseInt(input);
                        if (quantity <= 0) {
                            JOptionPane.showMessageDialog(null, "Vui lòng nhập số lớn hơn 0.");
                            quantity = -1;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập một số nguyên hợp lệ.");
                    }
                } while (quantity <= 0);

                if (quantity > 0) {
                    listCartItem.add(new CartItemSell(prod.getProductID(), prod.getProductImage(), prod.getProductName(), quantity, prod.getPrice()));
                    listSearchProduct.add(prod.getProductID());
                    list.remove(prod);
                    fillCartItemSell();
                    fillCartItem();
                }
            }));
        }
        JScrollPane scrollPaneItems = new JScrollPane(panel);
        scrollPaneItems.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.setLayout(new BorderLayout());
        scrollPaneItems.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPaneItems, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        mainPanel = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        cboSupplier = new javax.swing.JComboBox<>();
        cboCategory = new javax.swing.JComboBox<>();
        paymentPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cboCustomer = new javax.swing.JComboBox<>();
        panelItems = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        btnPayment = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        cboPaymentMethod = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtTotalPrice = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtPriceVat = new javax.swing.JLabel();
        txtTotalAmount = new javax.swing.JLabel();

        main.setPreferredSize(new java.awt.Dimension(1098, 829));

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 22)); // NOI18N
        jLabel2.setText("Bán hàng");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Tạo hóa đơn và xử lý thanh toán");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Chọn sản phẩm");

        mainPanel.setPreferredSize(new java.awt.Dimension(800, 642));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 642, Short.MAX_VALUE)
        );

        btnSearch.setText("Lọc");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        cboSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        cboSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSupplierActionPerformed(evt);
            }
        });

        cboCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        cboCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboSupplier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel4);

        paymentPanel.setBackground(new java.awt.Color(255, 255, 255));
        paymentPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        paymentPanel.setMaximumSize(new java.awt.Dimension(350, 32767));
        paymentPanel.setPreferredSize(new java.awt.Dimension(400, 534));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("Giỏ hàng");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Khách hàng");

        panelItems.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelItemsLayout = new javax.swing.GroupLayout(panelItems);
        panelItems.setLayout(panelItemsLayout);
        panelItemsLayout.setHorizontalGroup(
            panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );
        panelItemsLayout.setVerticalGroup(
            panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 354, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        btnPayment.setBackground(new java.awt.Color(0, 0, 0));
        btnPayment.setForeground(new java.awt.Color(255, 255, 255));
        btnPayment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/supermartket/icons/icons8-calculator-20.png"))); // NOI18N
        btnPayment.setText("Thanh toán");
        btnPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentActionPerformed(evt);
            }
        });

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/supermartket/icons/icons8-print-20.png"))); // NOI18N

        cboPaymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "Chuyển khoản" }));

        jLabel18.setText("Phương thức thanh toán");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Tạm tính: ");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Giảm giá:");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("VAT (10%):");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setText("Tổng cộng:");

        txtTotalPrice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTotalPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTotalPrice.setText("0đ");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("0đ");

        txtPriceVat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtPriceVat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtPriceVat.setText("0đ");

        txtTotalAmount.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotalAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTotalAmount.setText("0đ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                                .addComponent(txtTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtPriceVat, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cboPaymentMethod, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(btnPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(21, 21, 21))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtTotalPrice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtPriceVat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtTotalAmount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout paymentPanelLayout = new javax.swing.GroupLayout(paymentPanel);
        paymentPanel.setLayout(paymentPanelLayout);
        paymentPanelLayout.setHorizontalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboCustomer, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paymentPanelLayout.createSequentialGroup()
                        .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        paymentPanelLayout.setVerticalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.add(paymentPanel);

        javax.swing.GroupLayout mainLayout = new javax.swing.GroupLayout(main);
        main.setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainLayout.setVerticalGroup(
            mainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, 832, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        String cateId = null;
        String supId = null;
        for (ProductCategory category : listCate) {
            if (category.getCategoryName().equalsIgnoreCase(cboCategory.getSelectedItem().toString())) {
                cateId = category.getCategoryID();
                break;
            }
        }
        for (Supplier sup : listSup) {
            if (sup.getSupplierName().equalsIgnoreCase(cboSupplier.getSelectedItem().toString())) {
                supId = sup.getSupplierID();
                break;
            }
        }
        list = dao.findBySearch(new SearchProductDTO(txtSearch.getText().trim(), cateId, supId, listSearchProduct, 1));
        fillCartItem();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void cboSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSupplierActionPerformed

    private void cboCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCategoryActionPerformed

    private void btnPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentActionPerformed
        // TODO add your handling code here:
        String invoiceID = "INV" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Date invoiceDate = new Date(); // Ngày hiện tại
        LocalTime invoiceTime = LocalTime.now();
        String employeeID = XAuth.user.getEmployeeCode();
        String customerID = null;

        for (Customer cus : listCus) {
            if (cus.getFullName().equalsIgnoreCase(cboCustomer.getSelectedItem().toString())) {
                customerID = cus.getCustomerID();
                break;
            }
        }

        BigDecimal totalAmount = new BigDecimal(txtTotalAmount.getText().replaceAll("[^\\d]", ""));
        int totalQuantity = 0;
        for (CartItemSell cartItemSell : listCartItem) {
            totalQuantity += cartItemSell.getQuantity();
        }

        String paymentMethod = cboPaymentMethod.getSelectedItem().toString();
        String status = "Đã thanh toán";

        Invoice invoice = Invoice.builder()
                .invoiceID(invoiceID)
                .invoiceDate(invoiceDate)
                .invoiceTime(Time.valueOf(invoiceTime))
                .employeeID(employeeID)
                .customerID((customerID == null || customerID.isEmpty()) ? null : customerID)
                .totalAmount(totalAmount)
                .totalQuantity(totalQuantity)
                .paymentMethod(paymentMethod)
                .status(status)
                .build();

        try (Connection conn = XJdbc.openConnection()) {
            try {
                conn.setAutoCommit(false); // ✅ Bắt đầu transaction

                invoiceDao.create(invoice, conn); // tạo hóa đơn
                for (CartItemSell item : listCartItem) {
                    InvoiceDetail detail = InvoiceDetail.builder()
                            .invoiceID(invoiceID)
                            .productID(item.getProductId())
                            .unitPrice(item.getPrice())
                            .quantity(item.getQuantity())
                            .build();
                    invoiceDetailDao.create(detail, conn);
                }

                conn.commit(); // ✅ Xác nhận thành công
                XDialog.alert("Thanh toán thành công!");

                // Reset
                list = dao.findAll();
                listCartItem = new ArrayList<>();
                listSearchProduct = new ArrayList<>();
                fillCartItem();
                fillCartItemSell();

            } catch (Exception ex) {
                conn.rollback();
                ex.printStackTrace();
                XDialog.alert("Lỗi khi thanh toán: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            XDialog.alert("Không thể kết nối CSDL: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnPaymentActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPayment;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cboCategory;
    private javax.swing.JComboBox<String> cboCustomer;
    private javax.swing.JComboBox<String> cboPaymentMethod;
    private javax.swing.JComboBox<String> cboSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel main;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel panelItems;
    private javax.swing.JPanel paymentPanel;
    private javax.swing.JLabel txtPriceVat;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JLabel txtTotalAmount;
    private javax.swing.JLabel txtTotalPrice;
    // End of variables declaration//GEN-END:variables

}
