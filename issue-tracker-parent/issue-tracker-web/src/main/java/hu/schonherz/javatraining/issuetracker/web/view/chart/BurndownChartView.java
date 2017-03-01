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

        ticketList = ticketService.getTicketsByCompanyAndBetweenTime(companyService.findById(companyId),
                getMidnightFromToday(-daysInWeek), getMidnightFromToday(0));

        for (int i = daysInWeek - 1; i >= 0; i--) {
            Date stepStart = getMidnightFromToday(-i);
            Date stepEnd = getMidnightFromToday(-i + 1);

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

    public Date getMidnightFromToday(int days) {
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }
}
