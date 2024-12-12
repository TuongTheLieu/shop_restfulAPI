package lieu.shopapp.controller;

import jakarta.validation.Valid;
import lieu.shopapp.dtos.OrderDTO;
import lieu.shopapp.models.Order;
import lieu.shopapp.repositories.OrderRepository;
import lieu.shopapp.responses.OrderResponse;
import lieu.shopapp.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/${api.prefix}/orders")
public class OrderController {
    private final OrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createOrder (@Valid @RequestBody OrderDTO orderDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Order order = orderService.createOrder(orderDTO);
            return ResponseEntity.ok().body(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder (@Valid @PathVariable("id") long id) {
        try {
            Order existingOrder =  orderService.getOrder(id);
            return ResponseEntity.ok().body(existingOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // lấy ra tất cả order của userId có trong order
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getOrders (@Valid @PathVariable("id") long orderId) {
        try {
            List <Order> orders = orderService.findByUserId(orderId);
            return ResponseEntity.ok().body(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder (@Valid @PathVariable long id, @RequestBody OrderDTO orderDTO, BindingResult result) {
        try {
            Order updateOrder = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok().body(updateOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder (@Valid @PathVariable long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok().body("xoa thanh cong order");
    }
}
