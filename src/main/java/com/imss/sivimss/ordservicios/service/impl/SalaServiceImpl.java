package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.Sala;
import com.imss.sivimss.ordservicios.model.request.SalaRequest;
import com.imss.sivimss.ordservicios.model.response.SalaResponse;
import com.imss.sivimss.ordservicios.service.SalaService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SalaServiceImpl implements SalaService{

	@Value("${endpoints.dominio-consulta}")
	private String urlConsultar;
	private Sala sala=Sala.getInstancia();
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper modelMapper;
	
	public SalaServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper=modelMapper;
	}

	@Override
	public Response<?> consultarSalas(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson= new Gson();
		String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
		SalaRequest salaRequest=gson.fromJson(datosJson, SalaRequest.class);
		Response<?> response= providerServiceRestTemplate.consumirServicio(sala.obtenerSala(salaRequest.getIdVelatorio()).getDatos(), urlConsultar, authentication);
		if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
			List<SalaResponse> salaResponse= Arrays.asList(modelMapper.map(response.getDatos(), SalaResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(salaResponse));
		}
		return response;
		
	}

}
