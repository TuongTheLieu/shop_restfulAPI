package lieu.shopapp.controller;


import jakarta.validation.Valid;
import lieu.shopapp.dtos.UserDTO;
import lieu.shopapp.dtos.UserLoginDTO;
import lieu.shopapp.dtos.response.ResponseUser;
import lieu.shopapp.models.User;
import lieu.shopapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${api.prefix}/users")

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser (@Valid @RequestBody UserDTO userDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("password khong giong nhau");
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login (@Valid @RequestBody UserLoginDTO userLoginDTO) {
       try {
           String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
           return ResponseEntity.ok(token);
       } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUser> getUser (@PathVariable Long id) {
        try {
            User user = userService.getUser(id);
            ResponseUser responseUser = new ResponseUser();
            responseUser.setFullName(user.getFullName());
            responseUser.setPhoneNumber(user.getPhoneNumber());
            responseUser.setAddress(user.getAddress());
            responseUser.setDateOfBirth(user.getDateOfBirth());
            return ResponseEntity.ok(responseUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
