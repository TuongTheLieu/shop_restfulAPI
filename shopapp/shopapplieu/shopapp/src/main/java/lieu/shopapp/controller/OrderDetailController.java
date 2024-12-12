package lieu.shopapp.controller;


import jakarta.validation.Valid;
import lieu.shopapp.dtos.OrderDetailDTO;
import lieu.shopapp.exceptions.DataNotFoundException;
import lieu.shopapp.models.OrderDetail;
import lieu.shopapp.responses.OrderDetailResponse;
import lieu.shopapp.services.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    @PostMapping("")
    public ResponseEntity<?> createOrderDetails(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable Long id) throws DataNotFoundException {
        // tùy thích cách trả về
//        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
//        return ResponseEntity.ok().body(orderDetail);
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable Long orderId) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                // 2 dòng như nhau
                // orderDetail đại diện cho từng phần từng trong list orderDetails
//                .stream().map(orderDetail -> OrderDetailResponse.fromOrderDetail(orderDetail)).toList();
                .stream().map(OrderDetailResponse::fromOrderDetail).toList();
        return ResponseEntity.ok().body(orderDetailResponses);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetails(@Valid @PathVariable Long id, @RequestBody OrderDetailDTO orderDetailDTO) throws Exception {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok().body(orderDetail);
        }catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetails(@Valid @PathVariable Long id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok().body("xóa thành công order detail với id "+id);
    }
}
