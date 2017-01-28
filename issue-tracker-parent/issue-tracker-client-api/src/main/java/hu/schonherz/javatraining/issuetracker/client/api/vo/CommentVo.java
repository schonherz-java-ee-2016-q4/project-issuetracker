package hu.schonherz.javatraining.issuetracker.client.api.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long recUserId;
    private Long modUserId;
    private Date recDate;
    private Date modDate;
    private String commentText;
}
