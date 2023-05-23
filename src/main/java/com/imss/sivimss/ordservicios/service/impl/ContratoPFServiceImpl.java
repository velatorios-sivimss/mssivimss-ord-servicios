package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.service.ContratoPFService;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class ContratoPFServiceImpl implements ContratoPFService{

	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper modelMapper;
	
	private final LogUtil logUtil;
	
	public ContratoPFServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper,
			LogUtil logUtil) {	
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper = modelMapper;
		this.logUtil = logUtil;
	}



	@Override
	public Response<Object> obtenerContratoPF(DatosRequest request, Authentication authentication) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
