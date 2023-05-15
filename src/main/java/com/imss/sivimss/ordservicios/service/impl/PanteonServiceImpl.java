package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
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
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;


@Service
public class PanteonServiceImpl implements PanteonService{
	
	@Value("${endpoints.dominio-consulta}")
	private String urlDominio;
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;

	private Panteon panteon= new Panteon();
	
	private final ModelMapper modelMapper;
	
	private static final String AGREGADO_CORRECTAMENTE = "99";
	
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
		Response<?>response=providerServiceRestTemplate.consumirServicio(panteon.buscar(panteonRequest).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
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
		
		return MensajeResponseUtil.mensajeResponse( providerServiceRestTemplate.consumirServicio(panteon.insertar(panteonRequest ,usuarioDto).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CREAR_MULTIPLE),
				authentication),AGREGADO_CORRECTAMENTE);
	}

}
