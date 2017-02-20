package hu.schonherz.javatraining.issuetracker.web.view.tickettype;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;

@ManagedBean(name = "modifyStatusView")
@ViewScoped
public class ModifyStatusView implements Serializable {
	
	private StatusVo selectedStatus;
	private String newName;
	private String newDescription;
	
	public StatusVo getSelectedStatus() {
		return selectedStatus;
	}

	public void setSelectedStatus(StatusVo selectedStatus) {
		this.selectedStatus = selectedStatus;
		this.newName = selectedStatus == null ? "" : selectedStatus.getName();
		this.newDescription = selectedStatus == null ? "" :selectedStatus.getDescription();
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getNewDescription() {
		return newDescription;
	}

	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}
}
