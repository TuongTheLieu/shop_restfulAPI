package lieu.shopapp.services;

import lieu.shopapp.dtos.UserDTO;
import lieu.shopapp.exceptions.DataNotFoundException;
import lieu.shopapp.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password) throws Exception;
    User getUser(long id) throws DataNotFoundException;
}
