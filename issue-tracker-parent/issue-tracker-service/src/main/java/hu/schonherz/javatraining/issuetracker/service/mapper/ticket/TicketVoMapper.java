package hu.schonherz.javatraining.issuetracker.service.mapper.ticket;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.core.entities.TicketEntity;

public class TicketVoMapper {
	static Mapper mapper = new DozerBeanMapper();

	public static TicketVo toVo(TicketEntity ticket) {
		if (ticket == null) {
			return null;
		}
		return mapper.map(ticket, TicketVo.class);
	}

	public static TicketEntity toEntity(TicketVo ticketVo) {
		if (ticketVo == null) {
			return null;
		}
		
		return mapper.map(ticketVo, TicketEntity.class);
	}

	public static List<TicketVo> toVo(List<TicketEntity> tickets) {
		List<TicketVo> rv = new ArrayList<>();
		for (TicketEntity users : tickets) {
			rv.add(toVo(users));
		}
		return rv;
	}

	public static List<TicketEntity> toEntity(List<TicketVo> ticketVos) {
		List<TicketEntity> rv = new ArrayList<>();
		for (TicketVo users : ticketVos) {
			rv.add(toEntity(users));
		}
		return rv;
	}
}
