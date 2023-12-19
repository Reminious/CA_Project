package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "leave_remain", schema = "leavedb")
public class LeaveRemain {
    @ManyToOne
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private User user;

    @EmbeddedId
    private LeaveRemainId id;

    @Column(name = "AnnualRemain", columnDefinition = "int UNSIGNED not null")
    private Integer annualRemain;

    @Column(name = "SickRemain", columnDefinition = "int UNSIGNED not null")
    private Integer sickRemain;

    @Column(name = "CompensationRemain", columnDefinition = "int UNSIGNED not null")
    private Integer compensationRemain;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        LeaveRemain that = (LeaveRemain) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}