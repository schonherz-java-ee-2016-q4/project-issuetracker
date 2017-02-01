package hu.schonherz.javatraining.issuetracker.service.mapper.statusorder;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;
import hu.schonherz.javatraining.issuetracker.core.entities.StatusOrderEntity;

public class StatusOrderVoMapper {
	static Mapper mapper = new DozerBeanMapper();

	public static StatusOrderVo toVo(StatusOrderEntity status) {
		if (status == null) {
			return null;
		}
		return mapper.map(status, StatusOrderVo.class);
	}

	public static StatusOrderEntity toEntity(StatusOrderVo statusOrderVo) {
		if (statusOrderVo == null) {
			return null;
		}
		
		return mapper.map(statusOrderVo, StatusOrderEntity.class);
	}

	public static List<StatusOrderVo> toVo(List<StatusOrderEntity> status) {
		List<StatusOrderVo> rv = new ArrayList<>();
		for (StatusOrderEntity statuses : status) {
			rv.add(toVo(statuses));
		}
		return rv;
	}

	public static List<StatusOrderEntity> toEntity(List<StatusOrderVo> statusOrder) {
		List<StatusOrderEntity> rv = new ArrayList<>();
		for (StatusOrderVo statusOrders : statusOrder) {
			rv.add(toEntity(statusOrders));
		}
		return rv;
	}
}
