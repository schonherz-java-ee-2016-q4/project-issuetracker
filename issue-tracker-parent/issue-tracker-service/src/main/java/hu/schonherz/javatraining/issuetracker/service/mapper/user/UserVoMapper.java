package hu.schonherz.javatraining.issuetracker.service.mapper.user;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import hu.schonherz.javatraining.issuetracker.client.api.vo.UserVo;
import hu.schonherz.javatraining.issuetracker.core.entities.UserEntity;

public class UserVoMapper {
	static Mapper mapper = new DozerBeanMapper();

	public static UserVo toVo(UserEntity user) {
		if (user == null) {
			return null;
		}
		return mapper.map(user, UserVo.class);
	}

	public static UserEntity toEntity(UserVo userVO) {
		if (userVO == null) {
			return null;
		}
		return mapper.map(userVO, UserEntity.class);
	}

	public static List<UserVo> toVo(List<UserEntity> user) {
		List<UserVo> rv = new ArrayList<>();
		for (UserEntity users : user) {
			rv.add(toVo(users));
		}
		return rv;
	}

	public static List<UserEntity> toEntity(List<UserVo> user) {
		List<UserEntity> rv = new ArrayList<>();
		for (UserVo users : user) {
			rv.add(toEntity(users));
		}
		return rv;
	}
}
