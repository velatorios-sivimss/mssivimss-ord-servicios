package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.Paquete;
import com.imss.sivimss.ordservicios.model.request.PaquetesServiciosRequest;
import com.imss.sivimss.ordservicios.model.response.PaqueteResponse;
import com.imss.sivimss.ordservicios.model.response.ServicioResponse;
import com.imss.sivimss.ordservicios.service.PaqueteService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaqueteServiceImpl implements PaqueteService{

	@Value("${endpoints.dominio-consulta}")
	private String urlConsulta;
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper mapper;
	
	private final Paquete paquete=Paquete.getInstancia();
	
	public PaqueteServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper mapper) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.mapper = mapper;
	}

	@Override
	public Response<?> consultarPaquete(DatosRequest request, Authentication authentication) throws IOException {
		List<PaqueteResponse>paquetes;
		Response<?>response=providerServiceRestTemplate.consumirServicio(paquete.obtenerPaquete().getDatos(), urlConsulta, authentication);
		if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
			paquetes=Arrays.asList(mapper.map(response.getDatos(), PaqueteResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(paquetes));
		}
		return response;
	}

	@Override
	public Response<?> consultarServiciosPaquete(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson= new Gson();
		String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
		PaquetesServiciosRequest serviciosRequest= gson.fromJson(datosJson, PaquetesServiciosRequest.class);
		List<ServicioResponse>servicioResponses;
		Response<?>response=providerServiceRestTemplate.consumirServicio(paquete.obtenerServiciosPaquete(serviciosRequest.getIdPaquete()).getDatos(), urlConsulta, authentication);
		if (response.getCodigo() == 200 && !response.getDatos().toString().contains("[]")) {
			servicioResponses=Arrays.asList(mapper.map(response.getDatos(), ServicioResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(servicioResponses));
		}
		return response;
	}

}
