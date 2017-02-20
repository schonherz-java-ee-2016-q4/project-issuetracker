package hu.schonherz.javatraining.issuetracker.service.test;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import hu.schonherz.javatraining.issuetracker.client.api.service.comment.CommentServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.ticket.TicketServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CommentVo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ManagedBean
public class TestCommentService {
	static final Logger log = LogManager.getLogger(TestTypeService.class.getName());

	@EJB
	CommentServiceLocal serviceLocal;

	@EJB
	TicketServiceLocal ticketServiceLocal;
	
	@EJB
	private TestUserService.Caller transactionalCaller;
	
	private Long savedId;
	
	@Before
	public void startTheContainer() throws Exception {
		try {
			CreateContext.ejbContainer.getContext().bind("inject", this);
		} catch (Throwable e) {
			log.error("Error startContainer", e);
		}
	}

	@Test
	public void test1Save() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CommentVo commentVo = CommentVo.builder()
						.commentText("testText")
						.build();
				
				commentVo = serviceLocal.save(commentVo, "test");
				Assert.assertNotEquals(commentVo.getId(), null);
			} catch (Exception e) {
				log.error("Error to save", e);
			}
			return null;
		});
	}

	@Test
	public void test2findById() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CommentVo commentVo = serviceLocal.findById(savedId);
				Assert.assertEquals(commentVo.getId(), savedId);
			} catch (Exception e) {
				log.error("Error in findById", e);
			}
			return null;
		});
	}
	
	@Test
	public void test3update() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CommentVo commentVo = serviceLocal.findById(savedId);
				commentVo.setCommentText("newText");
				serviceLocal.update(commentVo, "test");
			} catch (Exception e) {
				log.error("Error to update", e);
			}
			return null;
		});
	}
	
	@Test
	public void test4findByIdAfterUpdate() throws Exception {
		transactionalCaller.call(() -> {
			try {
				CommentVo commentVo = serviceLocal.findById(savedId);
				Assert.assertEquals(commentVo.getCommentText(), "newText");
			} catch (Exception e) {
				log.error("Error in findById after update", e);
			}
			return null;
		});
	}
}
