package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.model.request.PersonaRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;
@Service
public class OrdenConsultar {

	@Value("${endpoints.dominio-consulta}")
	private String urlDominio;
	
	@Value("${endpoints.renapo}")
	private String urlRenapo;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	@Autowired
	private ProviderServiceRestTemplate providerServiceRestTemplate;	
	
	private static final String CURP_NO_VALIDO = "34"; // CURP no valido.
	private static final String SERVICIO_RENAPO_NO_DISPONIBLE = "184"; // El servicio de RENAPO no esta disponible.
	
	
	public Response<?>buscarRfc(DatosRequest datosRequest, Authentication authentication) throws IOException{
		Gson gson= new Gson();
		String datosJson=datosRequest.getDatos().get(AppConstantes.DATOS).toString();
		PersonaRequest personaRequest=gson.fromJson(datosJson, PersonaRequest.class);
		Map<String, Object>parametro= new HashMap<>();
		DatosRequest request= new DatosRequest();
		String query=reglasNegocioRepository.obtenerDatosContratanteRfc(personaRequest.getRfc());
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
	}
	
	public Response<?>buscarCurp(DatosRequest datosRequest,Authentication authentication) throws IOException{
		Gson gson= new Gson();
		String datosJson= datosRequest.getDatos().get(AppConstantes.DATOS).toString();
		PersonaRequest personaRequest=gson.fromJson(datosJson, PersonaRequest.class);
		DatosRequest request= new DatosRequest();
		Map<String, Object>parametro=new HashMap<>();
		String query=reglasNegocioRepository.obtenerDatosContratanteCurp(personaRequest.getCurp());
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		Response<?>response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
		if (response.getCodigo()==200 && response.getDatos().toString().contains("[]")) {
			response= providerServiceRestTemplate.consumirServicioExternoGet(urlRenapo+"/"+personaRequest.getCurp());
			response.setMensaje("Externo");
			return  MensajeResponseUtil.mensajeResponseExterno(response, CURP_NO_VALIDO, SERVICIO_RENAPO_NO_DISPONIBLE);
		}
		return response;
	}
	
	public Response<?>buscarOrdenServicio(){
		return null;
	}
	
	public Response<?>detalleOrdenServicio(){
		return null;
	}
	
	public Response<?>consultarOrdenesServicio(){
		return null;
	}
	
	
}
