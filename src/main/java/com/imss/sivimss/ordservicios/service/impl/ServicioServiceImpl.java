package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.Servicio;
import com.imss.sivimss.ordservicios.model.request.ProveedorServicioRequest;
import com.imss.sivimss.ordservicios.model.response.ProveedorServicioResponse;
import com.imss.sivimss.ordservicios.model.response.ServicioResponse;
import com.imss.sivimss.ordservicios.service.ServicioService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;


@Service
public class ServicioServiceImpl implements ServicioService{

	@Value("${endpoints.dominio-consulta}")
	private String urlDominio;
	
	private Servicio servicio=Servicio.getInstancia();
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper modelMapper;
	
	public ServicioServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper = modelMapper;
	}

	
	@Override
	public Response<?> consultarProvedorServicios(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson= new Gson();
		String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
		ProveedorServicioRequest proveedorServicioRequest=gson.fromJson(datosJson, ProveedorServicioRequest.class);
		List<ProveedorServicioResponse>proveedorResponses;
		Response<?>response=providerServiceRestTemplate.consumirServicio(servicio.obtenerProveedorServicio(proveedorServicioRequest.getIdServicio()).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
		if (response.getCodigo()== 200 && !response.getDatos().toString().contains("[]")) {
			proveedorResponses=Arrays.asList(modelMapper.map(response.getDatos(), ProveedorServicioResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(proveedorResponses));
		}
		
		return response;
	}

	@Override
	public Response<?> consultarServiciosVigentes(DatosRequest request, Authentication authentication)
			throws IOException {
		List<ServicioResponse>servicioResponses;
		Response<?>response=providerServiceRestTemplate.consumirServicio(servicio.obtenerServiciosVigentes().getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication) ;
		if (response.getCodigo()==200 && response.getDatos().toString().contains("[]")) {
			servicioResponses=Arrays.asList(modelMapper.map(response.getDatos(), ServicioResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(servicioResponses));
		}
		return response;
	}

}
