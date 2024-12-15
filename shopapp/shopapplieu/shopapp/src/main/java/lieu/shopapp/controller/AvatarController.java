package lieu.shopapp.controller;

import lieu.shopapp.dtos.response.AvatarUrl;
import lieu.shopapp.exceptions.DataNotFoundException;
import lieu.shopapp.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/avatar")
public class AvatarController {

    @Autowired
    private CloudinaryService cloudinaryService;

    // API tải lên ảnh đại diện
    @PostMapping("/{userId}")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable long userId) {
        try {
            byte[] imageBytes = file.getBytes();
            String imageUrl = cloudinaryService.uploadAvatar(imageBytes);
            cloudinaryService.saveAvatarUrl(imageUrl,userId);
            return ResponseEntity.ok().body("upload anh thanh cong"); // Trả về URL ảnh sau khi tải lên
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi khi tải lên ảnh: " + e.getMessage());
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("{userId}")
    private ResponseEntity<AvatarUrl> getAvatar(@PathVariable long userId) throws DataNotFoundException {
        AvatarUrl avatarUrl = cloudinaryService.getAvatar(userId);
        return ResponseEntity.ok().body(avatarUrl);
    }

    // API xóa ảnh đại diện
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAvatar(@RequestParam("public_id") String publicId) {
        try {
            boolean isDeleted = cloudinaryService.deleteAvatar(publicId);
            if (isDeleted) {
                return ResponseEntity.ok("Ảnh đã được xóa thành công.");
            } else {
                return ResponseEntity.status(500).body("Không thể xóa ảnh.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Lỗi khi xóa ảnh: " + e.getMessage());
        }
    }
}
