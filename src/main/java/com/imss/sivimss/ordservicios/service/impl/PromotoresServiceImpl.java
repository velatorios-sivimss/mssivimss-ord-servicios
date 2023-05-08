package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.beans.Promotor;
import com.imss.sivimss.ordservicios.model.response.PromotorResponse;
import com.imss.sivimss.ordservicios.service.PromotorService;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;


@Service
public class PromotoresServiceImpl implements PromotorService{

	@Value("${endpoints.dominio-consulta}")
	private String urlConsultar;
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper modelMapper;
	
	private final Promotor promotor=Promotor.getInstancia();
	
	public PromotoresServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper) {
		super();
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper = modelMapper;
	}

	@Override
	public Response<?> consultarPromotores(DatosRequest request, Authentication authentication) throws IOException {
		Response<?>response=providerServiceRestTemplate.consumirServicio(promotor.consultarPromotores().getDatos(), urlConsultar, authentication);
		List<PromotorResponse>promotorResponses;
		if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
			promotorResponses=Arrays.asList(modelMapper.map(response.getDatos(), PromotorResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(promotorResponses));
		}
		return response;
	}

}
