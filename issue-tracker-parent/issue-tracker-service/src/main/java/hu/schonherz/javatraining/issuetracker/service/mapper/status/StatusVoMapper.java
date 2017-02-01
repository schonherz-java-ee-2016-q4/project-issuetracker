package hu.schonherz.javatraining.issuetracker.service.mapper.status;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.core.entities.StatusEntity;

public class StatusVoMapper {
	static Mapper mapper = new DozerBeanMapper();

	public static StatusVo toVo(StatusEntity status) {
		if (status == null) {
			return null;
		}
		return mapper.map(status, StatusVo.class);
	}

	public static StatusEntity toEntity(StatusVo statusVo) {
		if (statusVo == null) {
			return null;
		}
		
		return mapper.map(statusVo, StatusEntity.class);
	}

	public static List<StatusVo> toVo(List<StatusEntity> status) {
		List<StatusVo> rv = new ArrayList<>();
		for (StatusEntity statuses : status) {
			rv.add(toVo(statuses));
		}
		return rv;
	}

	public static List<StatusEntity> toEntity(List<StatusVo> status) {
		List<StatusEntity> rv = new ArrayList<>();
		for (StatusVo statuses : status) {
			rv.add(toEntity(statuses));
		}
		return rv;
	}
}
