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

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;
	
	@Value("${reporte.genera_Reporte_ODS}")
	private String generaReporteODS;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;

	@Value("${reporte.reporte_detalle_is}")
	private String reporteDetalleIS;
	
	
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
	private static final String CU086_NOMBRE= "Genera Reoporte Detalle Importe Servicios: ";
	
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

	public Response<Object> consultaReporteODS(DatosRequest datosRequest, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(datosRequest.getDatos().get(AppConstantes.DATOS));
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		String query = reporteODSRepository.generaReporteODSCU025(reporteDto);
		try {
			log.info(CU025_NOMBRE);
			log.info(query );
			logUtil.crearArchivoLog(Level.INFO.toString(), CU025_NOMBRE + GENERAR_DOCUMENTO + " Genera Reporte ODS " + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "generarReporteODS", GENERA_DOCUMENTO, authentication);
			DatosRequest request= encodeQuery(query, datosRequest);
			Response<Object>  response = providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTA_PAGINADO), authentication);
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
			return response;
		} catch (Exception e) {
			log.error( CU025_NOMBRE + GENERAR_DOCUMENTO);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU025_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"", GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}	
	}
	private DatosRequest encodeQuery(String query, DatosRequest request) {
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public Response<Object> generaReporteDetalleIS(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		Map<String, Object> envioDatos = new HashMap<>();
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		envioDatos.put("fecIni", reporteDto.getFechaIni());
		envioDatos.put("fecFin", reporteDto.getFechaFin());
		envioDatos.put(TIPO_REPORTE, reporteDto.getTipoReporte());
		envioDatos.put(RUTA_NOMBRE_REPORTE, reporteDetalleIS);
		try {
			log.info(CU086_NOMBRE);
			log.info("reporteDetalleIS = %s" + reporteDetalleIS);
			log.info("fecIni= " + reporteDto.getFechaIni());
			log.info("fecFin= " + reporteDto.getFechaFin());
			logUtil.crearArchivoLog(Level.INFO.toString(), CU086_NOMBRE + GENERAR_DOCUMENTO + " Genera Reporte Importe Servicios " + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "generaReporteDetalleIS", GENERA_DOCUMENTO, authentication);
			Response<Object> response = providerServiceRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
		return   MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			log.error( CU086_NOMBRE + GENERAR_DOCUMENTO);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU086_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"", GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}	
	}
}
