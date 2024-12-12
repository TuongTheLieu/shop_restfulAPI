package lieu.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @JsonProperty("user_id")
    @Min(value = 1, message = "Id lon hon 1")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;
    private String email;
    private String address;
    @JsonProperty("phone_number")
    @NotBlank(message = "sdt khong duoc de trong")
    private String phoneNumber;
    private String note;
    @JsonProperty("total_money")
    @Min(value = 0, message = "tien phai lon hon 0")
    private Double totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("payment_method")
    private String paymentMethod;
}
