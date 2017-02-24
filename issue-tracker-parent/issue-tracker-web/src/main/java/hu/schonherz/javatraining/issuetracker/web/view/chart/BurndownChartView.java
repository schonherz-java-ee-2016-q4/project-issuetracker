package hu.schonherz.javatraining.issuetracker.web.view.chart;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.Date;
import java.util.List;

@ManagedBean(name = "burndownChartView")
@ViewScoped
@Data
@Log4j
public class BurndownChartView {

    @EJB
    private TicketServiceRemote ticketService;

    @EJB
    private CompanyServiceRemote companyService;

    @ManagedProperty("#{userSessionBean}")
    private UserSessionBean userSessionBean;

    private LineChartModel lineChart;
    private List<TicketVo> ticketList;
    private List<TicketVo> openTicketList;
    private List<CompanyVo> allCompanyList;
    private CompanyVo selectedCompany;
    private Long companyId;

    @PostConstruct
    public void init () {
        allCompanyList = companyService.findAll();
    }

    private void createLineModel() {
        lineChart = initLinearModel();
        lineChart.setTitle("Category Chart");
        lineChart.setLegendPosition("e");
        lineChart.setShowPointLabels(true);
        lineChart.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        Axis yAxis = lineChart.getAxis(AxisType.Y);
        yAxis.setLabel("Tickets");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }

    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();
        Date today = new Date();
        ticketList = ticketService.getTicketsByCompanyAndTime(companyService.findById(companyId), today);

        for(TicketVo ticket : ticketList) {
            if(!ticket.getCurrentStatus().getIsEndStatus()) {
                openTicketList.add(ticket);
            }
        }
        int openTickets = openTicketList.size();

        ChartSeries tickets = new ChartSeries();
        tickets.setLabel("Tickets");

        tickets.set(today, openTickets);
        tickets.set("2005", 100);
        tickets.set("2006", 44);
        tickets.set("2007", 150);
        tickets.set("2008", 25);

        model.addSeries(tickets);

        return model;
    }
}
