package com.imss.sivimss.ordservicios.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrdenGuardar {

	@Value("${endpoints.dominio-crear}")
	private String urlCrear;
	
	@Autowired
	private ProviderServiceRestTemplate providerServiceRestTemplate;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	public Response<?> agregarOrden(DatosRequest datosRequest, UsuarioDto usuario) {
		return new Response<>(false, 200, AppConstantes.DATOS+usuario.getNombre());
	}
}
