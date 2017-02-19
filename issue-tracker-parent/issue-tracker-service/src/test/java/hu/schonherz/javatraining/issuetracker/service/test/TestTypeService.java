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
                log.error("Error startContainer",e);
            }
        }

    @Test
    public void test0Save() throws Exception {
        transactionalCaller.call(() -> {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

                CompanyVo companyVo = new CompanyVo();
                companyVo.setRecUserName("test");
                companyVo.setModUserName("test");
                companyVo.setRecDate(fmt.parse("2013-05-06"));
                companyVo.setModDate(fmt.parse("2013-05-06"));
                companyVo.setName("testCompany");

                StatusVo statusVo = new StatusVo();
                statusVo.setRecUserName("test");
                statusVo.setModUserName("test");
                statusVo.setRecDate(fmt.parse("2013-05-06"));
                statusVo.setModDate(fmt.parse("2013-05-06"));
                statusVo.setName("test");
                statusVo.setDescription("test");

                statusVo = statusServiceLocal.save(statusVo,"username");
                companyVo = companyServiceLocal.save(companyVo,"username");
            } catch (Exception e) {
                log.error("Error to save a typevo",e);

            }
            return null;
        });
    }

     @Test
    public void test1Save() throws Exception {
        transactionalCaller.call(() -> {
            try {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

                TypeVo typeVo = new TypeVo();


                typeVo.setName("testType");
                typeVo.setDescription("testDescription");

                typeVo.setStartEntity(statusServiceLocal.findByName("test"));

                CompanyVo companyVo = companyServiceLocal.findByName("testCompany");
                typeVo.setCompany(companyVo);
                serviceLocal.save(typeVo, "testUser");
            } catch (Exception e) {
                log.error("Error to save a typevo",e);

            }
            return null;
        });
    }

        @Test
        public void test2FindByName() throws Exception {
            transactionalCaller.call(() -> {
                try {
                    TypeVo vo = serviceLocal.findByName("testStatus");
                    Assert.assertEquals("testStatus", vo.getName());
                } catch (Exception e) {
                    log.error("Error in findbyname",e);
                }
                return null;
            });
        }

        @Test
        public void test3FindByName2() throws Exception {
            transactionalCaller.call(() -> {
                try {
                    TypeVo vo = serviceLocal.findByName("testStatus");
                    Assert.assertEquals("testDescription", vo.getDescription());
                } catch (Exception e) {
                    log.error("Error in findbyname",e);
                }
                return null;
            });
        }

        @Test
        public void test4FindById() throws Exception {
            transactionalCaller.call(() -> {
                try {
                    TypeVo vo = serviceLocal.findByName("testStatus");
                    TypeVo voById = serviceLocal.findById(vo.getId());
                    Assert.assertEquals(vo, voById);
                } catch (Exception e) {
                    log.error("Error in findbyname",e);
                }
                return null;
            });
        }

        @Test
        public void test5Update() throws Exception {
            transactionalCaller.call(() -> {
                try {
                    TypeVo vo = serviceLocal.findByName("testStatus");
                    vo.setName("testStatus_updated");
                    serviceLocal.update(vo, "modUser");
                } catch (Exception e) {
                    log.error("Error to update vo",e);
                }
                return null;
            });
        }

        @Test
        public void test6FindByNameAfterUpdate() throws Exception {
            transactionalCaller.call(() -> {
                try {
                    TypeVo vo = serviceLocal.findByName("testStatus_updated");
                    Assert.assertEquals("testStatus_updated", vo.getName());
                } catch (Exception e) {
                    log.error("Error in findbyname after update",e);
                }
                return null;
            });
        }
    }


