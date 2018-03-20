package com.ctx.loteriaservice.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ctx.loteriaservice.dao.MegaSenaDAO;
import com.ctx.loteriaservice.vo.MegaSenaVO;

@RestController
@RequestMapping("/megasena")
public class MegaSenaService {
	
	private static final Logger log = LoggerFactory.getLogger(MegaSenaService.class);

	@Autowired
	private MegaSenaDAO dao;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<MegaSenaVO>> listAll() {
		List<MegaSenaVO> list = dao.listAll();
		return new ResponseEntity<List<MegaSenaVO>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/ultimo", method = RequestMethod.GET)
	public ResponseEntity<MegaSenaVO> loadUltimo() {
		MegaSenaVO vo = dao.loadUltimo();
		return new ResponseEntity<MegaSenaVO>(vo, HttpStatus.OK);
	}
	
	@RequestMapping(value="/concurso/{concurso}", method = RequestMethod.GET)
	public ResponseEntity<MegaSenaVO> load(@PathVariable("concurso") Long concurso) {
		MegaSenaVO vo = dao.loadByConcurso(concurso);
		return new ResponseEntity<MegaSenaVO>(vo, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<MegaSenaVO> insert(@RequestBody MegaSenaVO vo) {
		vo = dao.insert(vo);
		return new ResponseEntity<MegaSenaVO>(vo, HttpStatus.OK);
	}
	
	@RequestMapping(value="/batch", method = RequestMethod.POST)
	public ResponseEntity<List<MegaSenaVO>> insertBatch(@RequestBody List<MegaSenaVO> list) {
		if (list == null || list.isEmpty()) return new ResponseEntity<List<MegaSenaVO>>(new ArrayList<MegaSenaVO>(), HttpStatus.OK);
		
		int size = list.size();
		for (int i = 0; i < size; i += 500) {
			List<MegaSenaVO> subList = list.subList(i, Math.min(size, i + 500));
			this.dao.insert(subList);
		}
		return new ResponseEntity<List<MegaSenaVO>>(list, HttpStatus.OK);
	}
	
}
