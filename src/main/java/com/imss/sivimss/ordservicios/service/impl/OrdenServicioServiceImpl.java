package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.repository.OrdenesDAO;
import com.imss.sivimss.ordservicios.service.OrdenServicioService;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrdenServicioServiceImpl implements OrdenServicioService {

	private final OrdenesDAO ordenesDAO;

	public OrdenServicioServiceImpl(OrdenesDAO ordenesDAO) {
		super();
		this.ordenesDAO = ordenesDAO;
	}

	@Override
	public Response<?> peticionOrden(DatosRequest request, Authentication authentication, String accion)
			throws IOException {
		Gson gson= new Gson();
        UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		switch (accion) {
		case "guardar":
			return ordenesDAO.agregarOrden(request, usuarioDto);
			
		case "actualizar":
			return null;
		
		case "cambiar-estatus":
			
			return null;
			
		case "consultar":
			return null;
			

		case "descargar":
			return null;
		default:
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "La peticion no se pudo realizar");
		}
	}

}
