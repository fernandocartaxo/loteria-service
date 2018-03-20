package com.ctx.loteriaservice.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ctx.loteriaservice.crawler.Crawler;
import com.ctx.loteriaservice.crawler.MegaSenaCrawler;
import com.ctx.loteriaservice.dao.CrawlerLogDAO;
import com.ctx.loteriaservice.dao.MegaSenaDAO;
import com.ctx.loteriaservice.vo.CrawlerLogVO;
import com.ctx.loteriaservice.vo.JogoVO;
import com.ctx.loteriaservice.vo.MegaSenaVO;
import com.google.cloud.Timestamp;

@Component
public class MegaSenaTask {

	private static final Logger log = LoggerFactory.getLogger(MegaSenaTask.class);
	
	@Autowired
	private MegaSenaDAO megaSenaDAO;
	@Autowired
	private CrawlerLogDAO crawlerLogDAO;

	@Scheduled(fixedRate = 1000 * 60 * 60)
	public void craw() {
		Crawler<MegaSenaVO> crawler = new MegaSenaCrawler();
		String mensagem = null;
		try {
			JogoVO novoJogo = crawler.craw();
			if (novoJogo != null && novoJogo.getConcurso() != null) {
				log.info(novoJogo.toString());
				MegaSenaVO vo = this.megaSenaDAO.loadByConcurso(novoJogo.getConcurso());
				if (vo == null) {
					vo = new MegaSenaVO();
					vo.setConcurso(novoJogo.getConcurso());
					vo.setNumeros(novoJogo.getNumeros());
					vo.setDataProximoConcurso(novoJogo.getDataProximoConcurso());
					this.megaSenaDAO.insert(vo);
					log.info("inseriu novo jogo");
				}
			}
		} catch (Exception e) {
			log.error("erro ao buscar os jogos", e);
			mensagem = e.getMessage();
		} finally {
			CrawlerLogVO crawlerLog = new CrawlerLogVO();
			crawlerLog.setTipo(MegaSenaVO.KIND);
			crawlerLog.setMensagem(mensagem);
			crawlerLog.setDataExecucao(Timestamp.of(new Date()));
			this.crawlerLogDAO.insert(crawlerLog);
		}
	}

}
