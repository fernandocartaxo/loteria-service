package com.ctx.loteriaservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ctx.loteriaservice.dao.CrawlerLogDAO;
import com.ctx.loteriaservice.vo.CrawlerLogVO;

@RestController
@RequestMapping("/crawlerlog")
	
public class CrawlerLogService {
	
	@Autowired
	private CrawlerLogDAO dao;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<CrawlerLogVO> listAll() {
		return this.dao.listAll();
	}

	
	@RequestMapping(value = "/{tipo}", method = RequestMethod.GET)
	public List<CrawlerLogVO> listByTipo(@PathVariable("tipo") String kind) {
		return this.dao.listByTipo(kind);
	}
}
