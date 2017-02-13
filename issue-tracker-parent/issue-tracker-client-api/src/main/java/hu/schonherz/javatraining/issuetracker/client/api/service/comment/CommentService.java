package hu.schonherz.javatraining.issuetracker.client.api.service.comment;

import hu.schonherz.javatraining.issuetracker.client.api.vo.CommentVo;

public interface CommentService {

    CommentVo findById(Long id);
    CommentVo save(CommentVo comment, String username);
    CommentVo update(CommentVo comment, String username);
}
