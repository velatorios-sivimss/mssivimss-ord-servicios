package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.model.request.PersonaRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;
@Service
public class OrdenConsultar {

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;
	
	@Value("${endpoints.renapo}")
	private String urlRenapo;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	@Autowired
	private ProviderServiceRestTemplate providerServiceRestTemplate;	
	
	@Autowired
	private LogUtil logUtil;
	
	
	private static final Logger log = LoggerFactory.getLogger(OrdenConsultar.class);

	
	private static final String CURP_NO_VALIDO = "34"; // CURP no valido.
	private static final String SERVICIO_RENAPO_NO_DISPONIBLE = "184"; // El servicio de RENAPO no esta disponible.
	
	
	public Response<Object>buscarRfc(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarRfc", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson=datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			PersonaRequest personaRequest=gson.fromJson(datosJson, PersonaRequest.class);
			Map<String, Object>parametro= new HashMap<>();
			DatosRequest request= new DatosRequest();
			query=reglasNegocioRepository.obtenerDatosContratanteRfc(personaRequest.getRfc());
			String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			parametro.put(AppConstantes.QUERY, encoded);
			request.setDatos(parametro);
			return MensajeResponseUtil.mensajeConsultaResponseObject(providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication),AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
	        log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
	}
	
	public Response<Object>buscarCurp(DatosRequest datosRequest,Authentication authentication) throws IOException{
		String query="";
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarCurp", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson= datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			PersonaRequest personaRequest=gson.fromJson(datosJson, PersonaRequest.class);
			DatosRequest request= new DatosRequest();
			Map<String, Object>parametro=new HashMap<>();
			query=reglasNegocioRepository.obtenerDatosContratanteCurp(personaRequest.getCurp());
			String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			parametro.put(AppConstantes.QUERY, encoded);
			request.setDatos(parametro);
			Response<Object>response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			if (response.getCodigo()==200 && response.getDatos().toString().contains("[]")) {
				response= providerServiceRestTemplate.consumirServicioExternoGet(urlRenapo+"/"+personaRequest.getCurp());
				response=  MensajeResponseUtil.mensajeResponseExterno(response, CURP_NO_VALIDO, SERVICIO_RENAPO_NO_DISPONIBLE);
				response.setMensaje("Externo");
				return response;
			}
			
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
			response.setMensaje("Interno");
			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
		
	}
	
	public Response<Object>buscarOrdenServicio(DatosRequest datosRequest,Authentication authentication) throws IOException{
		return null;
	}
	
	public Response<?>detalleOrdenServicio(){
		return null;
	}
	
	public Response<?>consultarOrdenesServicio(){
		return null;
	}
	
	
}
