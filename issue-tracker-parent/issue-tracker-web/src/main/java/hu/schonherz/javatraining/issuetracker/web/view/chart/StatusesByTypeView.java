package hu.schonherz.javatraining.issuetracker.web.view.chart;

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
            Number value = chartData.get(statusName);
            if (value == null) {
                chartData.put(statusName, 1);
            } else {
                value = value.longValue() + 1;
                chartData.put(statusName, value);
            }
        }

        chart.setData(chartData);
        chart.setTitle(bundle.getString("typesbystatus_chart_title"));
        chart.setLegendPosition("w");
        log.debug("chart created:" + chart.getData());
    }
}
