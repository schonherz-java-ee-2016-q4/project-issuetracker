package hu.schonherz.javatraining.issuetracker.service.test;

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
	
	private static Long storedId;
	
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
				HistoryVo historyVo = HistoryVo.builder()
						.modStatus(HistoryEnum.CREATED)
						.build();
				
				historyVo = serviceLocal.save(historyVo, "test");
				storedId = historyVo.getId();
				Assert.assertNotEquals(historyVo.getId(), null);
			} catch (Exception e) {
				log.error("Error to save", e);
				Assert.fail();
			}
			return null;
		});
	}
	
	@Test
	public void test2findById() throws Exception {
		transactionalCaller.call(() -> {
			try {
				HistoryVo historyInDb = serviceLocal.findById(storedId);
				Assert.assertNotEquals(historyInDb, null);
			} catch (Exception e) {
				log.error("Error in findById", e);
				Assert.fail();
			}
			return null;
		});
	}
	
	@Test
	public void test3update() throws Exception {
		transactionalCaller.call(() -> {
			try {
				HistoryVo historyVo = serviceLocal.findById(storedId);
				historyVo.setModStatus(HistoryEnum.CHANGED_STATUS);
				serviceLocal.update(historyVo, "test");
			} catch (Exception e) {
				log.error("Error to update", e);
				Assert.fail();
			}
			return null;
		});
	}
	
	@Test
	public void test4findByIdAfterUpdate() throws Exception {
		transactionalCaller.call(() -> {
			try {
				HistoryVo historyInDb = serviceLocal.findById(storedId);
				Assert.assertEquals(historyInDb.getModStatus(), HistoryEnum.CHANGED_STATUS);
			} catch (Exception e) {
				log.error("Error in findById", e);
				Assert.fail();
			}
			return null;
		});
	}
}
