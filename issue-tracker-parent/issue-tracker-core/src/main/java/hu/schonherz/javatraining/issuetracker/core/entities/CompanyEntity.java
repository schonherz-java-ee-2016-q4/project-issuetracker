package hu.schonherz.javatraining.issuetracker.core.entities;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "company")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    private String name;


    @PrePersist
    public void prePersist() {
        recDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        modDate = new Date();
    }
}
