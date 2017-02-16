package hu.schonherz.javatraining.issuetracker.client.api.service.company;


import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;

import java.util.List;

public interface CompanyService {

    CompanyVo findById(Long id);
    CompanyVo findByName(String name);
    List<CompanyVo> findAll();
    CompanyVo save(CompanyVo company, String username);
    CompanyVo update(CompanyVo company, String username);
}
