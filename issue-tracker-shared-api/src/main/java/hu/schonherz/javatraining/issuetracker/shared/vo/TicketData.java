package hu.schonherz.javatraining.issuetracker.shared.vo;

public class TicketData {
	private String ticketName;
	private String ticketDescription;
	private String companyName;
	private String bindedUser;
	private String recUser;
	private String ticketType;
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

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public String getClientMail() {
		return clientMail;
	}

	public void setClientMail(String clientMail) {
		this.clientMail = clientMail;
	}
	
}
