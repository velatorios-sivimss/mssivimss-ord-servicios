package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.model.response.ContratoServicioInmediatoResponse;
import com.imss.sivimss.ordservicios.model.response.ReporteControlSalidaDonacionResponse;
import com.imss.sivimss.ordservicios.model.response.ReporteDonacionResponse;
import com.imss.sivimss.ordservicios.service.GeneraReporteService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConsultaConstantes;
import com.imss.sivimss.ordservicios.util.Database;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.NumeroLetras;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class GeneraReporteServiceImpl  implements GeneraReporteService {
	
	private static final String SIN_INFORMACION = "45";  // No se encontró información relacionada a tu búsqueda.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO= "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String CONSULTA = "consulta";
	private static final String CU024_NOMBRE= "Consulta Orden Servicio: ";
	private static final String GENERAR_DOCUMENTO = "Generar Reporte: " ;
	private static final String GENERA_DOCUMENTO = "Genera_Documento";
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Value("${endpoints.mod-catalogos}")
	private String urlConsultar;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;

	@Value("${reporte.contrato_servicio_inmediato}")
	private String contratoServicioInmediato;
	
	@Value("${reporte.contrato_servicio_inmediato_temp}")
	private String contratoServicioInmediatoTemp;
	
	@Value("${reporte.control-salida-ataudes-donacion}")
	private String nombrePdfControlSalida;
	
	@Value("${reporte.aceptacion-control-ataudes-donacion}")
	private String nombrePdfAceptacionControl;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@Autowired
	private LogUtil logUtil;
	
	private static final Logger log = LoggerFactory.getLogger(GeneraReporteServiceImpl.class);

	Response<Object> response;
	
	@Autowired
	private Database database;

	private Connection connection;

	private Statement statement;
	
	private ResultSet rs;
	
	@Override
	public Response<Object> generarDocumentoContratoServInmediato(DatosRequest request, Authentication authentication)
			throws IOException {
		try {
			UsuarioDto usuario = new Gson().fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
			ContratoServicioInmediatoRequest contratoServicioInmediatoRequest = new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), ContratoServicioInmediatoRequest.class);
			contratoServicioInmediatoRequest.setIdVelatorio(usuario.getIdVelatorio());
			List<ContratoServicioInmediatoResponse>contratoServicioInmediatoResponse;
            if(contratoServicioInmediatoRequest.getGeneraReporte() == 1) {
            	logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "generar documento contrato servicio inmediato", AppConstantes.CONSULTA, authentication);
                response = providerRestTemplate.consumirServicio(new Reporte().consultarContratoServicioInmediato(request, contratoServicioInmediatoRequest).getDatos(),urlConsultar.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
                if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
                	contratoServicioInmediatoResponse = Arrays.asList(modelMapper.map(response.getDatos(), ContratoServicioInmediatoResponse[].class));
                	generarMap(contratoServicioInmediatoRequest, contratoServicioInmediatoResponse);
                	Map<String, Object> envioDatos = generaReporteContratoServicioInmediato(contratoServicioInmediatoRequest,contratoServicioInmediato);
            		return MensajeResponseUtil.mensajeResponseObject(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication), ERROR_AL_DESCARGAR_DOCUMENTO);
                }
            } else {
            	logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "generar documento contrato servicio inmediato temp", AppConstantes.CONSULTA, authentication);
                response = providerRestTemplate.consumirServicio(new Reporte().consultarContratoServicioInmediatoTemp(request, contratoServicioInmediatoRequest).getDatos(),urlConsultar.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
                if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
                	contratoServicioInmediatoResponse = Arrays.asList(modelMapper.map(response.getDatos(), ContratoServicioInmediatoResponse[].class));
                	generarMap(contratoServicioInmediatoRequest, contratoServicioInmediatoResponse);
                	Map<String, Object> envioDatos = generaReporteContratoServicioInmediato(contratoServicioInmediatoRequest,contratoServicioInmediatoTemp);
            		return MensajeResponseUtil.mensajeResponseObject(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication), ERROR_AL_DESCARGAR_DOCUMENTO);
                }
            }
            return MensajeResponseUtil.mensajeResponse(response, SIN_INFORMACION);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error.. {}", e.getMessage());
            logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "Fallo al ejecutar el reporte : " + e.getMessage(), CONSULTA, authentication);
            throw new IOException("64", e.getCause());
		}
	}

	private void generarMap(ContratoServicioInmediatoRequest contratoServicioInmediatoRequest,
			List<ContratoServicioInmediatoResponse> contratoServicioInmediatoResponse) {
		contratoServicioInmediatoRequest.setNombreCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getNombreCliente()));
		contratoServicioInmediatoRequest.setNacionalidadCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getNacionalidadCliente()));
		contratoServicioInmediatoRequest.setCorreoCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getCorreoCliente()));
		contratoServicioInmediatoRequest.setTelefonoCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getTelefonoCliente()));
		contratoServicioInmediatoRequest.setRfcCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getRfcCliente()));
		contratoServicioInmediatoRequest.setCalleCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getCalleCliente()));
		contratoServicioInmediatoRequest.setNumInteriorCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getNumInteriorCliente()));
		contratoServicioInmediatoRequest.setNumExteriorCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getNumExteriorCliente()));
		contratoServicioInmediatoRequest.setColoniaCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getColoniaCliente()));
		contratoServicioInmediatoRequest.setCpCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getCpCliente()));
		contratoServicioInmediatoRequest.setMunicipioCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getMunicipioCliente()));
		contratoServicioInmediatoRequest.setEstadoCliente(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getEstadoCliente()));
		contratoServicioInmediatoRequest.setImporteTotalOds(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getImporteTotalOds()));
		contratoServicioInmediatoRequest.setNombreVelatorio(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getNombreVelatorio()));
		contratoServicioInmediatoRequest.setCalleVelatorio(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getCalleVelatorio()));
		contratoServicioInmediatoRequest.setNumInteriorVelatorio(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getNumInteriorVelatorio()));
		contratoServicioInmediatoRequest.setNumExteriorVelatorio(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getNumExteriorVelatorio()));
		contratoServicioInmediatoRequest.setColoniaVelatorio(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getColoniaVelatorio()));
		contratoServicioInmediatoRequest.setCpVelatorio(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getCpVelatorio()));
		contratoServicioInmediatoRequest.setMunicipioVelatorio(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getMunicipioVelatorio()));
		contratoServicioInmediatoRequest.setEstadoVelatorio(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getEstadoVelatorio()));
		contratoServicioInmediatoRequest.setNombreFinado(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getNombreFinado()));
		contratoServicioInmediatoRequest.setDiaOds(contratoServicioInmediatoResponse.get(0).getDiaOds());
		contratoServicioInmediatoRequest.setMesOds(contratoServicioInmediatoResponse.get(0).getMesOds());
		contratoServicioInmediatoRequest.setAnioOds(contratoServicioInmediatoResponse.get(0).getAnioOds());
		contratoServicioInmediatoRequest.setNombrePaquete(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getNombrePaquete()));
		contratoServicioInmediatoRequest.setPrecioPaquete(ConsultaConstantes.validar(contratoServicioInmediatoResponse.get(0).getPrecioPaquete()));

	}

	private Map<String, Object> generaReporteContratoServicioInmediato(ContratoServicioInmediatoRequest contratoServicioInmediatoRequest, String contratoServicioInmediato) {
		log.info(" INICIO - generaReporteContratoServicioInmediato");
		 
		Map<String, Object> envioDatos = new HashMap<>();
		NumeroLetras numeroLetras = new NumeroLetras();
		
		envioDatos.put("idOrdenServicio", contratoServicioInmediatoRequest.getIdOrdenServicio());
		envioDatos.put("nombreCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getNombreCliente()));
		envioDatos.put("nacionalidadCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getNacionalidadCliente()));
		envioDatos.put("correoCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getCorreoCliente()));
		envioDatos.put("telefonoCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getTelefonoCliente()));
		envioDatos.put("rfcCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getRfcCliente()));
		envioDatos.put("calleCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getCalleCliente()));
		envioDatos.put("numInteriorCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getNumInteriorCliente()));
		envioDatos.put("numExteriorCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getNumExteriorCliente()));
		envioDatos.put("coloniaCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getColoniaCliente()));
		envioDatos.put("cpCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getCpCliente()));
		envioDatos.put("municipioCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getMunicipioCliente()));
		envioDatos.put("estadoCliente", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getEstadoCliente()));
		envioDatos.put("importeTotalOds", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getImporteTotalOds()));
		envioDatos.put("importeLetraTotalOds", ConsultaConstantes.validar(numeroLetras.Convertir(contratoServicioInmediatoRequest.getImporteTotalOds().replaceAll(",",""), true)));
		envioDatos.put("nombreVelatorio", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getNombreVelatorio()));
		envioDatos.put("calleVelatorio", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getCalleVelatorio()));
		envioDatos.put("nombreFinado", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getNombreFinado()));
		envioDatos.put("numInteriorVelatorio", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getNumInteriorVelatorio()));
		envioDatos.put("numExteriorVelatorio", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getNumExteriorVelatorio()));
		envioDatos.put("coloniaVelatorio", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getColoniaVelatorio()));
		envioDatos.put("cpVelatorio", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getCpVelatorio()));
		envioDatos.put("municipioVelatorio", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getMunicipioVelatorio()));
		envioDatos.put("estadoVelatorio", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getEstadoVelatorio()));
		envioDatos.put("diaOds", contratoServicioInmediatoRequest.getDiaOds());
		envioDatos.put("mesOds", contratoServicioInmediatoRequest.getMesOds());
		envioDatos.put("anioOds", contratoServicioInmediatoRequest.getAnioOds());
		envioDatos.put("nombrePaquete", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getNombrePaquete()));
		envioDatos.put("precioPaquete", ConsultaConstantes.validar(contratoServicioInmediatoRequest.getPrecioPaquete()));
		envioDatos.put("tipoReporte", contratoServicioInmediatoRequest.getTipoReporte());
		envioDatos.put("rutaNombreReporte", contratoServicioInmediato);
		
		log.info(" TERMINO - generaReporteContratoServicioInmediato");
		
		return envioDatos;
	}

	@Override
	public Response<Object> generarReporteSalidaDonacion(DatosRequest request, Authentication authentication)
			throws SQLException, IOException {
		Gson gson= new Gson();
		String datosJson= request.getDatos().get(AppConstantes.DATOS).toString();
		ContratoServicioInmediatoRequest contratoServicioInmediatoRequest = gson.fromJson(datosJson, ContratoServicioInmediatoRequest.class);
		ReporteControlSalidaDonacionResponse reporteControlSalidaDonacionResponse = new ReporteControlSalidaDonacionResponse();
		connection = database.getConnection();
		statement = connection.createStatement();
		connection.setAutoCommit(false);
		 if(contratoServicioInmediatoRequest.getGeneraReporte() == 1) {
			 rs=statement.executeQuery(new Reporte().consultarReporteSalidaDonacion(contratoServicioInmediatoRequest.getIdOrdenServicio()));
		 } else {
			 rs=statement.executeQuery(new Reporte().consultarReporteSalidaDonacionTemp(contratoServicioInmediatoRequest.getIdOrdenServicio()));
		 }
		if (rs.next()) {
			reporteControlSalidaDonacionResponse.setOoadNom(rs.getString(1));
			reporteControlSalidaDonacionResponse.setVelatorioId(rs.getInt(2));
			reporteControlSalidaDonacionResponse.setVelatorioNom(rs.getString(3));
			reporteControlSalidaDonacionResponse.setNumAtaudes(rs.getInt(4));
			reporteControlSalidaDonacionResponse.setModeloAtaud(rs.getString(5));
			reporteControlSalidaDonacionResponse.setTipoAtaud(rs.getString(6));
			reporteControlSalidaDonacionResponse.setNumInventarios(rs.getString(7));
			reporteControlSalidaDonacionResponse.setNomSolicitantes(rs.getString(8));
			reporteControlSalidaDonacionResponse.setNomFinados(rs.getString(9));
			reporteControlSalidaDonacionResponse.setFecSolicitud(rs.getString(10));
			reporteControlSalidaDonacionResponse.setNomSolicitante(rs.getString(11));
			reporteControlSalidaDonacionResponse.setNomAdministrador(rs.getString(12));
			reporteControlSalidaDonacionResponse.setClaveAdministrador(rs.getString(13));
			reporteControlSalidaDonacionResponse.setLugar(rs.getString(14));
			reporteControlSalidaDonacionResponse.setDia(rs.getInt(15));
			reporteControlSalidaDonacionResponse.setMes(rs.getString(16));
			reporteControlSalidaDonacionResponse.setAnio(rs.getInt(17));
		}
		reporteControlSalidaDonacionResponse.setTipoReporte(contratoServicioInmediatoRequest.getTipoReporte());
		

		try {
			log.info( CU024_NOMBRE + GENERAR_DOCUMENTO + " Reporte Salida Donacion " );
			logUtil.crearArchivoLog(Level.INFO.toString(), CU024_NOMBRE + GENERAR_DOCUMENTO + " Reporte Salida Donacion " + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "generarReporteSalidaDonacion", GENERA_DOCUMENTO, authentication);
			response = providerRestTemplate.consumirServicioReportes(controlSalidaDonacionMap(reporteControlSalidaDonacionResponse), urlReportes, authentication);
			
			return   response;
		} catch (Exception e) {
			log.error( CU024_NOMBRE + GENERAR_DOCUMENTO);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU024_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"", GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		} finally {
				statement.close();
				connection.close();
		}
	}

	private Map<String, Object> controlSalidaDonacionMap(ReporteControlSalidaDonacionResponse reporteControlSalidaRequest) {
		log.info(" INICIO - controlSalidaDonacionMap");
		
		Map<String, Object> envioDatos = new HashMap<>();
		
		envioDatos.put("version", reporteControlSalidaRequest.getVersion());
		envioDatos.put("ooadNom", ConsultaConstantes.validar(reporteControlSalidaRequest.getOoadNom()));
		envioDatos.put("velatorio", reporteControlSalidaRequest.getVelatorioId());
		envioDatos.put("velatorioNom", ConsultaConstantes.validar(reporteControlSalidaRequest.getVelatorioNom()));
		envioDatos.put("numAtaudes", reporteControlSalidaRequest.getNumAtaudes());
		envioDatos.put("modeloAtaud", ConsultaConstantes.validar(reporteControlSalidaRequest.getModeloAtaud()));
		envioDatos.put("tipoAtaud", ConsultaConstantes.validar(reporteControlSalidaRequest.getTipoAtaud()));
		envioDatos.put("numInventarios", ConsultaConstantes.validar(reporteControlSalidaRequest.getNumInventarios()));
		envioDatos.put("nomSolicitantes", ConsultaConstantes.validar(reporteControlSalidaRequest.getNomSolicitantes()));
		envioDatos.put("nomFinados", ConsultaConstantes.validar(reporteControlSalidaRequest.getNomFinados()));
		envioDatos.put("fecSolicitud", ConsultaConstantes.validar(reporteControlSalidaRequest.getFecSolicitud()));
		envioDatos.put(ConsultaConstantes.RESPONSABLE_ALMACEN, ConsultaConstantes.validar(reporteControlSalidaRequest.getNomResponsableAlmacen()));
		envioDatos.put("matriculaResponSable", ConsultaConstantes.validar(reporteControlSalidaRequest.getClaveResponsableAlmacen()));
		envioDatos.put("solicitante", ConsultaConstantes.validar(reporteControlSalidaRequest.getNomSolicitante()));
		envioDatos.put("administrador", ConsultaConstantes.validar(reporteControlSalidaRequest.getNomAdministrador()));
		envioDatos.put("matriculaAdministrador", ConsultaConstantes.validar(reporteControlSalidaRequest.getClaveAdministrador()));
		envioDatos.put("lugar", ConsultaConstantes.validar(reporteControlSalidaRequest.getLugar()));
		envioDatos.put("dia", reporteControlSalidaRequest.getDia());
		envioDatos.put("mes", ConsultaConstantes.validar(reporteControlSalidaRequest.getMes()));
		envioDatos.put("anio", reporteControlSalidaRequest.getAnio());
		envioDatos.put(ConsultaConstantes.TIPO_REPORTE, reporteControlSalidaRequest.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfControlSalida);
		
		log.info(" TERMINO - controlSalidaDonacionMap");

		return envioDatos;
	}

	@Override
	public Response<Object> generarReporteDonacion(DatosRequest request, Authentication authentication)
			throws IOException, SQLException {
		Gson gson= new Gson();
		String datosJson= request.getDatos().get(AppConstantes.DATOS).toString();
		ContratoServicioInmediatoRequest contratoServicioInmediatoRequest = gson.fromJson(datosJson, ContratoServicioInmediatoRequest.class);
		ReporteDonacionResponse reporteDonacionResponse = new ReporteDonacionResponse();
		connection = database.getConnection();
		statement = connection.createStatement();
		connection.setAutoCommit(false);
		 if(contratoServicioInmediatoRequest.getGeneraReporte() == 1) {
			 rs=statement.executeQuery(new Reporte().consultarReporteDonacion(contratoServicioInmediatoRequest.getIdOrdenServicio()));
		 } else {
			 rs=statement.executeQuery(new Reporte().consultarReporteDonacionTemp(contratoServicioInmediatoRequest.getIdOrdenServicio()));
		 }
		if (rs.next()) {
			reporteDonacionResponse.setOoadNom(rs.getString(1));
			reporteDonacionResponse.setVelatorioId(rs.getInt(2));
			reporteDonacionResponse.setNumContrato(rs.getString(3));
			reporteDonacionResponse.setModeloAtaud(rs.getString(4));
			reporteDonacionResponse.setTipoAtaud(rs.getString(5));
			reporteDonacionResponse.setNumInventarios(rs.getString(6));
			reporteDonacionResponse.setNomFinado(rs.getString(7));
			reporteDonacionResponse.setNomContratante(rs.getString(8));
			reporteDonacionResponse.setNomAdministrador(rs.getString(9));
			reporteDonacionResponse.setClaveAdministrador(rs.getString(10));
			reporteDonacionResponse.setLugar(rs.getString(11));
			reporteDonacionResponse.setDia(rs.getInt(12));
			reporteDonacionResponse.setMes(rs.getString(13));
			reporteDonacionResponse.setAnio(rs.getInt(14));
			
		}
		reporteDonacionResponse.setTipoReporte(contratoServicioInmediatoRequest.getTipoReporte());

		try {
			log.info( CU024_NOMBRE + GENERAR_DOCUMENTO + " Reporte Donacion " );
			logUtil.crearArchivoLog(Level.INFO.toString(), CU024_NOMBRE + GENERAR_DOCUMENTO + " Reporte Donacion " + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "generarReporteDonacion", GENERA_DOCUMENTO, authentication);
			response = providerRestTemplate.consumirServicioReportes(donacionMap(reporteDonacionResponse), urlReportes, authentication);
			
			return   response;
		} catch (Exception e) {
			log.error( CU024_NOMBRE + GENERAR_DOCUMENTO);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU024_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"", GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		} finally {
				statement.close();
				connection.close();
		}
	}
	
	private Map<String, Object> donacionMap(ReporteDonacionResponse reporteDonacionResponse) {
		log.info(" INICIO - donacionMap");
		
		Map<String, Object> envioDatos = new HashMap<>();
		
		envioDatos.put("version", reporteDonacionResponse.getVersion());
		envioDatos.put("ooadNom", ConsultaConstantes.validar(reporteDonacionResponse.getOoadNom()));
		envioDatos.put("velatorio", reporteDonacionResponse.getVelatorioId());
		envioDatos.put("numContrato", ConsultaConstantes.validar(reporteDonacionResponse.getNumContrato()));
		envioDatos.put("tipoAtaud", ConsultaConstantes.validar(reporteDonacionResponse.getTipoAtaud()));
		envioDatos.put("modeloAtaud", ConsultaConstantes.validar(reporteDonacionResponse.getModeloAtaud()));
		envioDatos.put("numInventarios", ConsultaConstantes.validar(reporteDonacionResponse.getNumInventarios()));
		envioDatos.put("nomFinado", ConsultaConstantes.validar(reporteDonacionResponse.getNomFinado()));
		envioDatos.put(ConsultaConstantes.RESPONSABLE_ALMACEN, ConsultaConstantes.validar(reporteDonacionResponse.getNomResponsableAlmacen()));
		envioDatos.put("matriculaResponSable",ConsultaConstantes.validar(reporteDonacionResponse.getClaveResponsableAlmacen()));
		envioDatos.put("contratante", ConsultaConstantes.validar(reporteDonacionResponse.getNomContratante()));
		envioDatos.put("administrador", ConsultaConstantes.validar(reporteDonacionResponse.getNomAdministrador()));
		envioDatos.put("matriculaAdministrador", ConsultaConstantes.validar(reporteDonacionResponse.getClaveAdministrador()));
		envioDatos.put("lugar", ConsultaConstantes.validar(reporteDonacionResponse.getLugar()));
		envioDatos.put("dia", reporteDonacionResponse.getDia());
		envioDatos.put("mes", ConsultaConstantes.validar(reporteDonacionResponse.getMes()));
		envioDatos.put("anio", reporteDonacionResponse.getAnio());
		envioDatos.put(ConsultaConstantes.TIPO_REPORTE, reporteDonacionResponse.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfAceptacionControl);
		
		log.info(" TERMINO - donacionMap");
		
		return envioDatos;
	}
	


}
