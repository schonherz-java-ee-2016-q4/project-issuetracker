package hu.schonherz.javatraining.issuetracker.web.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lombok.Data;

@ManagedBean(name = "addStatusView")
@ViewScoped
@Data
public class AddStatusView implements Serializable {
	private String name;
	private String description;
}
