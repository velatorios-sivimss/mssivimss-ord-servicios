package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.service.KilometrajeService;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class KilometrajeServiceImpl implements KilometrajeService{

	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper mapper;
	
	private final LogUtil logUtil;
	
	public KilometrajeServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper mapper,
			LogUtil logUtil) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.mapper = mapper;
		this.logUtil = logUtil;
	}

	@Override
	public Response<Object> consultarKilometrajePorPaquete(DatosRequest request, Authentication authentication)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<Object> consultarKilometrajePorServicio(DatosRequest request, Authentication authentication)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
