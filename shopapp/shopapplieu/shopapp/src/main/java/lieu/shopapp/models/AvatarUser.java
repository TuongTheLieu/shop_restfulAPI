package lieu.shopapp.models;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "avatar_user")
public class AvatarUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
