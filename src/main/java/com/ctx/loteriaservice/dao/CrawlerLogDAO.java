package com.ctx.loteriaservice.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.ctx.loteriaservice.vo.CrawlerLogVO;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.FullEntity.Builder;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

@Component
public class CrawlerLogDAO extends DAO<CrawlerLogVO> {

	@Override
	public String getKind() {
		return CrawlerLogVO.KIND;
	}

	public CrawlerLogVO insert(CrawlerLogVO vo) {
		Entity e = datastore.add(buildInsertVO(vo));
		return this.loadById(e.getKey().getId());
	}

	private FullEntity<IncompleteKey> buildInsertVO(CrawlerLogVO vo) {
		IncompleteKey key = keyFactory.newKey();
		Builder<IncompleteKey> builder = Entity.newBuilder(key);
		if (vo.getTipo() != null) builder.set(CrawlerLogVO.PROP_TIPO, vo.getTipo());
		if (vo.getDataExecucao() != null) builder.set(CrawlerLogVO.PROP_DATA_EXECUCAO, vo.getDataExecucao());
		if (vo.getMensagem() != null) builder.set(CrawlerLogVO.PROP_MENSAGEM, vo.getMensagem());
		return builder.build();
	}

	public CrawlerLogVO loadById(Long id) {
		Entity e = datastore.get(keyFactory.newKey(id));
		return entityToVO(e);
	}

	public List<CrawlerLogVO> listAll() {
		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind(CrawlerLogVO.KIND)
				.build();
		QueryResults<Entity> resultList = datastore.run(query);
		return entitiesToVO(resultList);
		
	}

	private List<CrawlerLogVO> entitiesToVO(QueryResults<Entity> resultList) {
		List<CrawlerLogVO> list = new ArrayList<>();
		while (resultList.hasNext()) {
			list.add(entityToVO(resultList.next()));
		}
		return list;
	}

	protected CrawlerLogVO entityToVO(Entity e) {
		CrawlerLogVO vo = new CrawlerLogVO();
		vo.setId(e.getKey().getId());
		if (e.contains(CrawlerLogVO.PROP_TIPO)) vo.setTipo(e.getString(CrawlerLogVO.PROP_TIPO));
		if (e.contains(CrawlerLogVO.PROP_DATA_EXECUCAO)) vo.setDataExecucao(e.getTimestamp(CrawlerLogVO.PROP_DATA_EXECUCAO));
		if (e.contains(CrawlerLogVO.PROP_MENSAGEM)) vo.setMensagem(e.getString(CrawlerLogVO.PROP_MENSAGEM));
		return vo;
	}

	public List<CrawlerLogVO> insert(List<CrawlerLogVO> list) {
		List<FullEntity<IncompleteKey>> entities = new ArrayList<FullEntity<IncompleteKey>>(list.size());
		list.stream().forEach(item -> entities.add(buildInsertVO(item)));
		List<Entity> added = datastore.add(entities.toArray(new FullEntity<?>[0]));
		IntStream.range(0, added.size()).forEach(index -> list.get(index).setId(added.get(index).getKey().getId()));
		return list;
	}

	public List<CrawlerLogVO> listByTipo(String tipo) {
		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind(CrawlerLogVO.KIND)
				.setFilter(PropertyFilter.eq(CrawlerLogVO.PROP_TIPO, tipo))
				.setOrderBy(OrderBy.desc(CrawlerLogVO.PROP_DATA_EXECUCAO))
				.build();
		QueryResults<Entity> resultList = datastore.run(query);
		return resultList != null && resultList.hasNext() ? entitiesToVO(resultList) : Collections.emptyList();
	}
}
