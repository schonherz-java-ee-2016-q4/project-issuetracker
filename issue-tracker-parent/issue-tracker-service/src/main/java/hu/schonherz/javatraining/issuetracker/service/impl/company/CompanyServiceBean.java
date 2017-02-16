package hu.schonherz.javatraining.issuetracker.service.impl.company;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.core.dao.CompanyDao;
import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;

import java.util.List;

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
        return GenericVoMappers.companyVoMapper.toVo(company);
    }

    @Override
    public CompanyVo findByName(String name) {
        CompanyEntity company = companyDao.findByName(name);
        return GenericVoMappers.companyVoMapper.toVo(company);
    }
    
    @Override
    public CompanyVo save(CompanyVo company, String username) {
        company.setRecUserName(username);
        return GenericVoMappers.companyVoMapper.toVo(companyDao.save(GenericVoMappers.companyVoMapper.toEntity(company)));
    }

    @Override
    public CompanyVo update(CompanyVo company, String username) {
        company.setModUserName(username);
        return GenericVoMappers.companyVoMapper.toVo(companyDao.save(GenericVoMappers.companyVoMapper.toEntity(company)));
    }

    @Override
    public List<CompanyVo> findAll(){
        return GenericVoMappers.companyVoMapper.toVo(companyDao.findAll());
    }
}
