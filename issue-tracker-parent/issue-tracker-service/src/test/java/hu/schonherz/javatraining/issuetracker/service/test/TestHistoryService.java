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

import hu.schonherz.javatraining.issuetracker.client.api.service.history.HistoryServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryEnum;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ManagedBean
public class TestHistoryService {
	static final Logger log = LogManager.getLogger(TestTypeService.class.getName());

	@EJB
	HistoryServiceLocal serviceLocal;

	@EJB
	TicketServiceLocal ticketServiceLocal;
	
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
				TicketVo ticketVo = ticketServiceLocal.findByUid("TestUid");
				HistoryVo historyVo = HistoryVo.builder()
						.modStatus(HistoryEnum.CREATED)
						.build();
				
				historyVo = serviceLocal.save(historyVo, "test");
				Assert.assertNotEquals(historyVo.getId(), null);
			} catch (Exception e) {
				log.error("Error to save", e);
			}
			return null;
		});
	}

	/*@Test
	public void test2findByTicket() throws Exception {
		transactionalCaller.call(() -> {
			try {
				TicketVo ticketVo = ticketServiceLocal.findByUid("TestUid");
				List<HistoryVo> history = serviceLocal.findByTicket(ticketVo);
				Assert.assertNotEquals(history.size(), 0);
			} catch (Exception e) {
				log.error("Error in findByTicket", e);
			}
			return null;
		});
	}
	
	@Test
	public void test3findById() throws Exception {
		transactionalCaller.call(() -> {
			try {
				TicketVo ticketVo = ticketServiceLocal.findByUid("TestUid");
				List<HistoryVo> history = serviceLocal.findByTicket(ticketVo);
				HistoryVo historyVo = history.get(0);
				HistoryVo historyInDb = serviceLocal.findById(historyVo.getId());
				
				Assert.assertEquals(historyVo.getId(), historyInDb.getId());
			} catch (Exception e) {
				log.error("Error in findById", e);
			}
			return null;
		});
	}
	
	@Test
	public void test4update() throws Exception {
		transactionalCaller.call(() -> {
			try {
				TicketVo ticketVo = ticketServiceLocal.findByUid("TestUid");
				List<HistoryVo> history = serviceLocal.findByTicket(ticketVo);
				HistoryVo historyVo = history.get(0);
				historyVo.setModStatus(HistoryEnum.CHANGED_STATUS);
				serviceLocal.update(historyVo, "test");
			} catch (Exception e) {
				log.error("Error to update", e);
			}
			return null;
		});
	}*/
}
