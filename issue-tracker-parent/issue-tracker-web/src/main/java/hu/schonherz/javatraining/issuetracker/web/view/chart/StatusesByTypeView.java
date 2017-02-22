package hu.schonherz.javatraining.issuetracker.web.view.chart;

import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.web.view.tickettype.TypeCreateModifyView;
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

    @ManagedProperty("#{mes}")
    private ResourceBundle bundle;

    @ManagedProperty(value = "#{typeSelectorView}")
    private TypeSelectorView typeSelectorView;

    private PieChartModel chart;

    @PostConstruct
    public void init() {
        chart = new PieChartModel();
        TypeVo typeVo = typeService.findById(typeSelectorView.getTypeId());
        List<StatusVo> statuses = typeService.getStatuses(typeVo);

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
}
