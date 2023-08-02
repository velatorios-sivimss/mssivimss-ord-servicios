package com.imss.sivimss.ordservicios.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public interface ArticuloService {

	Response<Object>consultarAtaud(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object>consultarUrna(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object>consultarEmpaque(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object>consultarArticuloComplementario(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object>consultarArticuloComplementarioPorId(DatosRequest request, Authentication authentication) throws IOException;
}
