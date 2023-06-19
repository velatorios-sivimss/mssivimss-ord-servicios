package com.imss.sivimss.ordservicios.repository;

import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.beans.ordeservicio.OrdenConsultar;
import com.imss.sivimss.ordservicios.beans.ordeservicio.OrdenGuardar;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;


@Service
public class OrdenesDAO {

	@Autowired
	private OrdenConsultar ordenConsultar;
	
	@Autowired
	private OrdenGuardar ordenGuardar;
	
	public Response<Object> agregarOrden(DatosRequest datosRequest, Authentication authentication) throws IOException, SQLException{
		return ordenGuardar.agregarOrden(datosRequest, authentication);
	}
	
	public Response<Object> actualizarOrden(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<Object> buscarOrden(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<Object> detalleOrden(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<Object> consultarOrdenesServicio(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<Object> descargarDocumentoOrdenServicio(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<Object> descargarDocumentoAceptacionDonacion(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<Object> descargarDocumentoContratoServiciosInmediatos(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<Object> descargarDocumentoControlSalidaDonacion(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<Object> buscarRfc(DatosRequest datosRequest, Authentication authentication) throws IOException {
		return ordenConsultar.buscarRfc(datosRequest,authentication);
	}
	
	public Response<Object> buscarCurp(DatosRequest datosRequest, Authentication authentication) throws IOException {
		return ordenConsultar.buscarCurp(datosRequest,authentication);
	}
}
