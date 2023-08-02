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
import com.imss.sivimss.ordservicios.beans.ArticuloComplementario;
import com.imss.sivimss.ordservicios.beans.Ataud;
import com.imss.sivimss.ordservicios.beans.Empaque;
import com.imss.sivimss.ordservicios.beans.Urna;
import com.imss.sivimss.ordservicios.model.request.ArticuloComplementarioRequest;
import com.imss.sivimss.ordservicios.model.request.ArticuloFunerarioRequest;
import com.imss.sivimss.ordservicios.model.response.ArticuloComplementarioResponse;
import com.imss.sivimss.ordservicios.model.response.ArticuloFunerarioResponse;
import com.imss.sivimss.ordservicios.service.ArticuloService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;


@Service
public class ArticuloServiceImpl implements ArticuloService{
	
	@Value("${endpoints.mod-catalogos}")
	private String urlConsultar;

	private final Ataud ataud=Ataud.obtenerInstancia();
	
	private final Urna urna=Urna.obtenerInstancia();

	private final Empaque empaque=Empaque.obtenerInstancia();
	
	private final ArticuloComplementario articuloComplementario= ArticuloComplementario.getInstancia();
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper modelMapper;
	
	private static final String SIN_EXISTENCIA = "15";
	
	private final LogUtil logUtil;
	
	
	private static final Logger log = LoggerFactory.getLogger(ArticuloServiceImpl.class);

	
	public ArticuloServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper, LogUtil logUtil) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper = modelMapper;
		this.logUtil=logUtil;
	}

	@Override
	public Response<Object> consultarAtaud(DatosRequest request, Authentication authentication) throws IOException {
		ArticuloFunerarioRequest articuloFunerarioRequest= new ArticuloFunerarioRequest();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarAtaud", AppConstantes.CONSULTA, authentication);
            String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
            Gson gson= new Gson();
            articuloFunerarioRequest=gson.fromJson(datosJson, ArticuloFunerarioRequest.class);
			List<ArticuloFunerarioResponse>articuloFunerarioResponses;
			Response<Object>response=providerServiceRestTemplate.consumirServicio(ataud.obtenerAtaudes(articuloFunerarioRequest.getIdVelatorio()).getDatos(), urlConsultar.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			if (response.getCodigo()==200) {
				if(!response.getDatos().toString().contains("[]")) {
				articuloFunerarioResponses= Arrays.asList(modelMapper.map(response.getDatos(), ArticuloFunerarioResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloFunerarioResponses));
				return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.EXITO, AppConstantes.ERROR_CONSULTAR);
				}else {
					return MensajeResponseUtil.mensajeConsultaResponseObject(response, SIN_EXISTENCIA, AppConstantes.ERROR_CONSULTAR);
					}
				}
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.EXITO, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = ataud.obtenerAtaudes(articuloFunerarioRequest.getIdVelatorio()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        log.error(e.getMessage());
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
	}
	
	@Override
	public Response<Object> consultarUrna(DatosRequest request, Authentication authentication) throws IOException {
		ArticuloFunerarioRequest articuloFunerarioRequest= new ArticuloFunerarioRequest();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarUrna", AppConstantes.CONSULTA, authentication);
            String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
            Gson gson= new Gson();
            articuloFunerarioRequest=gson.fromJson(datosJson, ArticuloFunerarioRequest.class);
            List<ArticuloFunerarioResponse>articuloFunerarioResponses;
            Response<Object>response=providerServiceRestTemplate.consumirServicio(urna.obtenerUrna(articuloFunerarioRequest.getIdVelatorio()).getDatos(), urlConsultar.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
            if (response.getCodigo()==200) {
            	if (!response.getDatos().toString().contains("[]")) {
            		articuloFunerarioResponses=Arrays.asList(modelMapper.map(response.getDatos(), ArticuloFunerarioResponse[].class));
            		response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloFunerarioResponses));
            		return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.EXITO, AppConstantes.ERROR_CONSULTAR);
				} else {
					return MensajeResponseUtil.mensajeConsultaResponseObject(response, SIN_EXISTENCIA, AppConstantes.ERROR_CONSULTAR);
					}
            	}
           return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.EXITO, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = urna.obtenerUrna(articuloFunerarioRequest.getIdVelatorio()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        log.error(e.getMessage());
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
	}

	@Override
	public Response<Object> consultarEmpaque(DatosRequest request, Authentication authentication) throws IOException {
		ArticuloFunerarioRequest articuloFunerarioRequest= new ArticuloFunerarioRequest();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarEmpaque", AppConstantes.CONSULTA, authentication);
            String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
            Gson gson= new Gson();
            articuloFunerarioRequest=gson.fromJson(datosJson, ArticuloFunerarioRequest.class);
			List<ArticuloFunerarioResponse>articuloFunerarioResponses;
			Response<Object>response=providerServiceRestTemplate.consumirServicio(empaque.obtenerEmpaque(articuloFunerarioRequest.getIdVelatorio()).getDatos(), urlConsultar.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			if (response.getCodigo()==200) {
				if (!response.getDatos().toString().contains("[]")) {
					articuloFunerarioResponses=Arrays.asList(modelMapper.map(response.getDatos(), ArticuloFunerarioResponse[].class));
					response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloFunerarioResponses));
            		return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.EXITO, AppConstantes.ERROR_CONSULTAR);

					} else {
						return MensajeResponseUtil.mensajeConsultaResponseObject(response, SIN_EXISTENCIA, AppConstantes.ERROR_CONSULTAR);
						}
				}
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.EXITO, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = urna.obtenerUrna(articuloFunerarioRequest.getIdVelatorio()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        log.error(e.getMessage());
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
	}

	@Override
	public Response<Object> consultarArticuloComplementario(DatosRequest request, Authentication authentication)
			throws IOException {
		ArticuloComplementarioRequest articuloComplementarioRequest= new ArticuloComplementarioRequest();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarArticuloComplementario", AppConstantes.CONSULTA, authentication);
            String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
            Gson gson= new Gson();
            articuloComplementarioRequest=gson.fromJson(datosJson, ArticuloComplementarioRequest.class);
			List<ArticuloComplementarioResponse>articuloFunerarioResponses;
			Response<Object>response=providerServiceRestTemplate.consumirServicio(articuloComplementario.obtenerArticulosComplementarios(articuloComplementarioRequest.getIdVelatorio()).getDatos(), urlConsultar.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			
			if (response.getCodigo()==200) {
				if (!response.getDatos().toString().contains("[]")) {
					articuloFunerarioResponses=Arrays.asList(modelMapper.map(response.getDatos(), ArticuloComplementarioResponse[].class));
					response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloFunerarioResponses));
            		return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.EXITO, AppConstantes.ERROR_CONSULTAR);

					} else {
						return MensajeResponseUtil.mensajeConsultaResponseObject(response, SIN_EXISTENCIA, AppConstantes.ERROR_CONSULTAR);
						}
				}
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = articuloComplementario.obtenerArticulosComplementarios(articuloComplementarioRequest.getIdVelatorio()).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        log.error(e.getMessage());
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
	
	}

	@Override
	public Response<Object> consultarArticuloComplementarioPorId(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson= new Gson();
		String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
		ArticuloComplementarioRequest articuloComplementarioRequest=gson.fromJson(datosJson, ArticuloComplementarioRequest.class);
		List<ArticuloComplementarioResponse>articuloFunerarioResponses;
		Response<Object>response=providerServiceRestTemplate
				.consumirServicio(articuloComplementario.obtenerArticulosComplementariosPorId(articuloComplementarioRequest.getIdArticulo()).getDatos(), urlConsultar, authentication);
		if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
				articuloFunerarioResponses=Arrays.asList(modelMapper.map(response.getDatos(), ArticuloComplementarioResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(articuloFunerarioResponses));
			}
		
		return response;
	}

	

}
