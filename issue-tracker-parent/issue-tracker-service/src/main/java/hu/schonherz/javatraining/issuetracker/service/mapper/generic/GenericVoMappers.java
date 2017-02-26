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
	public static final GenericVoMapper<UserEntity, UserVo> userVoMapper;
	public static final GenericVoMapper<RoleEntity, RoleVo> roleVoMapper;
	public static final GenericVoMapper<CommentEntity, CommentVo> commentVoMapper;
	public static final GenericVoMapper<CompanyEntity, CompanyVo> companyVoMapper;
	public static final GenericVoMapper<StatusEntity, StatusVo> statusVoMapper;
	public static final GenericVoMapper<StatusOrderEntity, StatusOrderVo> statusOrderVoMapper;
	public static final GenericVoMapper<HistoryEntity, HistoryVo> historyVoMapper;
	public static final GenericVoMapper<TypeEntity, TypeVo> typeVoMapper;
	public static final GenericVoMapper<TicketEntity, TicketVo> ticketVoMapper;
	
	static {
		userVoMapper = new GenericVoMapper<>(UserEntity.class, UserVo.class);
		roleVoMapper = new GenericVoMapper<>(RoleEntity.class, RoleVo.class);
		commentVoMapper = new GenericVoMapper<>(CommentEntity.class, CommentVo.class);
		companyVoMapper = new GenericVoMapper<>(CompanyEntity.class, CompanyVo.class);
		statusVoMapper = new GenericVoMapper<>(StatusEntity.class, StatusVo.class);
		statusOrderVoMapper = new GenericVoMapper<>(StatusOrderEntity.class, StatusOrderVo.class);
		historyVoMapper = new GenericVoMapper<>(HistoryEntity.class, HistoryVo.class);
		typeVoMapper = new GenericVoMapper<>(TypeEntity.class, TypeVo.class);
		ticketVoMapper = new GenericVoMapper<>(TicketEntity.class, TicketVo.class);
		
	}
	
	private GenericVoMappers() {}
}
