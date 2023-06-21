package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.Ataud;
import com.imss.sivimss.ordservicios.beans.Paquete;
import com.imss.sivimss.ordservicios.model.request.ArticuloFunerarioRequest;
import com.imss.sivimss.ordservicios.model.request.AsignacionesAtaudRequest;
import com.imss.sivimss.ordservicios.model.request.PaqueteRequest;
import com.imss.sivimss.ordservicios.model.response.ArticuloFunerarioResponse;
import com.imss.sivimss.ordservicios.model.response.AsignacionesAtaudResponse;
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
	
	private final Ataud ataud= Ataud.obtenerInstancia();
	
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
	public Response<Object> consultarAtaudTipoAsignacionPaquete(DatosRequest request, Authentication authentication)
			throws IOException {
		Response<Object>response= new Response<>(false,HttpStatus.OK.value(),AppConstantes.EXITO);
		AsignacionesAtaudRequest asignacionesRequest= new AsignacionesAtaudRequest();
		String query="";
		Map<String, Object>paramtero= new HashMap<>();
		DatosRequest requestPeticion= new DatosRequest();
		AsignacionesAtaudResponse asignacionesAtaudResponse= new AsignacionesAtaudResponse();
		
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarAtaudTipoAsignacionPaquete", AppConstantes.CONSULTA, authentication);
            Gson gson= new Gson();
            String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
            asignacionesRequest=gson.fromJson(datosJson, AsignacionesAtaudRequest.class);
            for(Integer idTipoAsignacion: asignacionesRequest.getAsignaciones()){
            	switch (idTipoAsignacion) {
    			case 1:
    	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarAtaudTipoAsignacionConsignado", AppConstantes.CONSULTA, authentication);
    				query= ataud.obtenerAtaudTipoAsignacion(idTipoAsignacion);
    				paramtero.put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8)));
    				requestPeticion.setDatos(paramtero);
            		response=providerServiceRestTemplate.consumirServicio(requestPeticion.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
            		paramtero.clear();
            		asignacionesAtaudResponse.setConsigando(obtenerArticulosFunerarios(response));
            		break;
    				
    			case 3:
    	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarAtaudTipoAsignacionDonado", AppConstantes.CONSULTA, authentication);
    				query= ataud.obtenerAtaudTipoAsignacion(idTipoAsignacion);
    				paramtero.put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8)));
    				requestPeticion.setDatos(paramtero);
            		response=providerServiceRestTemplate.consumirServicio(requestPeticion.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
            		paramtero.clear();
            		asignacionesAtaudResponse.setDonado(obtenerArticulosFunerarios(response));
            		break;
    			case 5:
    	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarAtaudTipoAsignacionEconmico", AppConstantes.CONSULTA, authentication);
    				query= ataud.obtenerAtaudTipoAsignacion(idTipoAsignacion);
    				paramtero.put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8)));
    				requestPeticion.setDatos(paramtero);
            		response=providerServiceRestTemplate.consumirServicio(requestPeticion.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
            		paramtero.clear();
            		asignacionesAtaudResponse.setEconomico(obtenerArticulosFunerarios(response));
            		break;

    			default:
    				break;
    			}
            }
            response.setDatos(ConvertirGenerico.convertInstanceOfObject(asignacionesAtaudResponse));
    		return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.EXITO, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
	        log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
	}
	
	private List<ArticuloFunerarioResponse>obtenerArticulosFunerarios(Response<Object> response){
		List<ArticuloFunerarioResponse>articuloFunerarioResponses= new ArrayList<>();
		if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
			articuloFunerarioResponses=Arrays.asList(mapper.map(response.getDatos(), ArticuloFunerarioResponse[].class));
		}
		return articuloFunerarioResponses;
	}

	@Override
	public Response<Object> consultarProveedorAtaud(DatosRequest request, Authentication authentication)
			throws IOException {
			Response<Object>response;
			ArticuloFunerarioRequest articuloFunerarioRequest= new ArticuloFunerarioRequest();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarProveedorAtaud", AppConstantes.CONSULTA, authentication);
            Gson gson= new Gson();
            String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
            articuloFunerarioRequest= gson.fromJson(datosJson, ArticuloFunerarioRequest.class);
            response=providerServiceRestTemplate.consumirServicio(ataud.obtenerProveedorAtaud(articuloFunerarioRequest.getIdArticulo()).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
            return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR); 
		} catch (Exception e) {
			String consulta = ataud.obtenerProveedorAtaud(articuloFunerarioRequest.getIdArticulo()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(AppConstantes.ERROR_QUERY.concat(decoded));
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
			throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
	}
	
	

}
