package hu.schonherz.javatraining.issuetracker.web.view.chart;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@ManagedBean(name = "ticketsByUserView")
@ViewScoped
@Data
@Log4j
public class TicketsByUserView implements Serializable {
	
	@EJB
	private TicketServiceRemote ticketService;
	
	@EJB
	private UserServiceRemote userService;
	
	@EJB
	private CompanyServiceRemote companyService;
	
	private List<CompanyVo> allCompany;
	private Long companyId;
	
	private Map<String, Number> map;
	
	@PostConstruct
	public void init(){
		allCompany = companyService.findAll();
	}
	
	public void renderChart() {
		log.debug("TicketsByUserReport renderChart()");
		CompanyVo company = companyService.findById(companyId);
		List<UserVo> userVo = userService.findAllByCompany(company);
		map = new HashMap<>();
		
		for(UserVo u : userVo) {
			List<TicketVo> t = new ArrayList<TicketVo>();
			t = ticketService.findByUser(u);
			map.put(u.getUsername(), t.size()); 
		}

	}
}
