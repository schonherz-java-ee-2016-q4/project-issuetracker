package hu.schonherz.javatraining.issuetracker.service.test;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lombok.extern.log4j.Log4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Log4j
public class CreateContext {
	protected static EJBContainer ejbContainer;

	@PersistenceContext(unitName = "hu.schonherz.javatraining.jpa")
	private EntityManager entityManager;

	@Test
	public void startTheContainer() throws Exception {
		try {
			final Properties p = new Properties();

			p.put("hu.schonherz.javatraining.jpa.hibernate.hbm2ddl.auto", "create");
			p.put("hu.schonherz.javatraining.jpa.hibernate.default_schema", "");
			p.put("hu.schonherz.javatraining.jpa.hibernate.transaction.jta.platform",
					"org.apache.openejb.hibernate.OpenEJBJtaPlatform2");
			p.put("hu.schonherz.javatraining.jpa.hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
			p.put("hu.schonherz.javatraining.jpa.database.test", "new://Resource?type=DataSource");
			p.put("hu.schonherz.javatraining.jpa.database.test.JtaManaged", "true");
			p.put("hu.schonherz.javatraining.jpa.database.test.JdbcDriver", "org.hsqldb.jdbcDriver");
			p.put("hu.schonherz.javatraining.jpa.database.test.JdbcUrl",
					"jdbc:hsqldb:file:target/db/test_db;shutdown=true");

			ejbContainer = EJBContainer.createEJBContainer(p);

			ejbContainer.getContext().bind("inject", this);

			ejbContainer.getContext().bind("java:global/IssueTrackerEntityManagerFactory",
					entityManager.getEntityManagerFactory());

			ejbContainer.getContext().bind("java:global/IssueTrackerEntityManager", entityManager);

		} catch (Throwable e) {
			log.error("error in SratupConfig",e);
		}
	}
}
