package org.univartois.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.univartois.entity.id.HomeRoleId;
import org.univartois.enums.HomeRoleType;

import java.util.Objects;


@AllArgsConstructor
@Builder
@Getter @Setter
@Entity
@Table(name = "permissions")
public class HomeRoleEntity {
    @EmbeddedId
    @Builder.Default
    private HomeRoleId id = new HomeRoleId();

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @MapsId("homeId")
    @ManyToOne(fetch = FetchType.LAZY)
    private HomeEntity home;

    @Enumerated(EnumType.STRING)
    private HomeRoleType role;

    public HomeRoleEntity() {
        this.id = new HomeRoleId();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        HomeRoleEntity that = (HomeRoleEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
