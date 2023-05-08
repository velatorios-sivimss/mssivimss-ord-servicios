package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.CodigoPostal;
import com.imss.sivimss.ordservicios.model.request.CodigoPostalRequest;
import com.imss.sivimss.ordservicios.model.response.CodigoPostalResponse;
import com.imss.sivimss.ordservicios.service.CodigoPostalService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;


@Service
public class CodigoPostalServiceImpl implements CodigoPostalService{
	
	@Value("${endpoints.dominio-consulta}")
	private String urlConsulta;
	
	private CodigoPostal codigoPostal=CodigoPostal.getInstancia();
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	
	private final ModelMapper modelMapper;
	
	public CodigoPostalServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper=modelMapper;
	}



	@Override
	public Response<?> buscarCodigoPostal(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson= new Gson();
		String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
		List<CodigoPostalResponse> codigoPostalesResponse;
		CodigoPostalRequest codigoPostalDto=gson.fromJson(datosJson, CodigoPostalRequest.class);
		Response<?> response= providerServiceRestTemplate.consumirServicio(codigoPostal.buscar(codigoPostalDto.getCodigoPostal()).getDatos(), urlConsulta, authentication);
		if (response.getCodigo() == 200) {
			codigoPostalesResponse=Arrays.asList(modelMapper.map(response.getDatos(), CodigoPostalResponse[].class));
			
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(codigoPostalesResponse));
		}
		return response;
	}
	
	

}
