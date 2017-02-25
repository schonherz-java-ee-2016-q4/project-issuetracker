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

import hu.schonherz.javatraining.issuetracker.client.api.service.statusorder.StatusOrderServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;
import hu.schonherz.javatraining.issuetracker.service.test.TestUserService.Caller;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ManagedBean
public class TestStatusOrderService {

	static final Logger log = LogManager.getLogger(TestStatusOrderService.class.getName());
	
	@EJB
	StatusOrderServiceLocal serviceLocal;

	@EJB
	private Caller transactionalCaller;

	
	@Before
	public void startTheContainer() throws Exception {
		try {
			CreateContext.ejbContainer.getContext().bind("inject", this);
		} catch (Throwable e) {
			log.error("Error startContainer",e);
		}
	}

	@Test
	public void test1Save() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = new StatusOrderVo();
				vo.setFromStatusId(0L);
				vo.setToStatusId(1L);
				serviceLocal.save(vo, "testUser");
			} catch (Exception e) {
				log.error("Error to Save", e);
				Assert.fail();
			}
			return null;
		});
	}

	@Test
	public void test2FindByFromStatusId() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByFromStatusId(0L).get(0);
				Assert.assertEquals(vo.getFromStatusId().longValue(), 0L);
			} catch (Exception e) {
				log.error("Error in test2FindByFromStatusId", e);
				Assert.fail();
			}
			return null;
		});
	}
	
	@Test
	public void test3FindByToStatusId() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByToStatusId(1L).get(0);
				Assert.assertEquals(vo.getToStatusId().longValue(), 1L);
			} catch (Exception e) {
				log.error("Error in test3FindByToStatusId", e);
				Assert.fail();
			}
			return null;
		});
	}
	
	@Test
	public void test4Update() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByFromStatusId(0L).get(0);
				vo.setFromStatusId(2L);
				serviceLocal.update(vo, "testUser");
			} catch (Exception e) {
				log.error("Error in test4Update", e);
				Assert.fail();
			}
			return null;
		});
	}
	
	@Test
	public void test5FindByFromStatusIdAfterUpdate() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByFromStatusId(2L).get(0);
				Assert.assertEquals(vo.getFromStatusId().longValue(), 2L);
			} catch (Exception e) {
				log.error("Error in test5FindByFromStatusIdAfterUpdate", e);
				Assert.fail();
			}
			return null;
		});
	}
	
	@Test
	public void test6FindByFromStatusIdAndToStatusId() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByFromStatusIdAndToStatusId(2L, 1L);
				Assert.assertNotEquals(vo, null);
			} catch (Exception e) {
				log.error("Error in test6FindByFromStatusIdAndToStatusId", e);
				Assert.fail();
			}
			return null;
		});
	}
	
	@Test
	public void test7Delete() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByFromStatusIdAndToStatusId(2L, 1L);
				serviceLocal.deleteById(vo.getId());
			} catch (Exception e) {
				log.error("Error in test7Delete", e);
				Assert.fail();
			}
			return null;
		});
	}
	
}
