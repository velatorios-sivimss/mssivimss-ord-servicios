package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.model.request.ReporteDto;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioGeneraReporteODSRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;
@Service
public class GeneraReporte {

	@Value("${reporte.genera_Reporte_ODS}")
	private String generaReporteODS;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	
	@Autowired
	private ReglasNegocioGeneraReporteODSRepository reporteODSRepository;
	
	@Autowired
	private ProviderServiceRestTemplate providerServiceRestTemplate;	
	
	@Autowired
	private LogUtil logUtil;
	
	private static final Logger log = LoggerFactory.getLogger(GeneraReporte.class);
	
	private static final String ERROR_NO_SE_ENCONTRO_INFORMACION= "45"; // No se encontró información relacionada a tu búsqueda..

	private static final String CU025_NOMBRE= "Genera Reoporte ODS: ";
	private static final String GENERAR_DOCUMENTO = "Generar Reporte: " ;
	private static final String GENERA_DOCUMENTO = "Genera_Documento";
	private static final String TIPO_REPORTE = "tipoReporte";
	private static final String RUTA_NOMBRE_REPORTE = "rutaNombreReporte";
	
	public Response<Object> generarReporteODS(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		Map<String, Object> envioDatos = new HashMap<>();
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		String query = reporteODSRepository.generaReporteODSCU025(reporteDto);
		envioDatos.put("query", query);
		envioDatos.put(TIPO_REPORTE, reporteDto.getTipoReporte());
		envioDatos.put(RUTA_NOMBRE_REPORTE, generaReporteODS);
		try {
			log.info(CU025_NOMBRE);
			log.info(query );
			logUtil.crearArchivoLog(Level.INFO.toString(), CU025_NOMBRE + GENERAR_DOCUMENTO + " Genera Reporte ODS " + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "generarReporteODS", GENERA_DOCUMENTO, authentication);
			Response<Object> response = providerServiceRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
		return   MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			log.error( CU025_NOMBRE + GENERAR_DOCUMENTO);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU025_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"", GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}	
	}

}
