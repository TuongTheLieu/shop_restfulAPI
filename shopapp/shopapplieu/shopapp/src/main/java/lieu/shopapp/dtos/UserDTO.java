package lieu.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("phone_number")
    @NotBlank(message = "sdt khong dc de trong")
    private String phoneNumber;
    @NotBlank(message = "password khong dc de trong")
    private String password;
    @JsonProperty("retype_password")
    private String retypePassword;
    private String address;
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;
    @JsonProperty("facebook_account_id")
    private int facebookAccountId;
    @JsonProperty("google+account_id")
    private int googleAccountId;
    @JsonProperty("role_id")
    @NotNull(message = "ID is required")
    private Long roleId;
}
