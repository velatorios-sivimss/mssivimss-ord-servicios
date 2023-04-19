package com.imss.sivimss.ordservicios.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public interface ArticuloService {

	Response<?>consultarAtaud(DatosRequest request, Authentication authentication) throws IOException;
	Response<?>consultarUrna(DatosRequest request, Authentication authentication) throws IOException;
	Response<?>consultarEmpaque(DatosRequest request, Authentication authentication) throws IOException;
	Response<?>consultarArticuloComplementario(DatosRequest request, Authentication authentication) throws IOException;
	Response<?>consultarArticuloComplementarioPorId(DatosRequest request, Authentication authentication) throws IOException;
}
