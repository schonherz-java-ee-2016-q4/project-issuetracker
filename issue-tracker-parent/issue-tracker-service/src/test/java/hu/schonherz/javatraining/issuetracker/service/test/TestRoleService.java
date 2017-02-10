package hu.schonherz.javatraining.issuetracker.service.test;


import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleService;
import hu.schonherz.javatraining.issuetracker.client.api.service.role.RoleServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.status.StatusServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;

    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    @ManagedBean
    public class TestRoleService {
        static final Logger log = LogManager.getLogger(TestTypeService.class.getName());

        @EJB
        RoleServiceLocal serviceLocal;

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
                    RoleVo roleVo = new RoleVo();
                    roleVo.setName("testStatus");
                    serviceLocal.save(roleVo);
                } catch (Exception e) {
                    log.error("Error to save role",e);
                }
                return null;
            });
        }

        @Test
        public void test2FindByName() throws Exception {
            transactionalCaller.call(() -> {
                try {
                    RoleVo vo = serviceLocal.findByName("testRole");
                    Assert.assertEquals("testStatus", vo.getName());
                } catch (Exception e) {
                    log.error("Error to Find by name",e);
                }
                return null;
            });
        }


    }





