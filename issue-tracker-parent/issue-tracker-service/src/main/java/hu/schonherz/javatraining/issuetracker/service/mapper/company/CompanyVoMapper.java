package hu.schonherz.javatraining.issuetracker.service.mapper.company;



import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

public class CompanyVoMapper {
    static Mapper mapper = new DozerBeanMapper();

    public static CompanyVo toVo(CompanyEntity company) {
        if (company == null) {
            return null;
        }
        return mapper.map(company, CompanyVo.class);
    }

    public static List<CompanyVo> toVo(List<CompanyEntity> companies) {
        List<CompanyVo> rv = new ArrayList<>();
        for (CompanyEntity company: companies) {
            rv.add(toVo(company));
        }
        return rv;
    }

    public static CompanyEntity toEntity(CompanyVo companyVo) {
        if (companyVo == null) {
            return null;
        }
        return mapper.map(companyVo, CompanyEntity.class);
    }

    public static List<CompanyEntity> toEntity(List<CompanyVo> companies) {
        List<CompanyEntity> rv = new ArrayList<>();
        for (CompanyVo company : companies) {
            rv.add(toEntity(company));
        }
        return rv;
    }
}
