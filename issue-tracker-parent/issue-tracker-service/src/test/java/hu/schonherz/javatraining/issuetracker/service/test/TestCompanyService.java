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

import hu.schonherz.javatraining.issuetracker.client.api.service.company.CompanyServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ManagedBean
public class TestCompanyService {
	static final Logger log = LogManager.getLogger(TestCompanyService.class.getName());

	@EJB
	CompanyServiceLocal serviceLocal;
	
	@EJB
	private TestUserService.Caller transactionalCaller;
	
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
				CompanyVo companyVo = new CompanyVo();
				companyVo.setName("testCompany");
				serviceLocal.save(companyVo, "testUser");
			} catch (Exception e) {
                log.error("Error to save company",e);
			}
			return null;
		});
	}
	
	@Test
	public void test2FindByName() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = serviceLocal.findByName("testCompany");
				Assert.assertEquals("testCompany", companyVo.getName());
			} catch (Exception e) {
				log.error("Error in findByName company", e);
			}
			
			return null;
		});
	}
	
	@Test
	public void test3FindById() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = serviceLocal.findByName("testCompany");
				CompanyVo voById = serviceLocal.findById(companyVo.getId());
				Assert.assertEquals(companyVo, voById);
			} catch (Exception e) {
				log.error("Error in findById company", e);
			}

			return null;
		});
	}
	
	@Test
	public void test4Update() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = serviceLocal.findByName("testCompany");
				companyVo.setName("testCompanyUpdate");
				serviceLocal.update(companyVo, "modUser");
			} catch (Exception e) {
				log.error("Error in update company", e);
			}
			return null;
		});
	}
	
	@Test
	public void test5FindByNameAfterUpdate() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = serviceLocal.findByName("testCompanyUpdate");
				Assert.assertEquals("testCompanyUpdate", companyVo.getName());
			} catch (Exception e) {
				log.error("Error in findByName after update company", e);
			}
			
			return null;
		});
	}
	
	@Test
	public void test6FindByIdAfterUpdate() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = serviceLocal.findByName("testCompanyUpdate");
				CompanyVo voById = serviceLocal.findById(companyVo.getId());
				Assert.assertEquals(companyVo, voById);
			} catch (Exception e) {
				log.error("Error in findById after update company", e);
			}
			
			return null;
		});
	}
}
