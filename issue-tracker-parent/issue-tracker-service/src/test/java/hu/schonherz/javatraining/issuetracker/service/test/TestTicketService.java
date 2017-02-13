package hu.schonherz.javatraining.issuetracker.service.test;

import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ManagedBean
public class TestTicketService {
    static final Logger log = LogManager.getLogger(TestTicketService.class.getName());

    @EJB
    TicketServiceLocal serviceLocal;

    @EJB
    private TestUserService.Caller transactionalCaller;

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
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

                CompanyVo companyVo = new CompanyVo();
                companyVo.setId(1L);
                companyVo.setRecUserName("test");
                companyVo.setModUserName("test");
                companyVo.setRecUserId(2L);
                companyVo.setModUserId(3L);
                companyVo.setRecDate(fmt.parse("2017-02-13"));
                companyVo.setModDate(fmt.parse("2017-02-13"));
                companyVo.setName("testCompany");

                StatusVo statusVo = new StatusVo();
                statusVo.setId(1L);
                statusVo.setRecUserName("test");
                statusVo.setModUserName("test");
                statusVo.setRecDate(fmt.parse("2017-02-13"));
                statusVo.setModDate(fmt.parse("2017-02-13"));
                statusVo.setName("test");
                statusVo.setDescription("test");

                TypeVo typeVo = new TypeVo();
                typeVo.setName("testStatus");
                typeVo.setDescription("testDescription");
                typeVo.setCompany(companyVo);
                typeVo.setStartEntity(statusVo);


                UserVo userVO = new UserVo();
                userVO.setUsername("TestUser");

                CommentVo commentVo = new CommentVo();
                commentVo.setCommentText("TestCommentText");
                List<CommentVo> commentVoList = new ArrayList<>();
                commentVoList.add(commentVo);

                TicketVo ticketVo = new TicketVo();
                ticketVo.setUid("TestUid");
                ticketVo.setTitle("TestTitle");
                ticketVo.setDescription("TestDescription");
                ticketVo.setClientMail("TestClientMail");
                ticketVo.setType(typeVo);
                ticketVo.setCurrentStatus(statusVo);
                ticketVo.setUser(userVO);
                ticketVo.setComments(commentVoList);

                HistoryVo historyVo = new HistoryVo();
                historyVo.setTicket(null);
                historyVo.setModStatus(HistoryEnum.CREATED);
                List<HistoryVo> historyVoList = new ArrayList<>();
                historyVoList.add(historyVo);

                ticketVo.setHistory(historyVoList);

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
