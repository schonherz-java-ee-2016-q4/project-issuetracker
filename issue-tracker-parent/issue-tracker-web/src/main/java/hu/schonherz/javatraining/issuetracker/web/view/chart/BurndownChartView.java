package hu.schonherz.javatraining.issuetracker.web.view.chart;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
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
import java.util.*;

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

    @ManagedProperty("#{mes}")
    private ResourceBundle bundle;

    private LineChartModel lineChart;
    private Long companyId;
    int yAxisValue = 0;

    @PostConstruct
    public void init() {
        companyId = userSessionBean.getCurrentUser().getCompany().getId();
        createLineModel();
    }

    public void createLineModel() {
        lineChart = initLinearModel();
        lineChart.setLegendPosition("e");
        lineChart.getAxes().put(AxisType.X, new CategoryAxis("Date"));
        lineChart.setTitle(bundle.getString("burndownchart"));
        Axis yAxis = lineChart.getAxis(AxisType.Y);
        yAxis.setLabel("Tickets");
        yAxis.setMin(0);
        yAxis.setMax(yAxisValue);
        yAxis.setTickInterval("1");
    }

    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();
        Date today = new Date();
        Map<Object, Number> chartData = new HashMap<>();
        List<TicketVo> openTicketList = new ArrayList<>();
        int openTickets;
        List<TicketVo> ticketList;
        ChartSeries tickets = new ChartSeries();
        tickets.setLabel("Tickets");

        for (int i = 6; i >= 0; i--) {
            Date step = new Date(today.getTime() - i * (1000 * 60 * 60 * 24));
            ticketList = ticketService.getTicketsByCompanyAndTime(companyService.findById(companyId), step);
            if (ticketList.isEmpty()) {
                tickets.set(String.valueOf(step).substring(4, 10), 0);
            } else {

                for (TicketVo ticket : ticketList) {
                    if (!ticket.getCurrentStatus().getIsEndStatus()) {
                        openTicketList.add(ticket);
                        openTickets = openTicketList.size();
                        tickets.set(String.valueOf(step).substring(4, 10), openTickets);
                        if (openTickets > yAxisValue) {
                            yAxisValue = openTickets;
                        }
                        openTicketList.clear();
                    }
                }
            }
        }

        log.debug("openticketlist:" + openTicketList);
        log.debug("chartdata" + chartData);
        model.addSeries(tickets);
        return model;
    }
}
