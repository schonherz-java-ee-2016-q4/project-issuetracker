package hu.schonherz.javatraining.issuetracker.web.view.chart;

import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import lombok.Data;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ManagedBean(name = "typeSelectorView")
@ViewScoped
@Data
public class TypeSelectorView {

    @EJB
    private TypeServiceRemote typeService;

    private List<TypeVo> allTypes;
    private Long typeId;

    @PostConstruct
    public void init() {
        allTypes = typeService.findAll();
    }


}
