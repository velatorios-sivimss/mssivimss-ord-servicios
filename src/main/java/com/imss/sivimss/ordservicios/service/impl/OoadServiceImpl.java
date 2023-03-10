package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.service.OoadService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConstantsMensajes;
import com.imss.sivimss.ordservicios.util.DatosUsuarioDTO;
import com.imss.sivimss.ordservicios.util.EnviarDatosRequest;
import com.imss.sivimss.ordservicios.util.Response;
import com.imss.sivimss.ordservicios.util.RestTemplateUtil;

@Service
public class OoadServiceImpl implements OoadService {

	@Value("${endpoints.consulta-ooads}")
	private String urlConsultaOoad;
	
	@Value("${endpoints.paginado-unidad}")
	private String urlPaginadoUnidad;
	
	@Autowired
	private RestTemplateUtil restTemplateUtil;
	
	private static final Logger log = LoggerFactory.getLogger(OoadServiceImpl.class);
	
	@Override
	public Object obtenerOoad(DatosUsuarioDTO datosUsuarios) {
		
		Response<?> respuesta;
	    Map<String, Object> datos = new HashMap<>();
	    Object lista = null;
		Integer idOoad = datosUsuarios.getIDOOAD();
    	String rol = datosUsuarios.getRol();
		
    	log.info("Rol: {}", rol);
    	log.info( ConstantsMensajes.IDOOAD.getMensaje(), idOoad);
    	
    	if( rol.equals(AppConstantes.SUPERVISOR) ) {
    		datos.put("idOoad", 0);
    	}else {
    		datos.put("idOoad", idOoad);
    	}
    	
    	 try {
    		 
    		 respuesta = consumirEndpoint(datos, urlConsultaOoad);
    		 
 			if ( respuesta.getCodigo() != null && respuesta.getCodigo() == 200 ) {
 				
 				lista = respuesta.getDatos();
 				
 			}
 		
 		} catch (Exception e) {
 			log.info(e.getMessage());
 		}
    	
    	
		return lista;
	}
	
	private Response<?> consumirEndpoint(Map<String, Object> datos, String url) throws IOException {
		try {
			return restTemplateUtil.sendPostRequestByteArray(url,
					new EnviarDatosRequest(datos), Response.class);
		} catch (IOException exception) {
			log.error("Ha ocurrido un error al recuperar el reporte");
			throw exception;
		}
	}

	@Override
	public Object paginado(DatosUsuarioDTO datosUsuarios, Integer pagina, Integer tamanio, String sort,
			String columna) {
		
		Response<?> respuesta;
	    Map<String, Object> datos = new HashMap<>();
	    Object paginado = null;
		
	    Integer idOoad = datosUsuarios.getIDOOAD();
    	String rol = datosUsuarios.getRol();
		
    	datos.put("idOoad", idOoad);
    	datos.put("rol", rol);
    	datos.put("pagina", pagina);
    	datos.put("tamanio", tamanio);
    	
    	 try {	 
    		 respuesta = consumirEndpoint(datos, urlPaginadoUnidad);
 			if ( respuesta.getCodigo() != null && respuesta.getCodigo() == 200 ) {
 				paginado = respuesta.getDatos();
 			}
 		
 		} catch (Exception e) {
 			log.info(e.getMessage());
 		}
    	
		return paginado;
	}

}
