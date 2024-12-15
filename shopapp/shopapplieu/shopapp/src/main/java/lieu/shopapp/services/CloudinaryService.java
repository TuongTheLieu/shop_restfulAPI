package lieu.shopapp.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lieu.shopapp.dtos.response.AvatarUrl;
import lieu.shopapp.exceptions.DataNotFoundException;
import lieu.shopapp.models.AvatarUser;
import lieu.shopapp.models.User;
import lieu.shopapp.repositories.AvatarUserRepository;
import lieu.shopapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AvatarUserRepository avatarUserRepository;

    // Tải lên ảnh
    public String uploadAvatar(byte[] imageBytes) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap());
        return (String) uploadResult.get("url"); // Trả về URL ảnh
    }

    // Xóa ảnh theo public_id
    public boolean deleteAvatar(String publicId) throws IOException {
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return "ok".equals(result.get("result")); // Trả về true nếu xóa thành công
    }
    public void saveAvatarUrl(String avatarUrl, long userId) throws IOException, DataNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()-> new DataNotFoundException("khong tim thay nguoi dung voi id "+userId));
        AvatarUser avatarUser = new AvatarUser();
        avatarUser.setAvatarUrl(avatarUrl);
        avatarUser.setUser(user);
        avatarUserRepository.save(avatarUser);
    }
    public AvatarUrl getAvatar(long userId) throws DataNotFoundException {
        AvatarUser avatarUser = avatarUserRepository.findById(userId)
                .orElseThrow(()-> new DataNotFoundException("khong tim thay user"));
        String url = avatarUser.getAvatarUrl();
        return new AvatarUrl(url);
    }
}
