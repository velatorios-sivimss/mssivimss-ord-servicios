package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.beans.ordeservicio.AdministrarOperacionODS;
import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.service.AdministradorOrdenServicioService;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class AdministrarOperacionODServiceImpl implements AdministradorOrdenServicioService{

	@Autowired
	private AdministrarOperacionODS administrarOperacionODS;
	
	@Override
	public Response<Object> peticionOrden(DatosRequest request, Authentication authentication, String accion)
			throws IOException, SQLException {
		switch (accion) {
		case "consultar-admin":
			return administrarOperacionODS.consultarHistorial(request, authentication);
		case "consultar-tipo-servicio":
			return administrarOperacionODS.consultarTipoServicio(request, authentication);
		case "situar-servicio":
			return administrarOperacionODS.guardarSituarServicio(request, authentication);
        case "quitar-servicio":
			return administrarOperacionODS.desactivarSituarServicio(request, authentication);
		default:
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "La petición no se pudo realizar.");
		}
		
	}

}
