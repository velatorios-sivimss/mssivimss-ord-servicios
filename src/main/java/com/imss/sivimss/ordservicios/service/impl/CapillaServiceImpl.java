package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.Capilla;
import com.imss.sivimss.ordservicios.model.request.CapillaRequest;
import com.imss.sivimss.ordservicios.model.response.CapillaResponse;
import com.imss.sivimss.ordservicios.service.CapillaService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CapillaServiceImpl implements CapillaService{

	@Value("${endpoints.dominio-consulta}")
	private String urlConsulta;
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper modelMapper;
	
	private Capilla capilla=Capilla.getInstancia();
	
	public CapillaServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper = modelMapper;
	}


	@Override
	public Response<?> consultarCapilla(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson= new Gson();
		String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
		CapillaRequest capillaRequest=gson.fromJson(datosJson, CapillaRequest.class);
		List<CapillaResponse>capillaResponses;
		Response<?>response=providerServiceRestTemplate.consumirServicio(capilla.obtenerCapillas(capillaRequest.getIdVelatorio()).getDatos(), urlConsulta, authentication);
		if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
			capillaResponses= Arrays.asList(modelMapper.map(response.getDatos(), CapillaResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(capillaResponses));
		}
		return response;
	}

}
