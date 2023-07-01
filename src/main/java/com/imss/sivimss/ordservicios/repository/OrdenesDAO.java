package com.imss.sivimss.ordservicios.repository;

import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.beans.ordeservicio.OrdenActualizar;
import com.imss.sivimss.ordservicios.beans.ordeservicio.OrdenConsultar;
import com.imss.sivimss.ordservicios.beans.ordeservicio.OrdenGuardar;
import com.imss.sivimss.ordservicios.service.GeneraReporteService;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;


@Service
public class OrdenesDAO {

	@Autowired
	private OrdenConsultar ordenConsultar;
	
	@Autowired
	private OrdenGuardar ordenGuardar;
	
		@Autowired
	private GeneraReporteService generaReporteService;
	
	@Autowired
	private OrdenActualizar ordenActualizar;
	
	public Response<Object> agregarOrden(DatosRequest datosRequest, Authentication authentication) throws IOException, SQLException{
		return ordenGuardar.agregarOrden(datosRequest, authentication);
	}
	
	public Response<Object> actualizarOrden(DatosRequest datosRequest, Authentication authentication) throws IOException, SQLException {
		return ordenActualizar.actualizar(datosRequest, authentication);
	}
	
	public Response<Object> buscarOrden(DatosRequest datosRequest, Authentication authentication) throws IOException {
		return ordenConsultar.buscarOrdenServicio(datosRequest, authentication);
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
	//
	public Response<Object> buscarVelatorio(DatosRequest datosRequest, Authentication authentication) throws IOException{
		return ordenConsultar.buscarVelatorios(datosRequest, authentication);
	}
	public Response<Object> buscarFolioODS(DatosRequest datosRequest, Authentication authentication) throws IOException{
		return ordenConsultar.buscarFolioODS(datosRequest, authentication);
	}	
	public Response<Object> buscarContrante(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.buscarContratante(datos, auth);
	}	
	public Response<Object> buscarFinado(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.buscarFinado(datos, auth);
	}	
	public Response<Object> buscarTipoOrden(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.buscarTipoOrden(datos, auth);
	}	
	public Response<Object> buscarUnidadMedica(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.buscarUnidadMedica(datos, auth);
	}	
	public Response<Object> buscarContratoConvenio(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.buscarContratoConvenio(datos, auth);
	}	
	public Response<Object> buscarEstadoODS(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.buscarEstadoODS(datos, auth);
	}	
	public Response<Object> generaTarjetaIden(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.generaTarjetaIden(datos, auth);
	}	
	public Response<Object> buscarODS(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.buscarODS(datos, auth);
	}	
	public Response<Object> cancelarODS(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.cancelarODS(datos, auth);
	}

	public Response<Object> generaReporteConsultaODS(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.generaReporteConsultaODS(datos, auth);
	}
	public Response<Object> generaReporteServicioInmediato(DatosRequest datos, Authentication auth) throws IOException{
		return generaReporteService.generarDocumentoContratoServInmediato(datos, auth);
	}
	public Response<Object> generaReporteOrdenServicio(DatosRequest datos, Authentication auth) throws IOException{
		return ordenConsultar.generarDocumentoOrdenServicio(datos, auth);
	}
}
