package lieu.shopapp.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "sdt khong dc de trong")
    private String phoneNumber;
    @NotBlank(message = "password khong dc de trong")
    private String password;
}
