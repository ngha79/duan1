/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package supermartket.ui.manager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import supermartket.ui.comp.CartItem;
import net.miginfocom.swing.MigLayout;
import supermartket.dao.ActionCartItem;
import supermartket.dao.CustomerDAO;
import supermartket.dao.InvoiceDetailDAO;
import supermartket.dao.PaymentListener;
import supermartket.dao.ProductCategoryDAO;
import supermartket.dao.ProductDAO;
import supermartket.dao.PromotionDAO;
import supermartket.dao.SelectCustomerListener;
import supermartket.dao.SupplierDAO;
import supermartket.dao.dto.SearchProductActiveDTO;
import supermartket.dao.dto.SearchProductDTO;
import supermartket.dao.impl.CartItemSell;
import supermartket.dao.impl.CustomerDAOImpl;
import supermartket.dao.impl.InvoiceDAOImpl;
import supermartket.dao.impl.InvoiceDetailDAOImpl;
import supermartket.dao.impl.ProductCategoryDAOImpl;
import supermartket.dao.impl.ProductDAOImpl;
import supermartket.dao.impl.PromotionDAOImpl;
import supermartket.dao.impl.SupplierDAOImpl;
import supermartket.entity.Customer;
import supermartket.entity.Invoice;
import supermartket.entity.InvoiceDetail;
import supermartket.entity.Product;
import supermartket.entity.ProductCategory;
import supermartket.entity.Promotion;
import supermartket.entity.Supplier;
import supermartket.entity.XAuth;
import supermartket.pagination.EventPagination;
import supermartket.ui.comp.CartItemDetail;
import supermartket.ui.form.ConfirmPaymentJDialog;
import supermartket.ui.form.SelectCustomer;
import supermartket.util.XDialog;
import supermartket.util.XJdbc;

public final class Sell extends javax.swing.JPanel {
    int currentPage = 1;
    ProductDAO dao = new ProductDAOImpl();
    ProductCategoryDAO cateDao = new ProductCategoryDAOImpl();
    CustomerDAO cusDao = new CustomerDAOImpl();
    SupplierDAO supDao = new SupplierDAOImpl();
    InvoiceDAOImpl invoiceDao = new InvoiceDAOImpl();
    InvoiceDetailDAO invoiceDetailDao = new InvoiceDetailDAOImpl();
    PromotionDAO promotionDAO = new PromotionDAOImpl();

    List<Product> list;
    List<CartItemSell> listCartItem = new ArrayList<>();
    List<ProductCategory> listCate = cateDao.findAll();
    List<Supplier> listSup = supDao.findAll();
    ArrayList<String> listSearchProduct = new ArrayList<>();

    /**
     * Creates new form Sell
     */
    public Sell() {
        initComponents();
        paymentPanel.setPreferredSize(new Dimension(400, 0));
        listCate.forEach(cate -> cboCategory.addItem(cate.getCategoryName()));
        listSup.forEach(sup -> cboSupplier.addItem(sup.getSupplierName()));
        fillTable(currentPage);
        renderCartPanel();

        panelToShow.setVisible(false);

        cboPaymentMethod.addActionListener(e -> {
            String selected = (String) cboPaymentMethod.getSelectedItem();
            if ("Cả 2".equals(selected)) {
                panelToShow.setVisible(true);
            } else {
                panelToShow.setVisible(false);
            }

            panelToShow.getParent().revalidate();
            panelToShow.getParent().repaint();
        });

        txtCash.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            void calculateChange() {
                String text = txtCash.getText().trim().replace(",", "");
                if (text.isEmpty()) {
                    return;
                }
                try {
                    String rawTotal = txtTotalAmount.getText()
                            .replace("đ", "")
                            .replace(",", "")
                            .trim();
                    BigDecimal totalAmount = new BigDecimal(rawTotal);

                    BigDecimal moneyGiven = new BigDecimal(text);
                    BigDecimal change = moneyGiven.subtract(totalAmount);
                    if (change.compareTo(BigDecimal.ZERO) < 0) {
                        txtConten.setText("Khách còn thiếu: ");
                        txtPay.setText(change.abs() + " đ");
                    } else {
                        txtConten.setText("Tiền trả khách: ");
                        txtPay.setText(change + " đ");
                    }
                } catch (NumberFormatException ex) {
                    XDialog.alert("Tiền trả khách: lỗi nhập số");
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calculateChange();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calculateChange();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calculateChange();
            }
        });

        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                fillTable(page);
                currentPage = page;
            }

        });
    }

    private void renderCartPanel() {
        panelItems.removeAll();
        JPanel panelCartItem = new JPanel(new MigLayout("wrap 1", "[grow]", "[]"));

        for (CartItemSell prod : listCartItem) {
            panelCartItem.add(createCartItemDetail(prod));
        }
        updatePrice();

        JScrollPane scrollPane = createScrollPane(panelCartItem);
        panelItems.setLayout(new BorderLayout());
        panelItems.add(scrollPane, BorderLayout.CENTER);
        panelItems.revalidate();
        panelItems.repaint();
    }

    private void updatePrice() {
        double totalPrice = 0.0;
        for (CartItemSell prod : listCartItem) {
            totalPrice += prod.getQuantity() * prod.getPrice().doubleValue();
        }

        double discountAmount = 0.0;

        String customerId = txtCustomer.getText().trim();
        if (!customerId.equals("CUST0001")) {
            Promotion promotion = promotionDAO.getPromotionForCustomer(customerId);
            if (promotion != null) {
                double amount = Double.parseDouble(promotion.getDiscountAmount().toString());
                double maxAmount = Double.parseDouble(promotion.getMaxDiscountAmount().toString());
                double percentDiscount = Double.parseDouble(promotion.getDiscountPercent().toString());
                discountAmount = totalPrice * percentDiscount / 100.0;
                if (maxAmount > 0.0 && discountAmount > maxAmount) {
                    discountAmount = maxAmount;
                }
                if (amount > 0.0) {
                    discountAmount = Double.parseDouble(promotion.getDiscountAmount().toString());
                }
            }
        }
        double totalPayment = totalPrice - discountAmount;
        updateCartTotals(totalPrice, discountAmount, totalPayment);
    }

    private CartItemDetail createCartItemDetail(CartItemSell prod) {
        return new CartItemDetail(prod, new ActionCartItem() {
            @Override
            public void delete(CartItemSell item) {
                removeCartItem(item);
            }

            @Override
            public void update(CartItemSell item) {
                updateCartItemQuantity(item);
            }
        });
    }

    private void updateCartTotals(double totalPrice, double discountAmount, double totalPayment) {
        DecimalFormat df = new DecimalFormat("#,##0.00");

        txtDiscount.setText(df.format(discountAmount) + "đ");
        txtTotalPrice.setText(df.format(totalPrice) + "đ");
        txtTotalAmount.setText(df.format(totalPayment) + "đ");
    }

    private void removeCartItem(CartItemSell item) {
        Product prod = dao.findById(item.getProductId());
        list.add(prod);
        listCartItem.removeIf(cart -> cart.getProductId().equalsIgnoreCase(item.getProductId()));
        listSearchProduct.remove(item.getProductId());
        renderCartPanel();
        renderProductPanel();
    }

    private void updateCartItemQuantity(CartItemSell item) {
        listCartItem.stream()
                .filter(c -> c.getProductId().equalsIgnoreCase(item.getProductId()))
                .findFirst()
                .ifPresent(c -> c.setQuantity(item.getQuantity()));
        renderCartPanel();
    }

    private void renderProductPanel() {
        mainPanel.removeAll();
        JPanel panel = new JPanel(new MigLayout("wrap 5", "[grow, fill]", "[]"));
        for (Product prod : list) {
            panel.add(createProductItem(prod));
        }

        JScrollPane scrollPane = createScrollPane(panel);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private CartItem createProductItem(Product prod) {
        return new CartItem(prod, (Product product) -> {
            int quantity = promptQuantity(prod);
            if (quantity > 0) {
                addProductToCart(product, quantity);
            }
        });
    }

    private int promptQuantity(Product prod) {
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
                if (quantity > prod.getQuantity()) {
                    JOptionPane.showMessageDialog(null, "Sản phẩm chỉ còn lại " + prod.getQuantity());
                    quantity = -1;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập một số nguyên hợp lệ.");
            }
        } while (quantity <= 0);
        return quantity;
    }

    private void addProductToCart(Product prod, int quantity) {
        listCartItem.add(new CartItemSell(
                prod.getProductID(),
                prod.getProductImage(),
                prod.getProductName(),
                quantity,
                prod.getQuantity(),
                prod.getPrice()
        ));
        listSearchProduct.add(prod.getProductID());
        list.remove(prod);
        renderCartPanel();
        renderProductPanel();
    }

    private JScrollPane createScrollPane(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private void fillTable(int page) {
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
        SearchProductActiveDTO dto = new SearchProductActiveDTO(txtSearch.getText().trim(), cateId, supId, listSearchProduct, page);
        list = dao.findBySearchActive(dto);
        int count = dao.getTotalItemActive(dto).getCount();
        int limit = 10;
        int totalPage = (int) Math.ceil((double) count / limit);
        pagination1.setPagegination(page, totalPage);
        renderProductPanel();
    }

    public void createInvoice(String status) {
        Date invoiceDate = new Date(); // Ngày hiện tại
        LocalTime invoiceTime = LocalTime.now();
        String employeeID = XAuth.user.getEmployeeID();
        String customerID = txtCustomer.getText().trim();

        String rawTotal = txtTotalPrice.getText()
                .replace("đ", "")
                .replace(",", "")
                .trim();
        String rawTotalDiscount = txtDiscount.getText()
                .replace("đ", "")
                .replace(",", "")
                .trim();

        BigDecimal totalAmount;
        try {
            totalAmount = new BigDecimal(rawTotal);
            if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                XDialog.alert("Tổng tiền phải lớn hơn 0.");
                return;
            }
        } catch (NumberFormatException e) {
            XDialog.alert("Tổng tiền không hợp lệ.");
            return;
        }

        BigDecimal applyDiscount = new BigDecimal(rawTotalDiscount);

        if (listCartItem == null || listCartItem.isEmpty()) {
            XDialog.alert("Giỏ hàng đang trống, không thể thanh toán.");
            return;
        }

        int totalQuantity = 0;
        for (CartItemSell cartItemSell : listCartItem) {
            totalQuantity += cartItemSell.getQuantity();
        }

        String paymentMethod = cboPaymentMethod.getSelectedItem().toString();

        Invoice invoice = Invoice.builder()
                .invoiceDate(invoiceDate)
                .invoiceTime(Time.valueOf(invoiceTime))
                .employeeID(employeeID)
                .customerID((customerID == null || customerID.isEmpty()) ? "CUST0001" : customerID)
                .totalAmount(totalAmount)
                .discountApplied(applyDiscount)
                .totalQuantity(totalQuantity)
                .paymentMethod(paymentMethod)
                .status(status)
                .build();

        try (Connection conn = XJdbc.openConnection()) {
            try {
                conn.setAutoCommit(false);
                invoiceDao.create(invoice, conn);
                Invoice inv = invoiceDao.findByDate();
                for (CartItemSell item : listCartItem) {
                    InvoiceDetail detail = InvoiceDetail.builder()
                            .invoiceID(inv.getInvoiceID())
                            .productID(item.getProductId())
                            .unitPrice(item.getPrice())
                            .quantity(item.getQuantity())
                            .build();
                    invoiceDetailDao.create(detail, conn);
                }
                conn.commit();

                list = dao.findAll();
                listCartItem = new ArrayList<>();
                listSearchProduct = new ArrayList<>();
                renderProductPanel();
                renderCartPanel();
            } catch (Exception ex) {
                conn.rollback();
                ex.printStackTrace();
                XDialog.alert("Lỗi khi thanh toán: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            XDialog.alert("Không thể kết nối CSDL: " + ex.getMessage());
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
        pagination1 = new supermartket.pagination.Pagination();
        paymentPanel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        panelItems = new javax.swing.JPanel();
        a = new javax.swing.JPanel();
        btnPayment = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        cboPaymentMethod = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtTotalPrice = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JLabel();
        txtTotalAmount = new javax.swing.JLabel();
        panelToShow = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtPay = new javax.swing.JLabel();
        txtCash = new javax.swing.JTextField();
        txtConten = new javax.swing.JLabel();
        txtCustomer = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnCustomer = new javax.swing.JButton();
        txtCustomerName = new javax.swing.JLabel();

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
            .addGap(0, 660, Short.MAX_VALUE)
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
                    .addComponent(mainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pagination1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pagination1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.add(jPanel4);

        paymentPanel.setBackground(new java.awt.Color(255, 255, 255));
        paymentPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        paymentPanel.setMaximumSize(new java.awt.Dimension(350, 32767));
        paymentPanel.setPreferredSize(new java.awt.Dimension(400, 534));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("Giỏ hàng");

        panelItems.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelItemsLayout = new javax.swing.GroupLayout(panelItems);
        panelItems.setLayout(panelItemsLayout);
        panelItemsLayout.setHorizontalGroup(
            panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 384, Short.MAX_VALUE)
        );
        panelItemsLayout.setVerticalGroup(
            panelItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 315, Short.MAX_VALUE)
        );

        a.setBackground(new java.awt.Color(255, 255, 255));

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

        cboPaymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "Chuyển khoản", "Cả 2" }));

        jLabel18.setText("Phương thức thanh toán");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Tạm tính: ");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Giảm giá:");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setText("Tổng cộng:");

        txtTotalPrice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTotalPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTotalPrice.setText("0đ");

        txtDiscount.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtDiscount.setText("0đ");

        txtTotalAmount.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotalAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTotalAmount.setText("0đ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel5.setText("Đã trả");

        txtPay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        txtCash.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtConten.setText("Còn phải trả");

        javax.swing.GroupLayout panelToShowLayout = new javax.swing.GroupLayout(panelToShow);
        panelToShow.setLayout(panelToShowLayout);
        panelToShowLayout.setHorizontalGroup(
            panelToShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelToShowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelToShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelToShowLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtConten, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelToShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCash, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addComponent(txtPay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelToShowLayout.setVerticalGroup(
            panelToShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelToShowLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelToShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCash, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelToShowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPay, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtConten))
                .addContainerGap())
        );

        javax.swing.GroupLayout aLayout = new javax.swing.GroupLayout(a);
        a.setLayout(aLayout);
        aLayout.setHorizontalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aLayout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                        .addComponent(txtTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aLayout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aLayout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cboPaymentMethod, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aLayout.createSequentialGroup()
                        .addComponent(btnPayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelToShow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(aLayout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(21, 21, 21))
        );
        aLayout.setVerticalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aLayout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtTotalPrice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtDiscount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtTotalAmount))
                .addGap(5, 5, 5)
                .addComponent(panelToShow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9))
        );

        txtCustomer.setText("CUST0001");

        jLabel4.setText("Khách hàng");

        btnCustomer.setText("Chọn");
        btnCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerActionPerformed(evt);
            }
        });

        txtCustomerName.setText("Khách vãng lai");

        javax.swing.GroupLayout paymentPanelLayout = new javax.swing.GroupLayout(paymentPanel);
        paymentPanel.setLayout(paymentPanelLayout);
        paymentPanelLayout.setHorizontalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelItems, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(a, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCustomerName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addComponent(txtCustomer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCustomer))
                    .addGroup(paymentPanelLayout.createSequentialGroup()
                        .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paymentPanelLayout.setVerticalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(3, 3, 3)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(a, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(main, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        fillTable(1);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void cboSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSupplierActionPerformed

    private void cboCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboCategoryActionPerformed

    private void btnPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentActionPerformed
        // TODO add your handling code here:
        ConfirmPaymentJDialog confirm = new ConfirmPaymentJDialog(null, true, new PaymentListener() {
            @Override
            public void onConfirm() {
                createInvoice("Đã thanh toán");
            }

            @Override
            public void onCancel() {
                createInvoice("Chưa thanh toán");
            }
        });
        confirm.setLocationRelativeTo(null);
        confirm.setVisible(true);
    }//GEN-LAST:event_btnPaymentActionPerformed

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed
        // TODO add your handling code here:
        SelectCustomer comp = new SelectCustomer((Frame) SwingUtilities.getWindowAncestor(this), true, new SelectCustomerListener() {
            @Override
            public void onSelected(Customer item) {
                txtCustomer.setText(item.getCustomerID());
                txtCustomerName.setText(item.getFullName());
                updatePrice();
            }

            @Override
            public void onCreate(Customer item) {
                txtCustomer.setText(item.getCustomerID());
                txtCustomerName.setText(item.getFullName());
                updatePrice();
            }

        });
        comp.setLocationRelativeTo(null);
        comp.setVisible(true);
    }//GEN-LAST:event_btnCustomerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel a;
    private javax.swing.JButton btnCustomer;
    private javax.swing.JButton btnPayment;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cboCategory;
    private javax.swing.JComboBox<String> cboPaymentMethod;
    private javax.swing.JComboBox<String> cboSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel main;
    private javax.swing.JPanel mainPanel;
    private supermartket.pagination.Pagination pagination1;
    private javax.swing.JPanel panelItems;
    private javax.swing.JPanel panelToShow;
    private javax.swing.JPanel paymentPanel;
    private javax.swing.JTextField txtCash;
    private javax.swing.JLabel txtConten;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JLabel txtCustomerName;
    private javax.swing.JLabel txtDiscount;
    private javax.swing.JLabel txtPay;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JLabel txtTotalAmount;
    private javax.swing.JLabel txtTotalPrice;
    // End of variables declaration//GEN-END:variables

}
