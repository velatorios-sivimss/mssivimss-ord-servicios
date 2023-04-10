package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.Panteon;
import com.imss.sivimss.ordservicios.model.request.PanteonRequest;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.model.response.PanteonResponse;
import com.imss.sivimss.ordservicios.service.PanteonService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PanteonServiceImpl implements PanteonService{
	
	@Value("${endpoints.dominio-crear-multiple}")
	private String urlCrearMultiple;

	@Value("${endpoints.dominio-crear}")
	private String urlCrear;
	
	@Value("${endpoints.dominio-consulta}")
	private String urlConsulta;
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;

	private Panteon panteon= new Panteon();
	
	private final ModelMapper modelMapper;
	
	public PanteonServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper=modelMapper;
	}

	@Override
	public Response<?> buscarPanteon(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson= new Gson();
        String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
		List<PanteonResponse>nombrePanteones;
		PanteonRequest panteonRequest=gson.fromJson(datosJson, PanteonRequest.class);
		Response<?>response=providerServiceRestTemplate.consumirServicio(panteon.buscar(panteonRequest).getDatos(), urlConsulta, authentication);
		if (response.getCodigo()==200) {
			nombrePanteones=Arrays.asList(modelMapper.map(response.getDatos(), PanteonResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(nombrePanteones));
		}
		return response;
	}

	@Override
	public Response<?> guardarPanteon(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson= new Gson();
        UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
		PanteonRequest panteonRequest= gson.fromJson(datosJson, PanteonRequest.class);
		
		return providerServiceRestTemplate.consumirServicio(panteon.insertar(panteonRequest ,usuarioDto).getDatos(), urlCrear,
				authentication);
	}

}
