package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.sql.SQLException;

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
	public Response<Object> peticionOrden(DatosRequest request, Authentication authentication, String accion)
			throws IOException, SQLException {
		switch (accion) {
		case "guardar":
			return ordenesDAO.agregarOrden(request, authentication);

		case "actualizar":
			return ordenesDAO.actualizarOrden(request, authentication);

		case "cambiar-estatus":
			return null;

		case "buscarOrden":
			return ordenesDAO.buscarOrden(request, authentication);

		case "detalle-preorden":
			return ordenesDAO.detalleOrden(request, authentication);

		case "consultarOrdenes":
			return ordenesDAO.consultarOrdenesServicio(request, authentication);

		case "descargar":
			return null;

		case "buscarRfc":
			return ordenesDAO.buscarRfc(request, authentication);
			
		case "buscarCurp":
			return ordenesDAO.buscarCurp(request, authentication);
		//
		case "consultarVelatorio":
			return ordenesDAO.buscarVelatorio(request, authentication);
		case "consultarFolioODS":
			return ordenesDAO.buscarFolioODS(request, authentication);
		case "consultarContratante":
			return ordenesDAO.buscarContrante(request, authentication);
		case "consultarFinado":
			return ordenesDAO.buscarFinado(request, authentication);
		case "consultarTipoODS":
			return ordenesDAO.buscarTipoOrden(request, authentication);
		case "consultarUnidadMedica":
			return ordenesDAO.buscarUnidadMedica(request, authentication);
		case "consultarContratoConvenio":
			return ordenesDAO.buscarContratoConvenio(request, authentication);
		case "consultarEstadoODS":
			return ordenesDAO.buscarEstadoODS(request, authentication);
		case "generaTarjetaIdentificacion":
			return ordenesDAO.generaTarjetaIden(request, authentication);
		case "consultarODS":
			return ordenesDAO.buscarODS(request, authentication);
		case "cancelarODS":
			return ordenesDAO.cancelarODS(request, authentication);
		default:
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "La peticion no se pudo realizar");
		}
	}

}
