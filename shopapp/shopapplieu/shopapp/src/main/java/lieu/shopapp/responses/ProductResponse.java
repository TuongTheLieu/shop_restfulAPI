package lieu.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lieu.shopapp.models.Product;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private String name;
    private String description;
    private Float price;
    private String thumbnail;
    @JsonProperty("category_id")
    private Long categoryId;

    public static ProductResponse fromproduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .thumbnail(product.getThumbnail())
                .categoryId(product.getCategory().getId())
                .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
