package com.imss.sivimss.ordservicios.repository;

import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrdenesDAO {

	public Response<?> agregarOrden(DatosRequest datosRequest, UsuarioDto usuario) {
		OrdenGuardar guardar= new OrdenGuardar();
		return guardar.agregarOrden(datosRequest, usuario);
	}
	
	public Response<?> actualizarOrden(DatosRequest datosRequest, UsuarioDto usuario) {
		return null;
	}
	
	public Response<?> consultar(DatosRequest datosRequest, UsuarioDto usuario) {
		return null;
	}
	
	public Response<?> descargarDocumentoOrden(DatosRequest datosRequest, UsuarioDto usuario) {
		return null;
	}
}
