package hu.schonherz.javatraining.issuetracker.client.api.vo;

import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String commentText;
}
