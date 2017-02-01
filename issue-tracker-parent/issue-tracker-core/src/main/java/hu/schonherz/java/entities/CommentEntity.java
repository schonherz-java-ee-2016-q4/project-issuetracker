package hu.schonherz.javatraining.issuetracker.core.entities;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "comment")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String commentText;

}
