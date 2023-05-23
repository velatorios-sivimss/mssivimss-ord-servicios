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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.model.request.OrdenesServicioRequest;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class OrdenGuardar {

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;
	
	@Autowired
	private ProviderServiceRestTemplate providerServiceRestTemplate;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	@Autowired
	private LogUtil logUtil;
	
	
	private static final Logger log = LoggerFactory.getLogger(OrdenGuardar.class);

	
	public Response<Object> agregarOrden(DatosRequest datosRequest, Authentication authentication) throws IOException{
		Response<Object>response;
		String query="El tipo orden de servicio es incorrecto.";
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "agregarOrden", AppConstantes.ALTA, authentication);

			Gson gson= new Gson();
			UsuarioDto usuario = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
			String datosJson=datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			OrdenesServicioRequest ordenesServicioRequest=gson.fromJson(datosJson, OrdenesServicioRequest.class);
			switch (ordenesServicioRequest.getFinado().getIdTipoOrden()) {
			case 1:
	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "servicioInmediato", AppConstantes.ALTA, authentication);
				query=servicioInmediato(ordenesServicioRequest);
				break;
			case 2:
	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "contratoPF", AppConstantes.ALTA, authentication);

				query=contratoPF(ordenesServicioRequest);
				break;
			case 3:
	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "articulosComplementarios", AppConstantes.ALTA, authentication);

				query=articulosComplementarios(ordenesServicioRequest);
				break;
			case 4:
	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "convenioPF", AppConstantes.ALTA, authentication);

				query= convenioPF(ordenesServicioRequest);
				break;
			default:
				throw new BadRequestException(HttpStatus.BAD_REQUEST, query);
			}
			DatosRequest request= new DatosRequest();
			Map<String, Object>parametros= new HashMap<>();
			String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			parametros.put(AppConstantes.QUERY, encoded);
			request.setDatos(parametros);
			//return response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CREAR_MULTIPLE), authentication);
			return new Response<>(false, 200, AppConstantes.DATOS+usuario.getNombre());
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
		    logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.ALTA, authentication);
		    throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
	}
	
	private String servicioInmediato(OrdenesServicioRequest ordenesServicioRequest){
		return null;
	}
	
	private String contratoPF(OrdenesServicioRequest ordenesServicioRequest){
		return null;
	}
	
	private String articulosComplementarios(OrdenesServicioRequest ordenesServicioRequest){
		return null;
	}
	
	private String convenioPF(OrdenesServicioRequest ordenesServicioRequest){
		return null;
	}
	
}
