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
import com.imss.sivimss.ordservicios.beans.Paquete;
import com.imss.sivimss.ordservicios.model.request.PaqueteRequest;
import com.imss.sivimss.ordservicios.model.response.PaqueteCaracteristicas;
import com.imss.sivimss.ordservicios.model.response.PaqueteResponse;
import com.imss.sivimss.ordservicios.model.response.ServicioResponse;
import com.imss.sivimss.ordservicios.service.PaqueteService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;


@Service
public class PaqueteServiceImpl implements PaqueteService{

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper mapper;
	
	private final Paquete paquete=Paquete.getInstancia();
	
	private final LogUtil logUtil;
	
	
	private static final Logger log = LoggerFactory.getLogger(PaqueteServiceImpl.class);

	
	public PaqueteServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper mapper, LogUtil logUtil) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.mapper = mapper;
		this.logUtil=logUtil;
	}

	@Override
	public Response<Object> consultarPaquetes(DatosRequest request, Authentication authentication) throws IOException {
		Response<Object>response;
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarPaquetes", AppConstantes.CONSULTA, authentication);

			List<PaqueteResponse>paquetes;
			response=providerServiceRestTemplate.consumirServicio(paquete.obtenerPaquetes().getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
				paquetes=Arrays.asList(mapper.map(response.getDatos(), PaqueteResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(paquetes));
			}
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = paquete.obtenerPaquetes().getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
	}

	@Override
	public Response<Object> consultarServiciosPaquete(DatosRequest request, Authentication authentication)
			throws IOException {
		Response<Object>response;
		PaqueteRequest serviciosRequest= new PaqueteRequest();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarServiciosPaquete", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
			serviciosRequest= gson.fromJson(datosJson, PaqueteRequest.class);
			List<ServicioResponse>servicioResponses;
			response=providerServiceRestTemplate.consumirServicio(paquete.obtenerServiciosPaquete(serviciosRequest.getIdPaquete()).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			if (response.getCodigo() == 200 && !response.getDatos().toString().contains("[]")) {
				servicioResponses=Arrays.asList(mapper.map(response.getDatos(), ServicioResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(servicioResponses));
			}
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta =paquete.obtenerServiciosPaquete(serviciosRequest.getIdPaquete()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
		
	}

	@Override
	public Response<Object> consultarCaracteristicasPaquete(DatosRequest request, Authentication authentication)
			throws IOException {
		Response<Object>response;
		PaqueteRequest serviciosRequest= new PaqueteRequest();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarCaracteristicasPaquete", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
			serviciosRequest= gson.fromJson(datosJson, PaqueteRequest.class);
			List<PaqueteCaracteristicas>paqueteCaracteristicas;
			response=providerServiceRestTemplate.consumirServicio(paquete.obtenerCaracteristicasPaquete(serviciosRequest.getIdPaquete()).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			if (response.getCodigo() == 200 && !response.getDatos().toString().contains("[]")) {
				paqueteCaracteristicas=Arrays.asList(mapper.map(response.getDatos(), PaqueteCaracteristicas[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(paqueteCaracteristicas));
			}
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = paquete.obtenerCaracteristicasPaquete(serviciosRequest.getIdPaquete()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
		
	}

	@Override
	public Response<?> consultarAtaudTipoAsignacionPaquete(DatosRequest request, Authentication authentication)
			throws IOException {
		Response<Object>response;
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	

}
