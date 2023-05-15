package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.repository.OrdenesDAO;
import com.imss.sivimss.ordservicios.service.OrdenServicioService;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

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
		switch (accion) {
		case "guardar":
			return ordenesDAO.agregarOrden(request, authentication);

		case "actualizar":
			return null;

		case "cambiar-estatus":
			return null;

		case "buscarOrden":
			return ordenesDAO.buscarOrden(request, authentication);

		case "detalleOrden":
			return ordenesDAO.detalleOrden(request, authentication);

		case "consultarOrdenes":
			return ordenesDAO.consultarOrdenesServicio(request, authentication);

		case "descargar":
			return null;

		case "buscarRfc":
			return ordenesDAO.buscarRfc(request, authentication);
			
		case "buscarCurp":
			return ordenesDAO.buscarCurp(request, authentication);
			
		default:
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "La peticion no se pudo realizar");
		}
	}

}
