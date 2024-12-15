package lieu.shopapp.repositories;

import lieu.shopapp.models.AvatarUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarUserRepository extends JpaRepository<AvatarUser, Long> {
}
