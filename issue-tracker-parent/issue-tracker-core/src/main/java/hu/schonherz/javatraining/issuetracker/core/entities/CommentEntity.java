package hu.schonherz.javatraining.issuetracker.core.entities;


import lombok.Data;
import org.springframework.data.annotation.Version;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "comment")
public @Data class CommentEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer recUserId;
    private Integer modUserId;
    private Date recDate;
    @Version private Date modDate;
    private String commentText;

    public CommentEntity() {
        super();
        recDate = new Date();
    }
}
