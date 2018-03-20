package com.ctx.loteriaservice.vo;

import com.google.cloud.Timestamp;

public class JogoVO extends VO {

	private static final long serialVersionUID = 1L;
	public static final String PROP_NUMEROS = "numeros";
	public static final String PROP_CONCURSO = "concurso";
	public static final String PROP_DATA_PROXIMO_CONCURSO = "dataProximoConcurso";

	private String numeros;
	private Long concurso;
	private Timestamp dataProximoConcurso;

	public String getNumeros() {
		return numeros;
	}

	public void setNumeros(String numeros) {
		this.numeros = numeros;
	}

	public Long getConcurso() {
		return concurso;
	}

	public void setConcurso(Long concurso) {
		this.concurso = concurso;
	}
	
	public Timestamp getDataProximoConcurso() {
		return dataProximoConcurso;
	}

	public void setDataProximoConcurso(Timestamp dataProximoConcurso) {
		this.dataProximoConcurso = dataProximoConcurso;
	}

	@Override
	public String toString() {
		return "JogoVO [numeros=" + numeros + ", concurso=" + concurso + ", dataProximoConcurso=" + dataProximoConcurso
				+ "]";
	}
	
}