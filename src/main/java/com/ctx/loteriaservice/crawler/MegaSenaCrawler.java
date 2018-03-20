package com.ctx.loteriaservice.crawler;

import com.ctx.loteriaservice.vo.MegaSenaVO;

public class MegaSenaCrawler extends Crawler<MegaSenaVO> {

	
	public MegaSenaCrawler() {
		super("http://loterias.caixa.gov.br/wps/portal/loterias/landing/megasena/");
	}

}
