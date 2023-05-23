package com.imss.sivimss.ordservicios.repository;

import java.io.IOException;

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
	
	public Response<?> agregarOrden(DatosRequest datosRequest, Authentication authentication) throws IOException{
		return ordenGuardar.agregarOrden(datosRequest, authentication);
	}
	
	public Response<?> actualizarOrden(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<?> buscarOrden(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<?> detalleOrden(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<?> consultarOrdenesServicio(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<?> descargarDocumentoOrdenServicio(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<?> descargarDocumentoAceptacionDonacion(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<?> descargarDocumentoContratoServiciosInmediatos(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<?> descargarDocumentoControlSalidaDonacion(DatosRequest datosRequest, Authentication authentication) {
		return null;
	}
	
	public Response<?> buscarRfc(DatosRequest datosRequest, Authentication authentication) throws IOException {
		return ordenConsultar.buscarRfc(datosRequest,authentication);
	}
	
	public Response<?> buscarCurp(DatosRequest datosRequest, Authentication authentication) throws IOException {
		return ordenConsultar.buscarCurp(datosRequest,authentication);
	}
}
