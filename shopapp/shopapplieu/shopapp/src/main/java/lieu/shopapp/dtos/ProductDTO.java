package lieu.shopapp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must between 3 and 200 character")
    private String name;
    private String description;
    @Min(value = 0, message = "Price must be >= 0")
    private Float price;
    private String thumbnail;
    @JsonProperty("category_id")
    private Long categoryId;
}
