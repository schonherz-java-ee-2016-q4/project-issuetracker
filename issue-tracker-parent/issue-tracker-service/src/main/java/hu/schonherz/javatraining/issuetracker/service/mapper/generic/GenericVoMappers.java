package hu.schonherz.javatraining.issuetracker.service.mapper.generic;

import hu.schonherz.javatraining.issuetracker.client.api.vo.CommentVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.CompanyVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.HistoryVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusOrderVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.StatusVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TicketVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.TypeVo;
import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.javatraining.issuetracker.core.entities.CommentEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.CompanyEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.HistoryEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.RoleEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.StatusEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.StatusOrderEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TicketEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.TypeEntity;
import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;

public final class GenericVoMappers {
	public static final GenericVoMapper<UserEntity, UserVo> USER_VO_MAPPER;
	public static final GenericVoMapper<RoleEntity, RoleVo> ROLE_VO_MAPPER;
	public static final GenericVoMapper<CommentEntity, CommentVo> COMMENT_VO_MAPPER;
	public static final GenericVoMapper<CompanyEntity, CompanyVo> COMPANY_VO_MAPPER;
	public static final GenericVoMapper<StatusEntity, StatusVo> STATUS_VO_MAPPER;
	public static final GenericVoMapper<StatusOrderEntity, StatusOrderVo> STATUS_ORDER_VO_MAPPER;
	public static final GenericVoMapper<HistoryEntity, HistoryVo> HISTORY_VO_MAPPER;
	public static final GenericVoMapper<TypeEntity, TypeVo> TYPE_VO_MAPPER;
	public static final GenericVoMapper<TicketEntity, TicketVo> TICKET_VO_MAPPER;
	
	static {
		USER_VO_MAPPER = new GenericVoMapper<>(UserEntity.class, UserVo.class);
		ROLE_VO_MAPPER = new GenericVoMapper<>(RoleEntity.class, RoleVo.class);
		COMMENT_VO_MAPPER = new GenericVoMapper<>(CommentEntity.class, CommentVo.class);
		COMPANY_VO_MAPPER = new GenericVoMapper<>(CompanyEntity.class, CompanyVo.class);
		STATUS_VO_MAPPER = new GenericVoMapper<>(StatusEntity.class, StatusVo.class);
		STATUS_ORDER_VO_MAPPER = new GenericVoMapper<>(StatusOrderEntity.class, StatusOrderVo.class);
		HISTORY_VO_MAPPER = new GenericVoMapper<>(HistoryEntity.class, HistoryVo.class);
		TYPE_VO_MAPPER = new GenericVoMapper<>(TypeEntity.class, TypeVo.class);
		TICKET_VO_MAPPER = new GenericVoMapper<>(TicketEntity.class, TicketVo.class);
	}
	
	private GenericVoMappers() {}
}
