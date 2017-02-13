package hu.schonherz.javatraining.issuetracker.service.impl.comment;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.javatraining.issuetracker.client.api.service.comment.CommentServiceLocal;
import hu.schonherz.javatraining.issuetracker.client.api.service.comment.CommentServiceRemote;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CommentVo;
import hu.schonherz.javatraining.issuetracker.core.dao.CommentDao;
import hu.schonherz.javatraining.issuetracker.core.entities.CommentEntity;
import hu.schonherz.javatraining.issuetracker.service.mapper.generic.GenericVoMappers;

@Stateless(mappedName = "CommentService")
@Local(CommentServiceLocal.class)
@Remote(CommentServiceRemote.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CommentServiceBean implements CommentServiceLocal, CommentServiceRemote {

    @Autowired
    private CommentDao commentDao;

    @Override
    public CommentVo findById(Long id) {
        CommentEntity comment = commentDao.findById(id);
        return GenericVoMappers.commentVoMapper.toVo(comment);
    }

    @Override
    public CommentVo save(CommentVo comment, String username) {
        comment.setRecUserName(username);
        return GenericVoMappers.commentVoMapper.toVo(commentDao.save(GenericVoMappers.commentVoMapper.toEntity(comment)));
    }

    @Override
    public CommentVo update(CommentVo comment, String username) {
        comment.setModUserName(username);

        return GenericVoMappers.commentVoMapper.toVo(commentDao.save(GenericVoMappers.commentVoMapper.toEntity(comment)));
    }
}
