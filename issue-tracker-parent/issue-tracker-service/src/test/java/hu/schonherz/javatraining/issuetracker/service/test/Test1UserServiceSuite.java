package hu.schonherz.javatraining.issuetracker.service.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CreateContext.class, TestCompanyService.class, TestRoleService.class, TestUserService.class, CloseContext.class})
public class Test1UserServiceSuite {

}
