package hu.schonherz.javatraining.issuetracker.web.view.chart;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.primefaces.model.chart.PieChartModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@ManagedBean(name = "statusesByTypeView")
@ViewScoped
@Data
@Log4j
public class StatusesByTypeView implements Serializable {

    @EJB
    private TypeServiceRemote typeService;

    @EJB
    private TicketServiceRemote ticketService;

    @ManagedProperty("#{mes}")
    private ResourceBundle bundle;

    private PieChartModel chart;
    private List<TypeVo> allTypes;
    private Long typeId;

    @PostConstruct
    public void init() {
        allTypes = typeService.findAll();
    }

    public void renderChart() {
        chart = new PieChartModel();
        TypeVo typeVo = typeService.findById(typeId);
        List<StatusVo> statuses = typeService.getStatuses(typeVo);

        Map<String, Number> chartData = new HashMap<>();

        for (StatusVo status : statuses) {
            String statusName = status.getName().toLowerCase();
            chartData.put(statusName, ticketService.getNumberOfTicketsByTypeAndStatus(typeVo, status));

        }
        chart.setData(chartData);
        chart.setLegendPosition("w");
        log.debug("chart created:" + chart.getData());
    }
}
