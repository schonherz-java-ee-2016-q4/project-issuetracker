package hu.schonherz.javatraining.issuetracker.service.test;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;

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
                TypeVo typeVo = typeService.findByNameAndCompany("testType", companyVo);

                UserVo userVO = new UserVo();
                userVO.setUsername("TestUser");

                TicketVo ticketVo = new TicketVo();
                ticketVo.setUid("TestUid");
                ticketVo.setTitle("TestTitle");
                ticketVo.setDescription("TestDescription");
                ticketVo.setClientMail("TestClientMail");
                ticketVo.setType(typeVo);
                ticketVo.setCurrentStatus(typeVo.getStartEntity());
                ticketVo.setUser(userVO);

                serviceLocal.save(ticketVo, "TestUser");

            } catch (Exception e) {
                log.error("Error in save", e);
            }
            return null;
        });
    }

    @Test
    public void test2FindByUid() throws Exception {
        transactionalCaller.call(() -> {
            try {
                TicketVo voByUid = serviceLocal.findByUid("TestUid");
                Assert.assertEquals("TestUid", voByUid.getUid());
            } catch (Exception e) {
                log.error("Error in findByUid", e);
            }
            return null;
        });
    }

    @Test
    public void test3FindById() throws Exception {
        transactionalCaller.call(() -> {
            try {
                TicketVo vo = serviceLocal.findByUid("TestUid");
                TicketVo voById = serviceLocal.findById(vo.getId());
                Assert.assertEquals("TestUid", voById.getUid());
            } catch (Exception e) {
                log.error("Error in findById", e);
            }
            return null;
        });
    }

    @Test
    public void test4FindByUser() throws Exception {
        transactionalCaller.call(() -> {
            try {
                UserVo user = new UserVo();
                user.setUsername("TestUser");

                List<TicketVo> tickets = serviceLocal.findByUser(user);
                for (TicketVo ticket : tickets) {
                    Assert.assertEquals(user, ticket.getUser());
                }

            } catch (Exception e) {
                log.error("Error in findByUser", e);
            }
            return null;
        });
    }

    @Test
    public void test5Update() throws Exception {
        transactionalCaller.call(() -> {
            try {
                TicketVo vo = serviceLocal.findByUid("TestUid");
                vo.setTitle("TestTitle_updated");
                serviceLocal.update(vo, "modUser");
            } catch (Exception e) {
                log.error("Error during executing update on TicketVo",e);
            }
            return null;
        });
    }
}
