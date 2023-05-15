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
import com.imss.sivimss.ordservicios.model.request.ContratanteRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;
@Service
public class OrdenConsultar {

	@Value("${endpoints.dominio-consulta}")
	private String urlDominio;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	@Autowired
	private ProviderServiceRestTemplate providerServiceRestTemplate;	
	
	
	public Response<?>buscarRfc(DatosRequest datosRequest, Authentication authentication) throws IOException{
		Gson gson= new Gson();
		String datosJson=datosRequest.getDatos().get(AppConstantes.DATOS).toString();
		ContratanteRequest contratanteRequest=gson.fromJson(datosJson, ContratanteRequest.class);
		Map<String, Object>parametro= new HashMap<>();
		DatosRequest request= new DatosRequest();
		String query=reglasNegocioRepository.obtenerDatosContratanteRfc(contratanteRequest.getRfc());
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
	}
	
	public Response<?>buscarCurp(DatosRequest datosRequest) throws IOException{
		return null;
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
