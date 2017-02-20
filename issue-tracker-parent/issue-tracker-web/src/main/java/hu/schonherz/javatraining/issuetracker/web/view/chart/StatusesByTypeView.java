package hu.schonherz.javatraining.issuetracker.web.view.chart;

import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.web.view.tickettype.TypeCreateModifyView;
import lombok.Data;
import org.primefaces.model.chart.PieChartModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@ManagedBean(name = "statusesByTypeView")
@ViewScoped
@Data
public class StatusesByTypeView {

    @EJB
    private TypeServiceRemote typeService;

    @EJB
    private StatusOrderServiceRemote statusOrderService;

    @EJB
    private StatusServiceRemote statusService;

    @ManagedProperty("#{mes}")
    private ResourceBundle bundle;

    @ManagedProperty(value = "#{typeCreateModifyView}")
    private TypeCreateModifyView typeCreateModifyView;

    @ManagedProperty(value = "#{typeSelectorView}")
    private TypeSelectorView typeSelectorView;

    private PieChartModel chart;


    private StatusVo startStatus;
    private List<StatusVo> statuses;

    @PostConstruct
    public void init() {
        chart = new PieChartModel();
        startStatus = typeService.findById(typeSelectorView.getTypeId()).getStartEntity();
        getStatusesFrom(startStatus);

        Map<String, Number> chartData = new HashMap<>();

        for (StatusVo status : statuses) {
            String statusName = status.getName().toLowerCase();
            Number value = chartData.get(statusName);
            if (value == null) {
                chartData.put(statusName, 0);
            }
            else {
                value = value.longValue() + 1;
                chartData.put(statusName, value);
            }
        }

        chart.setData(chartData);
        chart.setTitle(bundle.getString("typesbystatus_chart_title"));
        chart.setLegendPosition("w");
    }

    private void getStatusesFrom(StatusVo status) {
        List<StatusOrderVo> fromStatuses = statusOrderService.findByFromStatusId(status.getId());
        boolean isNew;

        for (StatusOrderVo statusOrder : fromStatuses) {
            isNew = true;

            //check if already in our scope
            for (StatusVo statusInStatuses : statuses) {
                if (statusInStatuses.getId() == statusOrder.getToStatusId()) {
                    isNew = false;
                    break;
                }
            }

            if (isNew) {
                StatusVo newStatus = statusService.findById(statusOrder.getToStatusId());
                statuses.add(newStatus);
                getStatusesFrom(newStatus);
            }
        }

    }
}
