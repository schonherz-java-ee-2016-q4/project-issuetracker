package hu.schonherz.javatraining.issuetracker.client.api.service.company;


import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;

public interface CompanyService {

    CompanyVo findById(Long id);
    CompanyVo findByName(String name);
    CompanyVo save(CompanyVo company);
    CompanyVo update(CompanyVo company);
}