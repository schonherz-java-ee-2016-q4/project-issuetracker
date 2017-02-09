package hu.schonherz.javatraining.issuetracker.service.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({CreateContext.class, TestUserService.class, CloseContext.class, TestServiceSuite.class })
public class TestServiceSuite {

}
