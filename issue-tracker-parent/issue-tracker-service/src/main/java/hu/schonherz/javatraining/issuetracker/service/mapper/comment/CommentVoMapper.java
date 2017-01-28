package hu.schonherz.javatraining.issuetracker.service.mapper.comment;


import hu.schonherz.javatraining.issuetracker.client.api.vo.CommentVo;
import hu.schonherz.javatraining.issuetracker.core.entities.CommentEntity;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;

public class CommentVoMapper {
    static Mapper mapper = new DozerBeanMapper();

    public static CommentVo toVo(CommentEntity comment) {
        if (comment == null) {
            return null;
        }
        return mapper.map(comment, CommentVo.class);
    }

    public static List<CommentVo> toVo(List<CommentEntity> comments) {
        List<CommentVo> rv = new ArrayList<>();
        for (CommentEntity comment : comments) {
            rv.add(toVo(comment));
        }
        return rv;
    }

    public static CommentEntity toEntity(CommentVo commentVo) {
        if (commentVo == null) {
            return null;
        }
        return mapper.map(commentVo, CommentEntity.class);
    }

    public static List<CommentEntity> toEntity(List<CommentVo> comments) {
        List<CommentEntity> rv = new ArrayList<>();
        for (CommentVo comment : comments) {
            rv.add(toEntity(comment));
        }
        return rv;
    }
}
