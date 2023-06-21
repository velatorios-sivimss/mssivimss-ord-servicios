package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class SalaServiceImpl implements SalaService {

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;

	private Sala sala = Sala.getInstancia();

	private final ProviderServiceRestTemplate providerServiceRestTemplate;

	private final LogUtil logUtil;

	private final ModelMapper modelMapper;
	
	
	private static final Logger log = LoggerFactory.getLogger(SalaServiceImpl.class);


	public SalaServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper,
			LogUtil logUtil) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper = modelMapper;
		this.logUtil = logUtil;
	}

	@Override
	public Response<Object> consultarSalas(DatosRequest request, Authentication authentication) throws IOException {
		Response<Object> response;
		SalaRequest salaRequest= new SalaRequest();
		
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarSalas", AppConstantes.CONSULTA, authentication);
            Gson gson = new Gson();
			String datosJson = request.getDatos().get(AppConstantes.DATOS).toString();
			salaRequest = gson.fromJson(datosJson, SalaRequest.class);
			response = providerServiceRestTemplate.consumirServicio(
					sala.obtenerSala(salaRequest.getIdVelatorio()).getDatos(),
					urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			if (response.getCodigo() == 200 && !response.getDatos().toString().contains("[]")) {
				List<SalaResponse> salaResponse = Arrays
						.asList(modelMapper.map(response.getDatos(), SalaResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(salaResponse));
			}
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = sala.obtenerSala(salaRequest.getIdVelatorio()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}

	}

}
