package hu.schonherz.javatraining.issuetracker.service.mapper.role;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import hu.schonherz.javatraining.issuetracker.client.api.vo.RoleVo;
import hu.schonherz.javatraining.issuetracker.core.entities.RoleEntity;

public class RoleVoMapper {

	static Mapper mapper = new DozerBeanMapper();

	public static RoleVo toVo(RoleEntity Role) {
		if (Role == null) {
			return null;
		}
		return mapper.map(Role, RoleVo.class);
	}

	public static RoleEntity toEntity(RoleVo RoleVo) {
		if (RoleVo == null) {
			return null;
		}
		return mapper.map(RoleVo, RoleEntity.class);
	}

	public static List<RoleVo> toVo(List<RoleEntity> Role) {
		List<RoleVo> rv = new ArrayList<>();
		for (RoleEntity Roles : Role) {
			rv.add(toVo(Roles));
		}
		return rv;
	}

	public static List<RoleEntity> toEntity(List<RoleVo> Role) {
		List<RoleEntity> rv = new ArrayList<>();
		for (RoleVo Roles : Role) {
			rv.add(toEntity(Roles));
		}
		return rv;
	}
}
