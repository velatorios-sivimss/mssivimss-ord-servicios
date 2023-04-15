package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.service.ArticuloService;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public class ArticuloServiceImpl implements ArticuloService{

	@Override
	public Response<?> consultarAtaud(DatosRequest request, Authentication authentication) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<?> consultarUrna(DatosRequest request, Authentication authentication) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<?> consultarEmpaque(DatosRequest request, Authentication authentication) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<?> consultarArticuloComplementario(DatosRequest request, Authentication authentication)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
