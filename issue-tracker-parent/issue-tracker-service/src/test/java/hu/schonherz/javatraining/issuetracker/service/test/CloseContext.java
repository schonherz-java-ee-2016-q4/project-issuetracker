package hu.schonherz.javatraining.issuetracker.service.test;

import org.junit.Test;

public class CloseContext {
	@Test
	public void stopTheContainer() throws Exception {

		CreateContext.ejbContainer.close();
	}
}
