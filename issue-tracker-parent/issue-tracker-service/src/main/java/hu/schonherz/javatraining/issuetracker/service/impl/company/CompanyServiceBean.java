package hu.schonherz.javatraining.issuetracker.service.impl.company;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.core.dao.CompanyDao;
import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;
import hu.schonherz.javatraining.issuetracker.service.mapper.company.CompanyVoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.ejb.*;
import javax.interceptor.Interceptors;

@Stateless(mappedName = "CompanyService")
@Local(CompanyServiceLocal.class)
@Remote(CompanyServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CompanyServiceBean implements CompanyServiceLocal, CompanyServiceRemote {

    @Autowired
    private CompanyDao companyDao;

    @Override
    public CompanyVo findById(Long id) {
        CompanyEntity company = companyDao.findById(id);
        return CompanyVoMapper.toVo(company);
    }

    @Override
    public CompanyVo save(CompanyVo company) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        company.setRecUserId(user.getId());

        return CompanyVoMapper.toVo(companyDao.save(CompanyVoMapper.toEntity(company)));
    }

    @Override
    public CompanyVo update(CompanyVo company) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = (UserEntity) auth.getPrincipal();
        company.setModUserId(user.getId());

        return CompanyVoMapper.toVo(companyDao.save(CompanyVoMapper.toEntity(company)));
    }

    @Override
    public CompanyVo findByName(String name) {
        CompanyEntity company = companyDao.findByName(name);
        return CompanyVoMapper.toVo(company);
    }
}
