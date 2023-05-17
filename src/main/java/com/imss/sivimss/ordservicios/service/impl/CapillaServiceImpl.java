package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.Capilla;
import com.imss.sivimss.ordservicios.model.request.CapillaRequest;
import com.imss.sivimss.ordservicios.model.response.CapillaResponse;
import com.imss.sivimss.ordservicios.service.CapillaService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;




@Service
public class CapillaServiceImpl implements CapillaService{

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;
	
	private final LogUtil logUtil;
	
	
	private static final Logger log = LoggerFactory.getLogger(CapillaServiceImpl.class);

	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper modelMapper;
	
	private Capilla capilla=Capilla.getInstancia();
	
	public CapillaServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper, LogUtil logUtil) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper=modelMapper;
		this.logUtil=logUtil;
	}


	@Override
	public Response<Object> consultarCapilla(DatosRequest request, Authentication authentication) throws IOException {
		Response<Object>response;
		CapillaRequest capillaRequest= new CapillaRequest();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarCapilla", AppConstantes.CONSULTA, authentication);
			Gson gson= new Gson();
			String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
			capillaRequest=gson.fromJson(datosJson, CapillaRequest.class);
			List<CapillaResponse>capillaResponses;
			response=providerServiceRestTemplate.consumirServicio(capilla.obtenerCapillas(capillaRequest.getIdVelatorio()).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
				capillaResponses= Arrays.asList(modelMapper.map(response.getDatos(), CapillaResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(capillaResponses));
			}
			
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = capilla.obtenerCapillas(capillaRequest.getIdVelatorio()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
		
	}

}
