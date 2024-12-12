package lieu.shopapp.services;

import lieu.shopapp.dtos.OrderDetailDTO;
import lieu.shopapp.exceptions.DataNotFoundException;
import lieu.shopapp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    OrderDetail getOrderDetail(long id) throws DataNotFoundException;
    List<OrderDetail> findByOrderId(long id);
    OrderDetail updateOrderDetail(long id,OrderDetailDTO orderDetailDTO) throws Exception;
    void deleteOrderDetail(long id);

}
