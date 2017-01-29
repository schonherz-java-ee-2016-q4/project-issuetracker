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

    private Integer recUserId;
    private Integer modUserId;
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date recDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modDate;

    @PrePersist
    public void prePersist() {
        recDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        modDate = new Date();
    }
}
