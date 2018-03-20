package com.ctx.loteriaservice.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.ctx.loteriaservice.vo.MegaSenaVO;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.FullEntity.Builder;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

@Component
public class MegaSenaDAO extends DAO<MegaSenaVO> {

	@Override
	public String getKind() {
		return MegaSenaVO.KIND;
	}

	public MegaSenaVO insert(MegaSenaVO vo) {
		Entity e = datastore.add(buildInsertVO(vo));
		return this.loadById(e.getKey().getId());
	}

	private FullEntity<IncompleteKey> buildInsertVO(MegaSenaVO vo) {
		IncompleteKey key = keyFactory.newKey();
		Builder<IncompleteKey> builder = Entity.newBuilder(key);
		if (vo.getNumeros() != null) builder.set(MegaSenaVO.PROP_NUMEROS, vo.getNumeros());
		if (vo.getConcurso() != null) builder.set(MegaSenaVO.PROP_CONCURSO, vo.getConcurso());
		if (vo.getDataProximoConcurso() != null) builder.set(MegaSenaVO.PROP_DATA_PROXIMO_CONCURSO, vo.getDataProximoConcurso());
		return builder.build();
	}

	public MegaSenaVO loadById(Long id) {
		Entity e = datastore.get(keyFactory.newKey(id));
		return entityToVO(e);
	}

	public List<MegaSenaVO> listAll() {
		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind(MegaSenaVO.KIND)
				.setOrderBy(OrderBy.desc(MegaSenaVO.PROP_CONCURSO))
				.build();
		QueryResults<Entity> resultList = datastore.run(query); // Run the query
		return entitiesToVO(resultList);
		
	}

	private List<MegaSenaVO> entitiesToVO(QueryResults<Entity> resultList) {
		List<MegaSenaVO> list = new ArrayList<>();
		while (resultList.hasNext()) {
			list.add(entityToVO(resultList.next()));
		}
		return list;
	}

	protected MegaSenaVO entityToVO(Entity e) {
		MegaSenaVO vo = new MegaSenaVO();
		vo.setId(e.getKey().getId());
		if (e.contains(MegaSenaVO.PROP_NUMEROS)) vo.setNumeros(e.getString(MegaSenaVO.PROP_NUMEROS));
		if (e.contains(MegaSenaVO.PROP_CONCURSO)) vo.setConcurso(e.getLong(MegaSenaVO.PROP_CONCURSO));
		if (e.contains(MegaSenaVO.PROP_DATA_PROXIMO_CONCURSO)) vo.setDataProximoConcurso(e.getTimestamp(MegaSenaVO.PROP_DATA_PROXIMO_CONCURSO));
		return vo;
	}

	public MegaSenaVO loadByConcurso(Long concurso) {
		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind(MegaSenaVO.KIND)
				.setFilter(PropertyFilter.eq(MegaSenaVO.PROP_CONCURSO, concurso))
				.setLimit(1)
				.build();
		QueryResults<Entity> resultList = datastore.run(query);
		return resultList != null && resultList.hasNext() ? entityToVO(resultList.next()) : null;
	}

	public List<MegaSenaVO> insert(List<MegaSenaVO> list) {
		List<FullEntity<IncompleteKey>> entities = new ArrayList<FullEntity<IncompleteKey>>(list.size());
		list.stream().forEach(item -> entities.add(buildInsertVO(item)));
		List<Entity> added = datastore.add(entities.toArray(new FullEntity<?>[0]));
		IntStream.range(0, added.size()).forEach(index -> list.get(index).setId(added.get(index).getKey().getId()));
		return list;
	}

	public MegaSenaVO loadUltimo() {
		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind(MegaSenaVO.KIND)
				.setOrderBy(OrderBy.desc(MegaSenaVO.PROP_CONCURSO))
				.setLimit(1)
				.build();
		QueryResults<Entity> resultList = datastore.run(query);
		return resultList != null && resultList.hasNext() ? entityToVO(resultList.next()) : null;
	}
}
