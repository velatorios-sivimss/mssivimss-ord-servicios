package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.service.CatalogoService;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public class CatalogoServiceImpl implements CatalogoService{

	@Override
	public Response<?> peticionOrden(DatosRequest request, Authentication authentication, String accion)
			throws IOException {
	
		switch (accion) {
		case "convenio":
			return null;
		default:
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "La peticion no se realizo.");
		}
	}

}
