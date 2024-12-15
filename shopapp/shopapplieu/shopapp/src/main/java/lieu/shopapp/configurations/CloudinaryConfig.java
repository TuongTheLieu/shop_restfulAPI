package lieu.shopapp.configurations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dyacfxvff",
                "api_key", "712832581272714",
                "api_secret", "qkeD65ZF1tCze7NvTrtVvIJhErc"
        ));
    }
}
