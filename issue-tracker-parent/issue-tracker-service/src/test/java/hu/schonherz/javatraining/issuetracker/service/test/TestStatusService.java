package hu.schonherz.javatraining.issuetracker.service.test;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.service.test.TestUserService.Caller;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ManagedBean
public class TestStatusService {

	@EJB
	StatusServiceLocal serviceLocal;

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
				StatusVo statusVo = new StatusVo();
				statusVo.setName("testStatus");
				statusVo.setDescription("testDescription");
				statusVo.setIsEndStatus(true);
				serviceLocal.save(statusVo, "testUser");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}

	@Test
	public void test2FindByName() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusVo vo = serviceLocal.findByName("testStatus");
				Assert.assertEquals("testStatus", vo.getName());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}

	@Test
	public void test3FindByName2() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusVo vo = serviceLocal.findByName("testStatus");
				Assert.assertEquals("testDescription", vo.getDescription());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}

	@Test
	public void test4FindById() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusVo vo = serviceLocal.findByName("testStatus");
				StatusVo voById = serviceLocal.findById(vo.getId());
				Assert.assertEquals(vo, voById);
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
				StatusVo vo = serviceLocal.findByName("testStatus");
				vo.setName("testStatus_updated");
				serviceLocal.update(vo, "modUser");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}
	
	@Test
	public void test6FindByNameAfterUpdate() throws Exception {
		transactionalCaller.call(() -> {
			try {
				StatusVo vo = serviceLocal.findByName("testStatus_updated");
				Assert.assertEquals("testStatus_updated", vo.getName());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		});
	}
}
