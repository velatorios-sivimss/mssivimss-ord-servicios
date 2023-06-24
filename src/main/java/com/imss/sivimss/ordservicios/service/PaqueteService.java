package com.imss.sivimss.ordservicios.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public interface PaqueteService {

	Response<Object>consultarPaquetes(DatosRequest request, Authentication authentication)throws IOException;
	Response<Object>consultarServiciosPaquete(DatosRequest request, Authentication authentication)throws IOException;
	Response<Object>consultarCaracteristicasPaquete(DatosRequest request, Authentication authentication)throws IOException;
	Response<Object>consultarTipoAsignacionAtaud(DatosRequest request, Authentication authentication)throws IOException;
	Response<Object>consultarAtaud(DatosRequest request, Authentication authentication)throws IOException;
	Response<Object>consultarProveedorAtaud(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object>consultarAtaudInventario(DatosRequest request, Authentication authentication) throws IOException;
}
