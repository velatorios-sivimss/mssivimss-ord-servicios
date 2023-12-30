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
import com.imss.sivimss.ordservicios.beans.Servicio;
import com.imss.sivimss.ordservicios.model.request.ProveedorServicioRequest;
import com.imss.sivimss.ordservicios.model.response.ProveedorServicioResponse;
import com.imss.sivimss.ordservicios.model.response.ServicioResponse;
import com.imss.sivimss.ordservicios.service.ServicioService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;

@Service
public class ServicioServiceImpl implements ServicioService {

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;

	private Servicio servicio = Servicio.getInstancia();

	private final ProviderServiceRestTemplate providerServiceRestTemplate;

	private final ModelMapper modelMapper;

	private final LogUtil logUtil;

	private static final Logger log = LoggerFactory.getLogger(ServicioServiceImpl.class);

	public ServicioServiceImpl(Provide

	}

	@Override
	public Response<Object> consultarProvedorServicios(DatosRequest request, Authentication authentication)
			throws IOException {
		ProveedorServicioRequest proveedorServicioRequest = new ProveedorServicioRequest();
		Integer idVelatorio = 0;
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultarProvedorServicios", AppConstantes.CONSULTA,
					authentication);
			Gson gson1 = new Gson();
			UsuarioDto usuario = gson1.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
			idVelatorio = usuario.getIdVelatorio();
			
			Gson gson = new Gson();
			String datosJson = request.getDatos().get(AppConstantes.DATOS).toString();
			proveedorServicioRequest = gson.fromJson(datosJson, ProveedorServicioRequest.class);
			Response<Object> response = providerServiceRestTemplate.consumirServicio(
					servicio.obtenerProveedorServicio(proveedorServicioRequest.getIdServicio(),idVelatorio).getDatos(),
					urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = servicio.obtenerProveedorServicio(proveedorServicioRequest.getIdServicio(),idVelatorio).getDatos()
					.get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(AppConstantes.ERROR_QUERY.concat(decoded));
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded,
					AppConstantes.CONSULTA, authentication);
			throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}

	}

	@Override
	public Response<Object> consultarServiciosVigentes(DatosRequest request, Authentication authentication)
		

		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarServiciosVigentes", AppConstantes.CONSULTA, authentication);
			response=providerServiceRestTemplate.consumirServicio(servicio.obtenerServiciosVigentes().getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication) ;
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = servicio.obtenerServiciosVigentes().getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(AppConstantes.ERROR_QUERY.concat(decoded));
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
			throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
		
	}

}
