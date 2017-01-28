package hu.schonherz.javatraining.issuetracker.core.entities;


import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity implements Serializable {

    private Integer recUserId;
    private Date recDate;
    private Integer modUserId;
    private Date modDate;
    private String commentText;

    public CommentEntity() {}
}
