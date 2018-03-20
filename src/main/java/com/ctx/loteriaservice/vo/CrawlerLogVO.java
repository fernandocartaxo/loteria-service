package com.ctx.loteriaservice.vo;

import com.google.cloud.Timestamp;

/**
 * @author fcartaxo
 *
 */
public class CrawlerLogVO extends VO {

	private static final long serialVersionUID = 1L;
	public static final String KIND = "CrawlerLog";
	public static final String PROP_TIPO = "tipo";
	public static final String PROP_DATA_EXECUCAO = "dataExecucao";
	public static final String PROP_MENSAGEM = "mensagem";

	private String tipo;
	private Timestamp dataExecucao;
	private String mensagem;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Timestamp getDataExecucao() {
		return dataExecucao;
	}

	public void setDataExecucao(Timestamp dataExecucao) {
		this.dataExecucao = dataExecucao;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
