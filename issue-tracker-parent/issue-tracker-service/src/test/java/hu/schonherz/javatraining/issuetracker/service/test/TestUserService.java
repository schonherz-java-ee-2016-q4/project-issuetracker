package hu.schonherz.javatraining.issuetracker.service.test;

import java.util.concurrent.Callable;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import hu.schonherz.javatraining.issuetracker.client.api.service.user.UserServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ManagedBean
public class TestUserService {
	static final Logger log = LogManager.getLogger(TestUserService.class.getName());
	
	@EJB
	UserServiceLocal serviceLocal;

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
	public void test1save() throws Exception {
		transactionalCaller.call(() -> {
			try {
				UserVo userVO = new UserVo();
				userVO.setUsername("test2");
				serviceLocal.save(userVO);
			} catch (Exception e) {
				log.error("Error in test1save", e);
				Assert.fail();
			}
			return null;
		});
    }

	@Test
	public void test2findByUsername() throws Exception {
		transactionalCaller.call(() -> {
			try {
				UserVo vo = serviceLocal.findByUsername("test2");
				Assert.assertEquals("test2", vo.getUsername());
			} catch (Exception e) {
				log.error("Error in test2WithTransaction", e);
				Assert.fail();
			}
			return null;
		});
    }
	
	public static interface Caller {
        public <V> V call(Callable<V> callable) throws Exception;
    }
	
	@Stateless
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public static class TransactionBean implements Caller {

        public <V> V call(Callable<V> callable) throws Exception {
            return callable.call();
        }
    }

}
