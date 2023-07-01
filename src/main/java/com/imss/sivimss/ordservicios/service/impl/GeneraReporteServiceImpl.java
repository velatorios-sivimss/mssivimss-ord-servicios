package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.Reporte;
import com.imss.sivimss.ordservicios.model.request.ContratoServicioInmediatoRequest;
import com.imss.sivimss.ordservicios.model.response.ContratoServicioInmediatoResponse;
import com.imss.sivimss.ordservicios.service.GeneraReporteService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class GeneraReporteServiceImpl  implements GeneraReporteService {
	
	private static final String SIN_INFORMACION = "45";  // No se encontró información relacionada a tu búsqueda.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO= "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String CONSULTA = "consulta";
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Value("${endpoints.mod-catalogos}")
	private String urlConsultar;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;

	@Value("${reporte.contrato_servicio_inmediato}")
	private String contratoServicioInmediato;

	
	
	
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@Autowired
	private LogUtil logUtil;
	
	private static final Logger log = LoggerFactory.getLogger(GeneraReporteServiceImpl.class);

	Response<Object> response;
	
	@Override
	public Response<Object> generarDocumentoContratoServInmediato(DatosRequest request, Authentication authentication)
			throws IOException {
		try {
			ContratoServicioInmediatoRequest contratoServicioInmediatoRequest = new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), ContratoServicioInmediatoRequest.class);
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "generar documento contrato servicio inmediato", AppConstantes.CONSULTA, authentication);
            List<ContratoServicioInmediatoResponse>contratoServicioInmediatoResponse;
            Response<Object> response = providerRestTemplate.consumirServicio(new Reporte().consultarContratoServicioInmediato(request, contratoServicioInmediatoRequest).getDatos(),urlConsultar.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
            if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
            	contratoServicioInmediatoResponse = Arrays.asList(modelMapper.map(response.getDatos(), ContratoServicioInmediatoResponse[].class));
            	contratoServicioInmediatoRequest.setFolioOds(contratoServicioInmediatoResponse.get(0).getFolioOds());
            	contratoServicioInmediatoRequest.setNombreFibeso(contratoServicioInmediatoResponse.get(0).getNombreFibeso());
            	contratoServicioInmediatoRequest.setNombreContratante(contratoServicioInmediatoResponse.get(0).getNombreContratante());
            	contratoServicioInmediatoRequest.setCorreoFibeso(contratoServicioInmediatoResponse.get(0).getCorreoFibeso());
            	contratoServicioInmediatoRequest.setTelefonoQuejas(contratoServicioInmediatoResponse.get(0).getTelefonoQuejas());
            	contratoServicioInmediatoRequest.setDireccionCliente(contratoServicioInmediatoResponse.get(0).getDireccionCliente());
            	contratoServicioInmediatoRequest.setRfcCliente(contratoServicioInmediatoResponse.get(0).getRfcCliente());
            	contratoServicioInmediatoRequest.setTelefonoCliente(contratoServicioInmediatoResponse.get(0).getTelefonoCliente());
            	contratoServicioInmediatoRequest.setCorreoCliente(contratoServicioInmediatoResponse.get(0).getCorreoCliente());
            	contratoServicioInmediatoRequest.setDireccionVelatorio(contratoServicioInmediatoResponse.get(0).getDireccionVelatorio());
            	contratoServicioInmediatoRequest.setFechaOds(contratoServicioInmediatoResponse.get(0).getFechaOds());
            	contratoServicioInmediatoRequest.setHorarioInicio(contratoServicioInmediatoResponse.get(0).getHorarioInicio());
            	contratoServicioInmediatoRequest.setHorarioFin(contratoServicioInmediatoResponse.get(0).getHorarioFin());
            	contratoServicioInmediatoRequest.setCapillaVelatorio(contratoServicioInmediatoResponse.get(0).getCapillaVelatorio());
            	contratoServicioInmediatoRequest.setNombreFinado(contratoServicioInmediatoResponse.get(0).getNombreFinado());
            	contratoServicioInmediatoRequest.setLugarExpedicion(contratoServicioInmediatoResponse.get(0).getLugarExpedicion());
            	contratoServicioInmediatoRequest.setHorarioServicio(contratoServicioInmediatoResponse.get(0).getHorarioServicio());
            	contratoServicioInmediatoRequest.setDescripcionServicio(contratoServicioInmediatoResponse.get(0).getDescripcionServicio());
            	contratoServicioInmediatoRequest.setTotalOds(contratoServicioInmediatoResponse.get(0).getTotalOds());
            	contratoServicioInmediatoRequest.setNombrePanteon(contratoServicioInmediatoResponse.get(0).getNombrePanteon());
            	contratoServicioInmediatoRequest.setFechaServicio(contratoServicioInmediatoResponse.get(0).getFechaServicio());
            	Map<String, Object> envioDatos = generaReporteContratoServicioInmediato(contratoServicioInmediatoRequest,contratoServicioInmediato);
        		return MensajeResponseUtil.mensajeResponseObject(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication), ERROR_AL_DESCARGAR_DOCUMENTO);
            	
            }
            return MensajeResponseUtil.mensajeResponse(response, SIN_INFORMACION);
		} catch (Exception e) {
			log.error("Error.. {}", e.getMessage());
            logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Fallo al ejecutar el reporte : " + e.getMessage(), CONSULTA, authentication);
            throw new IOException("64", e.getCause());
		}
	}

	private Map<String, Object> generaReporteContratoServicioInmediato(ContratoServicioInmediatoRequest contratoServicioInmediatoRequest, String contratoServicioInmediato) {
		log.info(" INICIO - generaReporteContratoServicioInmediato");
		 
		Map<String, Object> envioDatos = new HashMap<>();
		
		envioDatos.put("version", contratoServicioInmediatoRequest.getVersion());
		envioDatos.put("folioODS", contratoServicioInmediatoRequest.getFolioOds());
		envioDatos.put("nombreFibeso", contratoServicioInmediatoRequest.getNombreFibeso());
		envioDatos.put("nombreContratante", contratoServicioInmediatoRequest.getNombreContratante());
		envioDatos.put("correoFibeso", contratoServicioInmediatoRequest.getCorreoFibeso());
		envioDatos.put("telefonoQuejas", contratoServicioInmediatoRequest.getTelefonoQuejas());
		envioDatos.put("direccionCliente", contratoServicioInmediatoRequest.getDireccionCliente());
		envioDatos.put("rfcCliente", contratoServicioInmediatoRequest.getRfcCliente());
		envioDatos.put("telefonoCliente", contratoServicioInmediatoRequest.getTelefonoCliente());
		envioDatos.put("correoCliente", contratoServicioInmediatoRequest.getCorreoCliente());
		envioDatos.put("direccionVelatorio", contratoServicioInmediatoRequest.getDireccionVelatorio());
		envioDatos.put("fechaODS", contratoServicioInmediatoRequest.getFechaOds());
		envioDatos.put("horarioInicio", contratoServicioInmediatoRequest.getHorarioInicio());
		envioDatos.put("horarioFin", contratoServicioInmediatoRequest.getHorarioFin());
		envioDatos.put("capillaVelatorio",contratoServicioInmediatoRequest.getCapillaVelatorio());
		envioDatos.put("nombreFinado", contratoServicioInmediatoRequest.getNombreFinado());
		envioDatos.put("lugarExpedicion", contratoServicioInmediatoRequest.getLugarExpedicion());
		envioDatos.put("horarioServicio", contratoServicioInmediatoRequest.getHorarioServicio());
		envioDatos.put("descripcionServicio", contratoServicioInmediatoRequest.getDescripcionServicio());
		envioDatos.put("totalODS", contratoServicioInmediatoRequest.getTotalOds());
		envioDatos.put("nombrePanteon", contratoServicioInmediatoRequest.getNombrePanteon());
		envioDatos.put("fechaSer", contratoServicioInmediatoRequest.getFechaServicio());
		envioDatos.put("tipoReporte", contratoServicioInmediatoRequest.getTipoReporte());
		envioDatos.put("rutaNombreReporte", contratoServicioInmediato);
		
		log.info("generaReporteContratoServicioInmediato: " + envioDatos.toString());
		
		log.info(" TERMINO - generaReporteContratoServicioInmediato");
		
		return envioDatos;
	}

}
