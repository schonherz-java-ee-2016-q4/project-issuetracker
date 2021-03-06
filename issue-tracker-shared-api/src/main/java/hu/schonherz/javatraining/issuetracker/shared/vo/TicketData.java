package hu.schonherz.javatraining.issuetracker.shared.vo;

import java.io.Serializable;

public class TicketData implements Serializable {
	private static final long serialVersionUID = -2511057542747405123L;

	private String ticketName;
	private String ticketDescription;
	private String companyName;
	private String bindedUser;
	private String recUser;
	private String ticketTypeName;
	private String clientMail;

	public String getTicketName() {
		return ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	public String getTicketDescription() {
		return ticketDescription;
	}

	public void setTicketDescription(String ticketDescription) {
		this.ticketDescription = ticketDescription;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBindedUser() {
		return bindedUser;
	}

	public void setBindedUser(String bindedUser) {
		this.bindedUser = bindedUser;
	}

	public String getRecUser() {
		return recUser;
	}

	public void setRecUser(String recUser) {
		this.recUser = recUser;
	}

	public String getTicketTypeName() {
		return ticketTypeName;
	}

	public void setTicketTypeName(String ticketTypeName) {
		this.ticketTypeName = ticketTypeName;
	}

	public String getClientMail() {
		return clientMail;
	}

	public void setClientMail(String clientMail) {
		this.clientMail = clientMail;
	}

	@Override
	public String toString() {
		return String.format(
				"TicketData [ticketName=%s, ticketDescription=%s, companyName=%s, bindedUser=%s, recUser=%s, ticketTypeName=%s, clientMail=%s]",
				ticketName, ticketDescription, companyName, bindedUser, recUser, ticketTypeName, clientMail);
	}

}
