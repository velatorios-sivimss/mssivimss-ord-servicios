package com.imss.sivimss.ordservicios.service;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public interface GeneraReporteService {

	Response<Object> generarDocumentoContratoServInmediato(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> generarReporteSalidaDonacion(DatosRequest request, Authentication authentication) throws IOException, SQLException;

}
