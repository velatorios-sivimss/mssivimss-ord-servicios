package com.imss.sivimss.ordservicios.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public interface SalaService {

	Response<?>consultarSalas(DatosRequest request, Authentication authentication) throws IOException;
}
