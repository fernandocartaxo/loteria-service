package com.ctx.loteriaservice.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthCheckService {

	@RequestMapping(method = RequestMethod.GET)
	public String healthcheck() {
		return "ok";
	}

}