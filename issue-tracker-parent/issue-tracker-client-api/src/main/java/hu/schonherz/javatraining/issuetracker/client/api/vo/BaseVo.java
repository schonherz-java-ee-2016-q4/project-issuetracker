package hu.schonherz.javatraining.issuetracker.client.api.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class BaseVo implements Serializable {
	
	private static final long serialVersionUID = -6214960303946037900L;
	
	private Long id;
	private String recUserName;
    private String modUserName;
    private Date recDate;
    private Date modDate;
}
