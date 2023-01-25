package com.enicom.nims.gate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.api.service.ApiService;
import com.enicom.nims.dao.ServiceUtil;

@Service(value = "gateService")
public class GateServiceImpl implements GateService {

	@Autowired
	private ServiceUtil serviceUtil;

	@Autowired
	private ApiService apiservice;


}
