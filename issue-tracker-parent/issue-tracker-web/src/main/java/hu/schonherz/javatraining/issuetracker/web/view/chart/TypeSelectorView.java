package hu.schonherz.javatraining.issuetracker.web.view.chart;

import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "typeSelectorView")
@ViewScoped
@Data
@Log4j
public class TypeSelectorView implements Serializable {

    @EJB
    private TypeServiceRemote typeService;

    private List<TypeVo> allTypes;
    private Long typeId;

    @PostConstruct
    public void init() {
        allTypes = typeService.findAll();
        log.debug(typeId);
    }


}
