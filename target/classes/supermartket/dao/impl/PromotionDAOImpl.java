package supermartket.dao.impl;

import java.util.ArrayList;
import java.util.List;
import supermartket.dao.CustomerDAO;
import supermartket.dao.PromotionDAO;
import supermartket.dao.dto.CustomerDTO;
import supermartket.dao.dto.CustomerInfoDTO;
import supermartket.dao.dto.CustomerTable;
import supermartket.dao.dto.PageDTO;
import supermartket.dao.dto.PromotionDTO;
import supermartket.entity.Customer;
import supermartket.entity.Promotion;
import supermartket.util.XJdbc;
import supermartket.util.XQuery;

public class PromotionDAOImpl implements PromotionDAO {

    private final String insertSql = """
        INSERT INTO Promotion (PromotionName, MinTotalAmount, DiscountPercent, DiscountAmount,MaxDiscountAmount, StartDate, EndDate, Status)
        VALUES (?, ?, ?, ?, ?, ?, ?,?)
    """;

    private final String updateSql = """
        UPDATE Promotion
        SET PromotionName = ?, MinTotalAmount = ?, DiscountPercent = ?, DiscountAmount = ?, MaxDiscountAmount = ?, 
            StartDate = ?, EndDate = ?, Status = ?, updatedAt = GETDATE()
        WHERE PromotionID = ?
    """;

    private final String deleteSql = "DELETE FROM Promotion WHERE PromotionID = ?";

    private final String findByIdSql = "SELECT * FROM Promotion WHERE PromotionID = ?";

    private final String findAllSql = "SELECT * FROM Promotion ORDER BY createdAt DESC";

    private final String findAllSearchSql = """
                                            SELECT * FROM Promotion
                                            WHERE 
                                            (? IS NULL OR PromotionID LIKE ?) AND
                                            (? IS NULL OR PromotionName LIKE ?) AND
                                            (? IS NULL OR StartDate >= ?) AND
                                            (? IS NULL OR EndDate <= ?) AND
                                            (? IS NULL OR Status = ?)
                                            ORDER BY PromotionID
                                            OFFSET (? - 1) * 10 ROWS
                                            FETCH NEXT 10 ROWS ONLY
                                            """;
    private final String getTotalItemSql = """
                                            SELECT count(*) as count FROM Promotion
                                            WHERE 
                                            (? IS NULL OR PromotionID LIKE ?) AND
                                            (? IS NULL OR PromotionName LIKE ?) AND
                                            (? IS NULL OR StartDate >= ?) AND
                                            (? IS NULL OR EndDate <= ?) AND
                                            (? IS NULL OR Status = ?)
                                            """;
    
    private final String getPromotionForCustomerSql = """
                                            SELECT TOP 1 *
                                            FROM Promotion
                                            WHERE MinTotalAmount <= (SELECT SUM(TotalAmount) 
                                                                    FROM Invoice 
                                                                    WHERE CustomerID = ? AND Status = N'Đã thanh toán'
                                                                    )
                                            AND Status = 1
                                            AND GETDATE() BETWEEN StartDate AND EndDate
                                            ORDER BY MinTotalAmount DESC
                                            """;

    @Override
    public Promotion create(Promotion entity) {
        XJdbc.executeUpdate(insertSql,
                entity.getPromotionName(),
                entity.getMinTotalAmount(),
                entity.getDiscountPercent(),
                entity.getDiscountAmount(),
                entity.getMaxDiscountAmount(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus()
        );
        return entity;
    }

    @Override
    public void update(Promotion entity) {
        System.out.println(entity);
        XJdbc.executeUpdate(updateSql,
                entity.getPromotionName(),
                entity.getMinTotalAmount(),
                entity.getDiscountPercent(),
                entity.getDiscountAmount(),
                entity.getMaxDiscountAmount(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus(),
                entity.getPromotionID()
        );
    }

    @Override
    public void deleteById(String id) {
        XJdbc.executeUpdate(deleteSql, id);
    }

    @Override
    public List<Promotion> findAll() {
        return XQuery.getBeanList(Promotion.class, findAllSql);
    }

    @Override
    public Promotion findById(String id) {
        return XQuery.getSingleBean(Promotion.class, findByIdSql, id);
    }

    @Override
    public List<Promotion> findAll(PromotionDTO dto) {
        StringBuilder sql = new StringBuilder(findAllSearchSql);
        List<Object> params = new ArrayList<>();
        params.add(dto.getName() == null ? null : "%" + dto.getName() + "%");
        params.add(dto.getName() == null ? null : "%" + dto.getName() + "%");

        params.add(dto.getName() == null ? null : "%" + dto.getName() + "%");
        params.add(dto.getName() == null ? null : "%" + dto.getName() + "%");

        params.add(dto.getStartDate());
        params.add(dto.getStartDate());

        params.add(dto.getEndDate());
        params.add(dto.getEndDate());

        params.add(dto.getStatus());
        params.add(dto.getStatus());

        params.add(dto.getPage());
        return XQuery.getBeanList(Promotion.class, sql.toString(), params.toArray());
    }

    @Override
    public PageDTO getTotalItem(PromotionDTO dto) {
        StringBuilder sql = new StringBuilder(getTotalItemSql);
        List<Object> params = new ArrayList<>();
        params.add(dto.getName() == null ? null : "%" + dto.getName() + "%");
        params.add(dto.getName() == null ? null : "%" + dto.getName() + "%");

        params.add(dto.getName() == null ? null : "%" + dto.getName() + "%");
        params.add(dto.getName() == null ? null : "%" + dto.getName() + "%");

        params.add(dto.getStartDate());
        params.add(dto.getStartDate());

        params.add(dto.getEndDate());
        params.add(dto.getEndDate());

        params.add(dto.getStatus());
        params.add(dto.getStatus());

        return XQuery.getSingleBean(PageDTO.class, sql.toString(), params.toArray());

    }

    @Override
    public Promotion getPromotionForCustomer(String customerId) {
        return XQuery.getSingleBean(Promotion.class, getPromotionForCustomerSql, customerId);
    }

}
