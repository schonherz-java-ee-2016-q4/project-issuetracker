package hu.schonherz.javatraining.issuetracker.web.view.chart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@ManagedBean(name = "ticketsByStatusView")
@ViewScoped
@Data
@Log4j
public class TicketsByStatusView implements Serializable {

	private PieChartModel chart;

	@EJB
	private TicketServiceRemote ticketService;
	
	@ManagedProperty("#{userSessionBean}")
	private UserSessionBean userSessionBean;
	
	@ManagedProperty("#{mes}")
	private ResourceBundle bundle;
	
	@PostConstruct
	public void init() {
		chart = new PieChartModel();

		List<TicketVo> tickets = ticketService.findByUser(userSessionBean.getCurrentUser());
		
		Map<String, Number> map = new HashMap<>();
		
		for (TicketVo ticket : tickets) {
			String status = ticket.getCurrentStatus().getName().toLowerCase();
			Number value = map.get(status);
			if (value == null) {
				map.put(status, 0);
			}
			else {
				value = value.longValue() + 1;
				map.put(status, value);
			}
		}
		chart.setData(map);
		
		chart.setTitle(bundle.getString("home_ticketsbystatus_chart_title"));
		chart.setLegendPosition("w");
		
		log.debug("chart data:");
		for (String key : chart.getData().keySet()) {
			log.debug(String.format("%s - %s", key, chart.getData().get(key)));
		}
	}
}
