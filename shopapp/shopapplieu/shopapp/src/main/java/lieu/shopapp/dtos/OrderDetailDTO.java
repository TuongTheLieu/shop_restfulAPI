package lieu.shopapp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "id phai lon hon 0")
    private long orderId;
    @JsonProperty("product_id")
    @Min(value = 1, message = "id phai lon hon 0")
    private long productId;
    @Min(value = 1, message = "gia phai >=0")
    private Float price;
    @JsonProperty("number_of_products")
    @Min(value = 1, message = "so luong phai >= 1")
    private int numberOfProducts;
    @JsonProperty("total_money")
    @Min(value = 0, message = "id phai >= 0")
    private Float totalMoney;
    private String color;
}
