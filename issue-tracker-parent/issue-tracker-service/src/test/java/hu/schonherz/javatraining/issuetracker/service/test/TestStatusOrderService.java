package hu.schonherz.javatraining.issuetracker.service.test;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;

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

	@EJB
	StatusOrderServiceLocal serviceLocal;

	@EJB
	private Caller transactionalCaller;

	
	@Before
	public void startTheContainer() throws Exception {
		try {
			CreateContext.ejbContainer.getContext().bind("inject", this);
		} catch (Throwable e) {
			throw e;
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
				throw new RuntimeException(e);
			}
			return null;
		});
	}

	@Test
	public void test2FindByFromStatusId() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByFromStatusId(0L);
				Assert.assertEquals(vo.getFromStatusId().longValue(), 0L);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}
	
	@Test
	public void test3FindByToStatusId() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByToStatusId(1L);
				Assert.assertEquals(vo.getToStatusId().longValue(), 1L);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}
	
	@Test
	public void test4FindByFromAndToStatusId() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByFromAndToStatusId(0L, 1L);
				Assert.assertEquals(vo.getFromStatusId().longValue() == 0L &&
						vo.getToStatusId().longValue() == 1L, true);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}
	
	@Test
	public void test5Update() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByFromStatusId(0L);
				vo.setFromStatusId(2L);
				serviceLocal.update(vo, "testUser");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}
	
	@Test
	public void test6FindByFromStatusIdAfterUpdate() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusOrderVo vo = serviceLocal.findByFromStatusId(2L);
				Assert.assertEquals(vo.getFromStatusId().longValue(), 2L);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}
}
