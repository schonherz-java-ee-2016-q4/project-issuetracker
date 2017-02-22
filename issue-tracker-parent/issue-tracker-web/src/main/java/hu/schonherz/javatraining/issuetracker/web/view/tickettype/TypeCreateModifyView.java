package hu.schonherz.javatraining.issuetracker.web.view.tickettype;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@ManagedBean(name = "typeCreateModifyView")
@ViewScoped
@Data
@Log4j
public class TypeCreateModifyView implements Serializable {

	@EJB
	private StatusServiceRemote statusService;

	@EJB
	private StatusOrderServiceRemote statusOrderService;

	@EJB
	private TypeServiceRemote typeService;

	@EJB
	private CompanyServiceRemote companyService;

	@EJB
	private UserServiceRemote userService;

	private TypeVo typevo;
	private List<StatusVo> statuses;

	@ManagedProperty("#{mes}")
	private ResourceBundle bundle;

	@ManagedProperty(value = "#{addStatusView}")
	private AddStatusView addStatusView;

	@ManagedProperty(value = "#{modifyStatusView}")
	private ModifyStatusView modifyStatusView;

	@ManagedProperty(value = "#{modifyStatusOrderView}")
	private ModifyStatusOrderView modifyStatusOrderView;

	@ManagedProperty("#{userSessionBean}")
	private UserSessionBean userSessionBean;

	@PostConstruct
	public void init() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String param = params.get("id");
		log.debug("passed paramter: " + param);

		if (param != null) {
			load(Long.parseLong(params.get("id")));
		} else {
			load(null);
		}
	}

	private void load(Long id) {
		typevo = new TypeVo();
		statuses = new ArrayList<>();
		List<StatusOrderViewModel> statusOrders = new ArrayList<>();

		if (id != null) {
			typevo = typeService.findById(id);
			StatusVo startVo = typevo.getStartEntity();
			statuses.add(startVo);
			statuses = typeService.getStatuses(typevo);
			
			List<StatusVo> noNullStatuses = new ArrayList<>();
			for (StatusVo status : statuses) {
				status.setIsEndStatus(status.getIsEndStatus() == null ? false : status.getIsEndStatus());
				noNullStatuses.add(status);
			}
			statuses = noNullStatuses;
			
			statusOrders = getStatusOrder();
		}

		modifyStatusOrderView.init();
		modifyStatusOrderView.generateDiagram(statuses, statusOrders);
		log.debug(String.format("loaded back: %s", id));
	}

	private List<StatusOrderViewModel> getStatusOrder() {
		List<StatusOrderViewModel> back = new ArrayList<>();

		for (StatusVo status : statuses) {
			List<StatusOrderVo> fromStatuses = statusOrderService.findByFromStatusId(status.getId());

			for (StatusOrderVo statusOrder : fromStatuses) {

				StatusVo to = statuses.stream().filter(x -> x.getId() == statusOrder.getToStatusId()).findFirst().get();

				StatusOrderViewModel newOrder = StatusOrderViewModel.builder().from(status.getName()).to(to.getName())
						.isOriginal(true).build();
				back.add(newOrder);
			}
		}

		return back;
	}

	public void save() {
		FacesContext context = FacesContext.getCurrentInstance();
		log.debug("mentes");

		if ("".equals(typevo.getName())) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_empty_name")));
			return;
		}
		if ("".equals(typevo.getDescription())) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_empty_desc")));
			return;
		}

		if (statuses.size() < 2) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_status_order_min")));
			return;
		}
		
		if (!isFullyConnected()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					bundle.getString("tickettype_status_order_noconnect")));
			return;
		}

		StatusVo startStatus = getStartStatus();
		if (startStatus == null) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					bundle.getString("tickettype_status_order_nostart")));
			return;
		}
		
		if (!statuses.stream().filter(x -> x.getIsEndStatus() && !x.equals(startStatus)).findFirst().isPresent()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					bundle.getString("tickettype_noend")));
			return;
		}

		if (typevo.getId() == null) {
			TypeVo typeWithSameName = typeService.findByNameAndCompany(typevo.getName(),
					userSessionBean.getCurrentUser().getCompany());
			if (typeWithSameName != null) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						bundle.getString("tickettype_alreadyinuser")));
				return;
			}
		}

		List<StatusVo> comittedStatuses = new ArrayList<>();
		for (StatusVo statusVo : statuses) {
			if (statusVo.getId() == null) {
				statusVo = statusService.save(statusVo, userSessionBean.getUserName());
			} else {
				statusVo = statusService.update(statusVo, userSessionBean.getUserName());
			}

			comittedStatuses.add(statusVo);
			log.debug("commited status: " + statusVo.getName() + " id: " + statusVo.getId());
		}

		for (StatusOrderViewModel model : modifyStatusOrderView.getOldStatusOrders()) {
			StatusVo fromStatusVo = comittedStatuses.stream().filter(x -> x.getName().equals(model.getFrom()))
					.findFirst().get();
			StatusVo toStatusVo = comittedStatuses.stream().filter(x -> x.getName().equals(model.getTo())).findFirst()
					.get();
			StatusOrderVo statusOrderVo = statusOrderService.findByFromStatusIdAndToStatusId(fromStatusVo.getId(),
					toStatusVo.getId());
			statusOrderService.deleteById(statusOrderVo.getId());
		}

		for (StatusOrderViewModel model : modifyStatusOrderView.getStatusOrders()) {
			StatusVo fromStatusVo = comittedStatuses.stream().filter(x -> x.getName().equals(model.getFrom()))
					.findFirst().get();
			StatusVo toStatusVo = comittedStatuses.stream().filter(x -> x.getName().equals(model.getTo())).findFirst()
					.get();

			if (typevo.getId() != null) {
				StatusOrderVo statusOrderVo = statusOrderService.findByFromStatusIdAndToStatusId(fromStatusVo.getId(),
						toStatusVo.getId());
				if (statusOrderVo != null) {
					statusOrderService.update(statusOrderVo, userSessionBean.getUserName());
					continue;
				}
			}

			StatusOrderVo newOrderVo = StatusOrderVo.builder().fromStatusId(fromStatusVo.getId())
					.toStatusId(toStatusVo.getId()).build();
			statusOrderService.save(newOrderVo, userSessionBean.getUserName());
		}

		StatusVo comittedStartStatus = comittedStatuses.stream().filter(x -> x.getName().equals(startStatus.getName()))
				.findFirst().get();
		typevo.setStartEntity(comittedStartStatus);
		typevo.setCompany(userSessionBean.getCurrentUser().getCompany());

		if (typevo.getId() == null) {
			typevo = typeService.save(typevo, userSessionBean.getUserName());
		} else {
			typevo = typeService.update(typevo, userSessionBean.getUserName());
		}

		logCurrentStatus();

		load(typevo.getId());
		context.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", bundle.getString("tickettype_succes")));
	}

	private boolean isFullyConnected() {
		List<String> statusesWithConnection = new ArrayList<>();

		for (StatusOrderViewModel statusOrder : modifyStatusOrderView.getStatusOrders()) {
			if (!statusesWithConnection.contains(statusOrder.getTo())) {
				statusesWithConnection.add(statusOrder.getTo());
			}
			if (!statusesWithConnection.contains(statusOrder.getFrom())) {
				statusesWithConnection.add(statusOrder.getFrom());
			}
		}

		return statusesWithConnection.size() == statuses.size();
	}

	private StatusVo getStartStatus() {
		List<StatusVo> startCandidates = new ArrayList<>();

		for (StatusVo statusVo : statuses) {
			boolean hasConnections = false;

			for (StatusOrderViewModel statusOrderViewModel : modifyStatusOrderView.getStatusOrders()) {
				if (statusVo.getName().equals(statusOrderViewModel.getTo())) {
					hasConnections = true;
					break;
				}
			}

			if (!hasConnections) {
				startCandidates.add(statusVo);
			}
		}

		if (startCandidates.size() == 1) {
			return startCandidates.get(0);
		}

		return null;
	}

	public void addNewStatus() {
		FacesContext context = FacesContext.getCurrentInstance();
		log.debug("status add");

		if ("".equals(addStatusView.getName()) || "".equals(addStatusView.getDescription())) {
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", bundle.getString("tickettype_status_add_empty")));
			return;
		}

		String newStatusName = addStatusView.getName();
		for (StatusVo statusVo : statuses) {
			if (statusVo.getName().equals(newStatusName)) {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
						bundle.getString("tickettype_status_add_alreadyinuser")));
				return;
			}
		}

		statuses.add(StatusVo.builder().name(addStatusView.getName()).description(addStatusView.getDescription())
				.isEndStatus(addStatusView.isEndStatus()).build());
		modifyStatusOrderView.addStatus(addStatusView.getName());

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
				newStatusName + " " + bundle.getString("tickettype_status_add_succes")));

		addStatusView.setName("");
		addStatusView.setDescription("");
		addStatusView.setEndStatus(false);
		logCurrentStatus();
	}

	public void modifyStatus() {
		FacesContext context = FacesContext.getCurrentInstance();
		log.debug("modify status");

		if ("".equals(modifyStatusView.getNewName()) || "".equals(modifyStatusView.getNewDescription())) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
					bundle.getString("tickettype_status_modify_empty")));
			return;
		}

		for (StatusVo statusVo : statuses) {
			if (statusVo.equals(modifyStatusView.getSelectedStatus())) {
				String modStatName = statusVo.getName();
				statusVo.setName(modifyStatusView.getNewName());
				statusVo.setDescription(modifyStatusView.getNewDescription());
				statusVo.setIsEndStatus(modifyStatusView.isEndStatus());
				modifyStatusOrderView.modifyStatus(modStatName, modifyStatusView.getNewName());

				modifyStatusView.setSelectedStatus(null);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						modStatName + " " + bundle.getString("tickettype_status_modify_succes")));
			}
		}
	}

	public void deleteStatus() {
		FacesContext context = FacesContext.getCurrentInstance();
		log.debug("delete status");

		String modStatName = modifyStatusView.getSelectedStatus().getName();
		statuses.remove(modifyStatusView.getSelectedStatus());
		modifyStatusOrderView.deleteStatus(modStatName);

		modifyStatusView.setSelectedStatus(null);
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
				modStatName + " " + bundle.getString("tickettype_status_modify_deleted")));
	}

	public void cancelModifyingStatus() {
		log.debug("cancel modifying status");

		modifyStatusView.setSelectedStatus(null);
	}

	private void logCurrentStatus() {
		log.debug("Name: " + typevo.getName());
		log.debug("Description:" + typevo.getDescription());
		log.debug("Statuses:");
		for (StatusVo statusVo : statuses) {
			log.debug(String.format("%s - %s - %s - is end: %s",
					statusVo.getId(), statusVo.getName(), statusVo.getDescription(), statusVo.getIsEndStatus()));
		}
		log.debug("Connections:");
		for (StatusOrderViewModel statusOrderViewModel : modifyStatusOrderView.getStatusOrders()) {
			log.debug(statusOrderViewModel.getFrom() + " - " + statusOrderViewModel.getTo());
		}
	}
}
