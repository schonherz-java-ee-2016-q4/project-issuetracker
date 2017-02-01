package hu.schonherz.javatraining.issuetracker.service.mapper.type;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.core.entities.TypeEntity;

public class TypeVoMapper {
	static Mapper mapper = new DozerBeanMapper();

	public static TypeVo toVo(TypeEntity entity) {
		if (entity == null) {
			return null;
		}
		return mapper.map(entity, TypeVo.class);
	}

	public static TypeEntity toEntity(TypeVo vo) {
		if (vo == null) {
			return null;
		}
		
		return mapper.map(vo, TypeEntity.class);
	}

	public static List<TypeVo> toVo(List<TypeEntity> entities) {
		List<TypeVo> rv = new ArrayList<>();
		for (TypeEntity entity : entities) {
			rv.add(toVo(entity));
		}
		return rv;
	}

	public static List<TypeEntity> toEntity(List<TypeVo> vos) {
		List<TypeEntity> rv = new ArrayList<>();
		for (TypeVo vo : vos) {
			rv.add(toEntity(vo));
		}
		return rv;
	}
}
