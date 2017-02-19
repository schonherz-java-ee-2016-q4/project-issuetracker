package hu.schonherz.javatraining.issuetracker.service.test;

import java.text.SimpleDateFormat;

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
import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.type.TypeServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ManagedBean
public class TestTypeService {
	static final Logger log = LogManager.getLogger(TestTypeService.class.getName());

	@EJB
	TypeServiceLocal serviceLocal;

	@EJB
	CompanyServiceLocal companyServiceLocal;

	@EJB
	StatusServiceLocal statusServiceLocal;

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
	public void test0Save() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = new CompanyVo();
				companyVo.setName("testCompany");
				companyVo = companyServiceLocal.save(companyVo, "username");

				StatusVo statusVo = new StatusVo();
				statusVo.setName("test");
				statusVo.setDescription("test");
				statusVo = statusServiceLocal.save(statusVo, "username");
			} catch (Exception e) {
				log.error("Error to save a typevo", e);
			}
			return null;
		});
	}

	@Test
	public void test1Save() throws Exception {
		transactionalCaller.call(() -> {
			try {
				TypeVo typeVo = new TypeVo();

				typeVo.setName("testType");
				typeVo.setDescription("testDescription");
				typeVo.setStartEntity(statusServiceLocal.findByName("test"));
				typeVo.setCompany(companyServiceLocal.findByName("testCompany"));
				serviceLocal.save(typeVo, "testUser");
			} catch (Exception e) {
				log.error("Error to save a typevo", e);

			}
			return null;
		});
	}

	@Test
	public void test2FindByName() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = companyServiceLocal.findByName("testCompany");
				TypeVo vo = serviceLocal.findByNameAndCompany("testType", companyVo);
				Assert.assertEquals("testType", vo.getName());
			} catch (Exception e) {
				log.error("Error in findbyname", e);
			}
			return null;
		});
	}

	@Test
	public void test3FindByName2() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = companyServiceLocal.findByName("testCompany");
				TypeVo vo = serviceLocal.findByNameAndCompany("testType", companyVo);
				Assert.assertEquals("testDescription", vo.getDescription());
			} catch (Exception e) {
				log.error("Error in findbyname", e);
			}
			return null;
		});
	}

	@Test
	public void test4FindById() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = companyServiceLocal.findByName("testCompany");
				TypeVo vo = serviceLocal.findByNameAndCompany("testType", companyVo);
				TypeVo voById = serviceLocal.findById(vo.getId());
				Assert.assertEquals(vo, voById);
			} catch (Exception e) {
				log.error("Error in findbyname", e);
			}
			return null;
		});
	}

	@Test
	public void test5Update() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = companyServiceLocal.findByName("testCompany");
				TypeVo vo = serviceLocal.findByNameAndCompany("testType", companyVo);
				vo.setName("testType_updated");
				serviceLocal.update(vo, "modUser");
			} catch (Exception e) {
				log.error("Error to update vo", e);
			}
			return null;
		});
	}

	@Test
	public void test6FindByNameAfterUpdate() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CompanyVo companyVo = companyServiceLocal.findByName("testCompany");
				TypeVo vo = serviceLocal.findByNameAndCompany("testType_updated", companyVo);
				Assert.assertEquals("testType_updated", vo.getName());
			} catch (Exception e) {
				log.error("Error in findbyname after update", e);
			}
			return null;
		});
	}
}
