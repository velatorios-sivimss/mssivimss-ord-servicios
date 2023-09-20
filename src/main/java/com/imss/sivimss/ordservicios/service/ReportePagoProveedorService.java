package com.imss.sivimss.ordservicios.service;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public interface ReportePagoProveedorService {

	Response<?> generarReportePagoProveedor(DatosRequest request, Authentication authentication) throws IOException, ParseException;

}
