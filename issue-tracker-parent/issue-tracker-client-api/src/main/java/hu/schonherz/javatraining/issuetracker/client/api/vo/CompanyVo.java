package hu.schonherz.javatraining.issuetracker.client.api.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private long recUserId;
    private long modUserId;
    private String name;
}
