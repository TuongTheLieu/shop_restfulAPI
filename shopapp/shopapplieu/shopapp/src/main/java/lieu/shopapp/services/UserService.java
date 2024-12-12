package lieu.shopapp.services;
import lieu.shopapp.components.JwtTokenUtil;
import lieu.shopapp.dtos.UserDTO;
import lieu.shopapp.exceptions.DataNotFoundException;
import lieu.shopapp.exceptions.PermisstionDenyException;
import lieu.shopapp.models.Role;
import lieu.shopapp.models.User;
import lieu.shopapp.repositories.RoleRepository;
import lieu.shopapp.repositories.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public UserService(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phone = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phone)) {
            throw new DataIntegrityViolationException("sdt da ton tai");
        }
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(
                ()-> new DataNotFoundException("Role not found"));
        if (role.getName().toUpperCase().equals("ADMIN")) {
            throw new PermisstionDenyException("không thể tạo 1 account admin");
        }
        // convert from UserDTO -> user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        newUser.setRole(role);
        // kiểm tra nếu có accoutId thì không yêu cầu password
        if (userDTO.getFacebookAccountId()==0 & userDTO.getGoogleAccountId()==0) {
            String password = userDTO.getPassword();
            String encoderPassword = passwordEncoder.encode(password);
            newUser.setPassword(encoderPassword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("phone number hoac password khong dung");
        }
        User existingUser = optionalUser.get();
        if (existingUser.getFacebookAccountId()==0 & existingUser.getGoogleAccountId()==0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password,existingUser.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public User getUser(long id) throws DataNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("khong tim thay user voi id "+id));
        return user;
    }

}
