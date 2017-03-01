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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

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
        List<TicketVo> openTicketList = new ArrayList<>();
        int openTickets = 0;
        List<TicketVo> ticketList;
        ChartSeries tickets = new ChartSeries();
        tickets.setLabel(bundle.getString("tickets"));

        Long today = new Date().getTime();
        Date todayMidnight = new Date(today - today % dayInLong);
        Date oneWeekBeforeMidnight = new Date(today - today % dayInLong - (daysInWeek - 1) * dayInLong);

        ticketList = ticketService.getTicketsByCompanyAndBetweenTime(companyService.findById(companyId),
                oneWeekBeforeMidnight, todayMidnight);

        for (int i = daysInWeek - 1; i >= 0; i--) {
            Long time = new Date().getTime();
            Date stepStart = new Date(time - time % dayInLong - i * dayInLong);
            Date stepEnd = new Date(time - time % dayInLong + dayInLong - i * dayInLong);

            for (TicketVo ticket : ticketList) {
                if (ticket.getRecDate().after(stepStart) && ticket.getRecDate().before(stepEnd)) {
                    openTicketList.add(ticket);
                }
            }
            if (openTicketList.size() != 0) {
                openTickets = openTicketList.size();
                tickets.set(String.valueOf(stepStart).substring(dateCutStartIndex, dateCutEndIndex), openTickets);
            } else {
                tickets.set(String.valueOf(stepStart).substring(dateCutStartIndex, dateCutEndIndex), 0);
            }
            if (openTickets > yAxisValue) {
                yAxisValue = openTickets;
            }
            openTicketList.clear();
        }
        yAxisValue += 1;
        model.addSeries(tickets);
        return model;
    }
}
