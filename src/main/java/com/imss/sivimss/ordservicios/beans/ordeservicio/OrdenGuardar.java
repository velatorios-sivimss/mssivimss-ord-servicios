package com.imss.sivimss.ordservicios.beans.ordeservicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.model.request.OrdenesServicioRequest;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrdenGuardar {

	@Value("${endpoints.mod-catalogos}")
	private String urlCrear;
	
	@Autowired
	private ProviderServiceRestTemplate providerServiceRestTemplate;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	public Response<?> agregarOrden(DatosRequest datosRequest, Authentication authentication) {
		Gson gson= new Gson();
        UsuarioDto usuario = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		String datosJson=datosRequest.getDatos().get(AppConstantes.DATOS).toString();
		OrdenesServicioRequest ordenesServicioRequest=gson.fromJson(datosJson, OrdenesServicioRequest.class);
		return new Response<>(false, 200, AppConstantes.DATOS+usuario.getNombre());
	}
}
