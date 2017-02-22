package hu.schonherz.javatraining.issuetracker.service.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CreateContext.class,
	TestStatusService.class, TestStatusOrderService.class, TestTypeService.class,
	TestTicketService.class, TestHistoryService.class, TestCommentService.class,
	CloseContext.class})
public class Test2TicketServiceSuite {

}
