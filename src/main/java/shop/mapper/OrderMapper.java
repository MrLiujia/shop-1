package shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import shop.model.Order;
import shop.model.OrderItem;

public interface OrderMapper {

    void create(Order order);

    void addItem(OrderItem orderItem);

    Order findOne(@Param("userId") Long userId, 
                  @Param("orderId") Long orderId);

    List<Order> findAll(Long userId);

    void setTotalAmount(@Param("id") Long id, 
                        @Param("totalAmount") Integer totalAmount);

    Order findOneToPay(Long orderId);

    void setStateToPaid(Long orderId);

    Integer deleteCanceledOrders();

}
