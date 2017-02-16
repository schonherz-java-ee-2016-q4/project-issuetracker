package hu.schonherz.javatraining.issuetracker.service.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
//@SuiteClasses({ CreateContext.class, TestUserService.class,TestStatusService.class,TestStatusOrderService.class, TestTypeService.class, TestRoleService.class, TestTicketService.class, TestCompanyService.class, CloseContext.class})
@SuiteClasses({ CreateContext.class, TestUserService.class, TestStatusService.class,TestStatusOrderService.class, TestTypeService.class,  CloseContext.class})
public class TestServiceSuite {

}
