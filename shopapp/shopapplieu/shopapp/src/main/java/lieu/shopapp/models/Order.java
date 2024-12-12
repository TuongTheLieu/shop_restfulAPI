package lieu.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "fullname")
    private String fullname;
    private String email;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    private String address;
    private String note;
    @Column(name = "order_date")
    private Date orderDate;
    private String status;
    @Column(name = "total_money")
    private Integer totalMoney;
    @Column(name = "shipping_method")
    private String shippingMethod;
    @Column(name = "shipping_address")
    private String shippingAddress;
    @Column(name = "shipping_date")
    private LocalDate shippingDate;
    @Column(name = "tracking_number")
    private String trackingNumber;
    @Column(name = "payment_method")
    private String paymentMethod;
    private Boolean active;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
