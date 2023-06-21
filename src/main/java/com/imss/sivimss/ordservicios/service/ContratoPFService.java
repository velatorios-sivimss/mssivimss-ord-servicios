package com.imss.sivimss.ordservicios.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public interface ContratoPFService {

	Response<Object>obtenerContratoPF(DatosRequest request,Authentication authentication)throws IOException;
}
