package com.ctx.loteriaservice.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctx.loteriaservice.vo.JogoVO;
import com.google.cloud.Timestamp;

public abstract class Crawler<T extends JogoVO> {

	private static final Logger log = LoggerFactory.getLogger(Crawler.class);
	
	private String url;
	private Pattern patternUrlBase = Pattern.compile("base href=\"(.*?)\">", Pattern.DOTALL);
	private Pattern patternUrlBuscaResultado = Pattern.compile("input type=\"hidden\" value=\"(.*?)\"\\s*ng-model=\"urlBuscarResultado\" id=\"urlBuscarResultado\"", Pattern.DOTALL);
	private Pattern patternNumeros = Pattern.compile("resultado\":\"(.*?)\"", Pattern.DOTALL);
	private Pattern patternConcurso = Pattern.compile("concurso\":(.*?),", Pattern.DOTALL);
	private Pattern patternDataProximoConcurso = Pattern.compile("dt_proximo_concurso\":(.*?),", Pattern.DOTALL);

	public Crawler(String url) {
		this.url = url;
	}
	
	protected String getBuscarUrlResultado() {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(this.url);
		log.info("call " + this.url);
		BufferedReader rd = null;
		try {
			HttpResponse response = client.execute(request);
			log.info("Response Code : " + response.getStatusLine().getStatusCode());
			rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			line = result.toString();
			String urlServico = "";
			Matcher m = patternUrlBase.matcher(line);
			if (m.find()) {
				urlServico += m.group(1);
			}
			m = patternUrlBuscaResultado.matcher(line);
			if (m.find()) {
				urlServico += m.group(1) + "?timestampAjax=" + new Date().getTime();
			}
			return urlServico;
		} catch (Exception e) {
			log.error("erro ao fazer o crawler", e);
		} finally {
			if (rd != null) try { rd.close(); } catch (Exception e) {}	 
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public T craw() {
		String urlResultado = getBuscarUrlResultado();
		if (urlResultado == null) return null;
		log.info("call " + urlResultado);
		HttpGet request = new HttpGet(urlResultado);
		
		HttpClient client = HttpClientBuilder.create().build();
		
		BufferedReader rd = null;
		JogoVO vo = new JogoVO();
		try {
			HttpResponse response = client.execute(request);
	
			log.info("Response Code : " + response.getStatusLine().getStatusCode());
	
			rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			line = result.toString();
			Matcher m = patternNumeros.matcher(line);
			if (m.find()) {
				List<String> list = new ArrayList<String>(6);
				Stream.of(m.group(1).split("-")).forEach(n -> list.add(n));
				Collections.sort(list);
				StringBuilder nums = new StringBuilder(17);
				list.stream().forEach(n -> {if (nums.length() > 0) nums.append("-"); nums.append(n);});
				vo.setNumeros(nums.toString());
			}
			m = patternConcurso.matcher(line);
			if (m.find()) {
				vo.setConcurso(Long.valueOf(m.group(1)));
			}
			m = patternDataProximoConcurso.matcher(line);
			if (m.find()) {
				vo.setDataProximoConcurso(Timestamp.ofTimeMicroseconds(Long.valueOf(m.group(1))));
			}
		} catch (Exception e) {
			log.error("erro ao fazer o crawler", e);
		} finally {
			if (rd != null) try { rd.close(); } catch (Exception e) {}	 
		}
		
		return (T) vo;
	}

}