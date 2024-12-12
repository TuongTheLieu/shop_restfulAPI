package lieu.shopapp.services;

import lieu.shopapp.dtos.OrderDTO;
import lieu.shopapp.exceptions.DataNotFoundException;
import lieu.shopapp.models.Order;
import lieu.shopapp.responses.OrderResponse;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order updateOrder(Long id ,OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(Long orderId);
    Order getOrder(Long orderId);
    List<Order> findByUserId(long orderId);

}
