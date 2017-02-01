package hu.schonherz.javatraining.issuetracker.service.mapper.history;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;
import hu.schonherz.javatraining.issuetracker.core.entities.HistoryEntity;


public class HistoryVoMapper {
	static Mapper mapper = new DozerBeanMapper();

	public static HistoryVo toVo(HistoryEntity entity) {
		if (entity == null) {
			return null;
		}
		return mapper.map(entity, HistoryVo.class);
	}

	public static HistoryEntity toEntity(HistoryVo vo) {
		if (vo == null) {
			return null;
		}
		
		return mapper.map(vo, HistoryEntity.class);
	}

	public static List<HistoryVo> toVo(List<HistoryEntity> entities) {
		List<HistoryVo> rv = new ArrayList<>();
		for (HistoryEntity entity : entities) {
			rv.add(toVo(entity));
		}
		return rv;
	}

	public static List<HistoryEntity> toEntity(List<HistoryVo> vos) {
		List<HistoryEntity> rv = new ArrayList<>();
		for (HistoryVo vo : vos) {
			rv.add(toEntity(vo));
		}
		return rv;
	}
}
