package com.imss.sivimss.ordservicios.service;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.Response;

public interface OrdenServicioService {

	Response<?>peticionOrden(DatosRequest request, Authentication authentication, String accion)throws IOException, SQLException;
}
