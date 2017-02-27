package hu.schonherz.javatraining.issuetracker.service.test;

import java.util.ArrayList;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ManagedBean
public class TestTicketService {
    static final Logger log = LogManager.getLogger(TestTicketService.class.getName());

    @EJB
    TicketServiceLocal serviceLocal;

    @EJB
    private TestUserService.Caller transactionalCaller;
    
    @EJB
    TypeServiceLocal typeService;
    
    @EJB
    CompanyServiceLocal companyService;
    
    @EJB
    UserServiceLocal userService;
    
    @Inject
    TransactionManagement transactionManager;
    
    @Before
    public void startTheContainer() throws Exception {
        try {
            CreateContext.ejbContainer.getContext().bind("inject", this);
        } catch (Throwable e) {
            log.error("Error startContainer", e);
        }
    }

    @Test
    public void test1Save() throws Exception {
        transactionalCaller.call(() -> {
            try {
                CompanyVo companyVo = companyService.findByName("testCompany");
                TypeVo typeVo = typeService.findByNameAndCompany("testType_updated", companyVo);
                UserVo userVo = userService.findByUsername("test2");
                log.debug("MYN");
                log.debug(companyVo);
                log.debug(typeVo);

                TicketVo ticketVo = new TicketVo();
                ticketVo.setTitle("TestTitle");
                ticketVo.setDescription("TestDescription");
                ticketVo.setClientMail("TestClientMail");
                ticketVo.setType(typeVo);
                ticketVo.setCurrentStatus(typeVo.getStartEntity());
                ticketVo.setUser(userVo);
                ticketVo.setCompany(companyVo);
                ticketVo.setComments(new ArrayList<>());
                ticketVo.setHistory(new ArrayList<>());

                TicketVo save = serviceLocal.save(ticketVo, "TestUser");
                log.debug(String.format("saved ticket: %s, id: %s", save, save.getId()));
            } catch (Exception e) {
                log.error("Error in test1Save", e);
				Assert.fail();
            }
            return null;
        });
    }

}