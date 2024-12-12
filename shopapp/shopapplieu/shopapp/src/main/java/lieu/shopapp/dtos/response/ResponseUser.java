package lieu.shopapp.dtos.response;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseUser {
    private String fullName;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
}
