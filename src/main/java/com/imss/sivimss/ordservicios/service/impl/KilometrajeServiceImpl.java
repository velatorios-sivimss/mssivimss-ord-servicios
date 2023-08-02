package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.Kilometraje;
import com.imss.sivimss.ordservicios.model.request.KilometrajeRequest;
import com.imss.sivimss.ordservicios.model.request.PaqueteRequest;
import com.imss.sivimss.ordservicios.service.KilometrajeService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class KilometrajeServiceImpl implements KilometrajeService{
	
	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;

	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper mapper;
	
	private final LogUtil logUtil;
	
	private final Kilometraje kilometraje= Kilometraje.getInstancia();
	
	private static final Logger log = LoggerFactory.getLogger(KilometrajeServiceImpl.class);

	
	public KilometrajeServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper mapper,
			LogUtil logUtil) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.mapper = mapper;
		this.logUtil = logUtil;
	}

	@Override
	public Response<Object> consultarKilometrajePorPaquete(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson= new Gson();
		String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
		PaqueteRequest paqueteRequest= new PaqueteRequest();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarKilometrajePorPaquete", AppConstantes.CONSULTA, authentication);
            paqueteRequest=gson.fromJson(datosJson, PaqueteRequest.class);
            Response<Object>response=providerServiceRestTemplate.consumirServicio(kilometraje.obtenerKilometrajePorPaquete(paqueteRequest.getIdPaquete(),paqueteRequest.getIdProveedor()).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
            return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = kilometraje.obtenerKilometrajePorPaquete(paqueteRequest.getIdPaquete(),paqueteRequest.getIdProveedor()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        log.error(e.getMessage());
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
	}

	@Override
	public Response<Object> consultarKilometrajePorServicio(DatosRequest request, Authentication authentication)
			throws IOException {
		Response<Object>response;
		KilometrajeRequest kilometrajeRequest= new KilometrajeRequest();
		Gson gson= new Gson();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarKilometrajePorServicio", AppConstantes.CONSULTA, authentication);
            String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
            kilometrajeRequest= gson.fromJson(datosJson, KilometrajeRequest.class);
            response=providerServiceRestTemplate.consumirServicio(kilometraje.obtenerKilometrajePorServicio(kilometrajeRequest.getIdServicio(), kilometrajeRequest.getIdProveedor()).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
            return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = kilometraje.obtenerKilometrajePorServicio(kilometrajeRequest.getIdServicio(),kilometrajeRequest.getIdProveedor()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        log.error(e.getMessage());
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
	}

}
