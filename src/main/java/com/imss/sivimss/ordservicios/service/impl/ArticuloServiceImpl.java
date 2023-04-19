package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.ArticuloComplementario;
import com.imss.sivimss.ordservicios.beans.Ataud;
import com.imss.sivimss.ordservicios.beans.Empaque;
import com.imss.sivimss.ordservicios.beans.Urna;
import com.imss.sivimss.ordservicios.model.request.ArticuloComplementarioRequest;
import com.imss.sivimss.ordservicios.model.response.ArticuloComplementarioResponse;
import com.imss.sivimss.ordservicios.model.response.ArticuloFunerarioResponse;
import com.imss.sivimss.ordservicios.service.ArticuloService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticuloServiceImpl implements ArticuloService{
	
	@Value("${endpoints.dominio-consulta}")
	private String urlConsultar;

	private final Ataud ataud=Ataud.obtenerInstancia();
	
	private final Urna urna=Urna.obtenerInstancia();

	private final Empaque empaque=Empaque.obtenerInstancia();
	
	private final ArticuloComplementario articuloComplementario= ArticuloComplementario.getInstancia();
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper modelMapper;
	
	private static final String SIN_EXISTENCIA = "15";
	
	public ArticuloServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper = modelMapper;
	}

	@Override
	public Response<?> consultarAtaud(DatosRequest request, Authentication authentication) throws IOException {
		
		List<ArticuloFunerarioResponse>articuloFunerarioResponses;
		Response<?>response=providerServiceRestTemplate.consumirServicio(ataud.obtenerAtaudes().getDatos(), urlConsultar, authentication);
		
		if (response.getCodigo()==200) {
			if(!response.getDatos().toString().contains("[]")) {
				articuloFunerarioResponses= Arrays.asList(modelMapper.map(response.getDatos(), ArticuloFunerarioResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloFunerarioResponses));
			}else {
				response.setMensaje(SIN_EXISTENCIA);
			}
		}
		
		return response;
	}

	@Override
	public Response<?> consultarUrna(DatosRequest request, Authentication authentication) throws IOException {
		
		
		List<ArticuloFunerarioResponse>articuloFunerarioResponses;
		Response<?>response=providerServiceRestTemplate.consumirServicio(urna.obtenerUrna().getDatos(), urlConsultar, authentication);
		if (response.getCodigo()==200) {
			if (!response.getDatos().toString().contains("[]")) {
				articuloFunerarioResponses=Arrays.asList(modelMapper.map(response.getDatos(), ArticuloFunerarioResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloFunerarioResponses));
			} else {
				response.setMensaje(SIN_EXISTENCIA);
			}
		}
		return response;
	}

	@Override
	public Response<?> consultarEmpaque(DatosRequest request, Authentication authentication) throws IOException {
		
		List<ArticuloFunerarioResponse>articuloFunerarioResponses;
		Response<?>response=providerServiceRestTemplate.consumirServicio(empaque.obtenerEmpaque().getDatos(), urlConsultar, authentication);
		if (response.getCodigo()==200) {
			if (!response.getDatos().toString().contains("[]")) {
				articuloFunerarioResponses=Arrays.asList(modelMapper.map(response.getDatos(), ArticuloFunerarioResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloFunerarioResponses));
			} else {
				response.setMensaje(SIN_EXISTENCIA);
			}
		}
		return response;
	}

	@Override
	public Response<?> consultarArticuloComplementario(DatosRequest request, Authentication authentication)
			throws IOException {
		List<ArticuloComplementarioResponse>articuloFunerarioResponses;
		Response<?>response=providerServiceRestTemplate
				.consumirServicio(articuloComplementario.obtenerArticulosComplementarios().getDatos(), urlConsultar, authentication);
		if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
				articuloFunerarioResponses=Arrays.asList(modelMapper.map(response.getDatos(), ArticuloComplementarioResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloFunerarioResponses));
			}
		
		return response;
	}

	@Override
	public Response<?> consultarArticuloComplementarioPorId(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson= new Gson();
		String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
		ArticuloComplementarioRequest articuloComplementarioRequest=gson.fromJson(datosJson, ArticuloComplementarioRequest.class);
		List<ArticuloComplementarioResponse>articuloFunerarioResponses;
		Response<?>response=providerServiceRestTemplate
				.consumirServicio(articuloComplementario.obtenerArticulosComplementariosPorId(articuloComplementarioRequest.getIdArticulo()).getDatos(), urlConsultar, authentication);
		if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
				articuloFunerarioResponses=Arrays.asList(modelMapper.map(response.getDatos(), ArticuloComplementarioResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloFunerarioResponses));
			}
		
		return response;
	}

}
