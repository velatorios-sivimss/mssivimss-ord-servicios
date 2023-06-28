package com.imss.sivimss.ordservicios.service;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public interface ContratoPFService {

	Response<Object>obtenerContratoPF(DatosRequest request,Authentication authentication)throws IOException, SQLException;
	Response<Object>obtenerContratantes (DatosRequest request,Authentication authentication)throws IOException, SQLException;
	Response<Object>obtenerContratanteBeneficiarios (DatosRequest request,Authentication authentication)throws IOException, SQLException;
}
