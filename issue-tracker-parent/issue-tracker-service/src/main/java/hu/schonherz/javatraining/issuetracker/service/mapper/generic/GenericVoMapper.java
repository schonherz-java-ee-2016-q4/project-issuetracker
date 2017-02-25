package hu.schonherz.javatraining.issuetracker.service.mapper.generic;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

public class GenericVoMapper <E, V> {
	private static Mapper mapper = new DozerBeanMapper();
	private Class<?> entityType;
	private Class<?> voType;
	
	public GenericVoMapper(Class<?> entityType, Class<?> voType) {
		this.entityType = entityType;
		this.voType = voType;
	}
	
	@SuppressWarnings("unchecked")
	public V toVo(E entity) {
		if (entity == null) {
			return null;
		}
		return (V)mapper.map(entity, voType);
	}

	@SuppressWarnings("unchecked")
	public E toEntity(V vo) {
		if (vo == null) {
			return null;
		}
		
		return (E)mapper.map(vo, entityType);
	}

	public List<V> toVo(List<E> entities) {
		List<V> rv = new ArrayList<>();
		for (E entity : entities) {
			rv.add(toVo(entity));
		}
		return rv;
	}

	public List<E> toEntity(List<V> vos) {
		List<E> rv = new ArrayList<>();
		for (V vo : vos) {
			rv.add(toEntity(vo));
		}
		return rv;
	}
}
