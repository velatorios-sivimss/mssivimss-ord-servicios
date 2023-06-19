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
import com.imss.sivimss.ordservicios.beans.ContratoPF;
import com.imss.sivimss.ordservicios.model.request.ContratoPfRequest;
import com.imss.sivimss.ordservicios.model.response.ArticuloFunerarioResponse;
import com.imss.sivimss.ordservicios.model.response.ContratoPfSiniestroResponse;
import com.imss.sivimss.ordservicios.service.ContratoPFService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class ContratoPFServiceImpl implements ContratoPFService{

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper modelMapper;
	
	private final LogUtil logUtil;
	
	private final ContratoPF contratoPF = ContratoPF.getInstancia();
	
	private static final String ERROR_SINIESTRO="73";
	
	private static final Logger log = LoggerFactory.getLogger(ContratoPFServiceImpl.class);

	
	public ContratoPFServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper,
			LogUtil logUtil) {	
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper = modelMapper;
		this.logUtil = logUtil;
	}



	@Override
	public Response<Object> obtenerContratoPF(DatosRequest request, Authentication authentication) throws IOException {
		Response<Object>response;
		ContratoPfRequest contratoPfRequest = new ContratoPfRequest();
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "obtenerContratoPF", AppConstantes.CONSULTA, authentication);
            Gson gson= new Gson();
            String datosJson= request.getDatos().get(AppConstantes.DATOS).toString();
            contratoPfRequest=gson.fromJson(datosJson, ContratoPfRequest.class);
            response=providerServiceRestTemplate.consumirServicio(contratoPF.consultarSiniestros(contratoPfRequest).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
            List<ContratoPfSiniestroResponse> contratoPfSiniestroResponse;
            if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
            	contratoPfSiniestroResponse= Arrays.asList(modelMapper.map(response.getDatos(), ContratoPfSiniestroResponse[].class));
            	if (!contratoPfSiniestroResponse.isEmpty() && contratoPfSiniestroResponse.get(0).getTipoPrevision()!=null
            			&& contratoPfSiniestroResponse.get(0).getTipoPrevision()==1) {
            		return MensajeResponseUtil.mensajeConsultaResponseObject(response, ERROR_SINIESTRO, AppConstantes.ERROR_CONSULTAR);
            	}
            	
            	if (contratoPfSiniestroResponse.get(0).getTipoPrevision()==null) {
            		return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR, AppConstantes.ERROR_CONSULTAR);
     			}
                 
                 
			}
            // consultar convenio
            
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.EXITO, AppConstantes.ERROR_CONSULTAR);
		}catch (Exception e) {
			String consulta ="";
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
	}
	

}
