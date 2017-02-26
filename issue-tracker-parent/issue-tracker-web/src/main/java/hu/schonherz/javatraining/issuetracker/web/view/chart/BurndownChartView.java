package hu.schonherz.javatraining.issuetracker.web.view.chart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.web.beans.UserSessionBean;
import lombok.Data;
import lombok.extern.log4j.Log4j;

@ManagedBean(name = "burndownChartView")
@ViewScoped
@Data
@Log4j
public class BurndownChartView {

	private final long dayInLong = 1000 * 60 * 60 * 24;
	private final int daysInWeek = 7;
	private final int dateCutStartIndex = 4;
	private final int dateCutEndIndex = 10;
	
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
    private int yAxisValue = 0;

    @PostConstruct
    public void init() {
        companyId = userSessionBean.getCurrentUser().getCompany().getId();
        createLineModel();
    }

    public void createLineModel() {
        lineChart = initLinearModel();
        lineChart.setLegendPosition("e");
        lineChart.getAxes().put(AxisType.X, new CategoryAxis(bundle.getString("date")));
        lineChart.setTitle(bundle.getString("burndownchart"));
        Axis yAxis = lineChart.getAxis(AxisType.Y);
        yAxis.setLabel(bundle.getString("opentickets"));
        yAxis.setMin(0);
        yAxis.setMax(yAxisValue);
        yAxis.setTickInterval("1");
    }

    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();
        Date today = new Date();
        List<TicketVo> openTicketList = new ArrayList<>();
        int openTickets;
        List<TicketVo> ticketList;
        ChartSeries tickets = new ChartSeries();
        tickets.setLabel(bundle.getString("tickets"));

        for (int i = daysInWeek - 1; i >= 0; i--) {
            Date step = new Date(today.getTime() - i * dayInLong);
            ticketList = ticketService.getTicketsByCompanyAndTime(companyService.findById(companyId), step);
            if (ticketList.isEmpty()) {
                tickets.set(String.valueOf(step).substring(dateCutStartIndex, dateCutEndIndex), 0);
            } else {
                for (TicketVo ticket : ticketList) {
                    if (!ticket.getCurrentStatus().getIsEndStatus()) {
                        openTicketList.add(ticket);
                    }
                }
                openTickets = openTicketList.size();
                tickets.set(String.valueOf(step).substring(dateCutStartIndex, dateCutEndIndex), openTickets);
                if (openTickets > yAxisValue) {
                    yAxisValue = openTickets;
                }
                openTicketList.clear();
            }
        }

        yAxisValue += 1;
        model.addSeries(tickets);
        return model;
    }
}
