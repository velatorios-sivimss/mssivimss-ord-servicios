package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.imss.sivimss.ordservicios.model.request.OperadorRequest;
import com.imss.sivimss.ordservicios.model.request.PersonaRequest;
import com.imss.sivimss.ordservicios.model.request.ReporteDto;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.model.request.VelatorioRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioConsultaODSRepository;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.Database;
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

	@Value("${reporte.orden_servicio}")
	private String reporteOrdenServicio;

	@Value("${reporte.orden_servicio_temp}")
	private String reporteOrdenServicioTemp;

	@Value("${reporte.consulta_ODS}")
	private String reporteConsultaODS;
	
	@Value("${reporte.genera_Tarjeta_Iden}")
	private String reporteGeneraTarjetaIden;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	@Autowired
	private ReglasNegocioConsultaODSRepository rNConsultaODSRepository;
	
	@Autowired
	private ProviderServiceRestTemplate providerServiceRestTemplate;	
	
	@Autowired
	private LogUtil logUtil;

	private Response<Object> response;
	
	@Autowired
	private Database database;

	private Connection connection;

	private ResultSet rs;

	private Statement statement;

	
	private static final Logger log = LoggerFactory.getLogger(OrdenConsultar.class);

	
	private static final String CURP_NO_VALIDO = "34"; // CURP no valido.
	private static final String SERVICIO_RENAPO_NO_DISPONIBLE = "184"; // El servicio de RENAPO no esta disponible.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO= "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String ODS_CANCELADA= "74"; // La Orden de Servicio ha sido cancelada correctamente. 

	private static final String CU024_NOMBRE= "Consulta Orden Servicio: ";
	private static final String GENERAR_DOCUMENTO = "Generar Reporte: " ;
	private static final String GENERA_DOCUMENTO = "Genera_Documento";
	private static final String TIPO_REPORTE = "tipoReporte";
	private static final String RUTA_NOMBRE_REPORTE = "rutaNombreReporte";
	
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
	
	public Response<Object>detalleOrdenServicio(){
		return null;
	}
	
	public Response<Object>consultarOrdenesServicio(){
		return null;
	}
	
	//
	public Response<Object> buscarVelatorios(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarVelatorios", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson= datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			VelatorioRequest velatorioRequest=gson.fromJson(datosJson, VelatorioRequest.class);
			DatosRequest request= new DatosRequest();
			Map<String, Object>parametro=new HashMap<>();
			query = rNConsultaODSRepository.obtenerVelatorios(velatorioRequest.getIdDelegacion());
			String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			parametro.put(AppConstantes.QUERY, encoded);
			request.setDatos(parametro);
			
			response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}
	public Response<Object> buscarFolioODS(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarFolioODS", AppConstantes.CONSULTA, authentication);

			query = rNConsultaODSRepository.obtenerFolioODS();
			DatosRequest request= encodeQuery(query, datosRequest);
			
			response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}

	public Response<Object> buscarContratante(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString()
            		, "buscarContratante", AppConstantes.CONSULTA, authentication);
            
			query = rNConsultaODSRepository.obtenerContratante();
			DatosRequest request= encodeQuery(query, datosRequest);
			
			response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}

	public Response<Object> buscarFinado(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
	try {
        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarFinado", AppConstantes.CONSULTA, authentication);
        query = rNConsultaODSRepository.obtenerFinado();
		DatosRequest request= encodeQuery(query, datosRequest);
		response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
		response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

		return response;
	} catch (Exception e) {
		log.error(AppConstantes.ERROR_QUERY.concat(query));
        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

	}
	}
	public Response<Object> buscarTipoOrden(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
	        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarTipoOrden", AppConstantes.CONSULTA, authentication);

			query = rNConsultaODSRepository.obtenerTipoOrden();
			DatosRequest request= encodeQuery(query, datosRequest);
			response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}
	public Response<Object> buscarUnidadMedica(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
	        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarUnidadMedica", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson= datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			VelatorioRequest finadoRequest=gson.fromJson(datosJson, VelatorioRequest.class);
			query = rNConsultaODSRepository.obtenerUnidadMedica(finadoRequest.getIdDelegacion());
			DatosRequest request= encodeQuery(query, datosRequest);
			response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}
	public Response<Object> buscarContratoConvenio(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
	        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarContratoConvenio", AppConstantes.CONSULTA, authentication);

			query = rNConsultaODSRepository.obtenerContratoConvenio();
			DatosRequest request= encodeQuery(query, datosRequest);
			response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}
	public Response<Object> buscarEstadoODS(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
	        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarEstadoODS", AppConstantes.CONSULTA, authentication);

			query = rNConsultaODSRepository.obtenerEstadoODS();
			DatosRequest request= encodeQuery(query, datosRequest);
			response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}
	public Response<Object> buscarOperadores(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
	        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarOperadores", AppConstantes.CONSULTA, authentication);

			query = rNConsultaODSRepository.obtenerOperadores();
			DatosRequest request= encodeQuery(query, datosRequest);
			response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}
	public Response<Object> generaTarjetaIden(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
	        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "generaTarjetaIden", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson= datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			OperadorRequest operadorRequest = gson.fromJson(datosJson, OperadorRequest.class);
			UsuarioDto usuario = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
			query = rNConsultaODSRepository.generaTarjetaIden(operadorRequest.getIdOperador(),operadorRequest.getIdOrdenServicio(), usuario );
			DatosRequest request= encodeQuery(query, datosRequest);
			response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}
	public Response<Object> buscarODS(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
	        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarODS", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson= datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			ReporteDto filtroODS= gson.fromJson(datosJson, ReporteDto.class);
			query = rNConsultaODSRepository.obtenerODS(filtroODS);
			DatosRequest request= encodeQuery(query, datosRequest);
			response = providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTA_PAGINADO), authentication);
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}

	public Response<Object> buscarCostoCancelarODS(DatosRequest datosRequest, Authentication authentication) throws IOException{
		String query="";
		try {
	        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarCostoCancelarODS", AppConstantes.CONSULTA, authentication);

			query = rNConsultaODSRepository.obtenerCostoCancelacionODS();
			DatosRequest request= encodeQuery(query, datosRequest);
			response = providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			response= MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		}
	}
	public Response<Object> cancelarODS(DatosRequest datosRequest, Authentication authentication) throws IOException, SQLException{
		String query="";
	try {
        logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "cancelarODS", AppConstantes.BAJA, authentication);
		Gson gson= new Gson();
		String datosJson= datosRequest.getDatos().get(AppConstantes.DATOS).toString();
		ReporteDto idOrdenServicio= gson.fromJson(datosJson, ReporteDto.class);
		UsuarioDto usuario = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		connection = database.getConnection();
		statement = connection.createStatement();
		connection.setAutoCommit(false);
		query = rNConsultaODSRepository.cancelarODS(idOrdenServicio, usuario);
		statement.executeUpdate(query);
		query = rNConsultaODSRepository.cancelarCaracteristicasPaquete(idOrdenServicio.getIdOrdenServicio(), usuario);
		statement.executeUpdate(query);
		query = rNConsultaODSRepository.cancelarDetalleCaracteristicasPaquete(idOrdenServicio.getIdOrdenServicio(), usuario);
		statement.executeUpdate(query);
		query = rNConsultaODSRepository.cancelarCaracteristicasPresupuesto(idOrdenServicio.getIdOrdenServicio(), usuario);
		statement.executeUpdate(query);
		query = rNConsultaODSRepository.cancelarDetalleCaracteristicasPresupuesto(idOrdenServicio.getIdOrdenServicio(), usuario);
		statement.executeUpdate(query);
		query = rNConsultaODSRepository.cancelarDonacion(idOrdenServicio.getIdOrdenServicio(), usuario);
		statement.executeUpdate(query);
		query = rNConsultaODSRepository.cancelarInventarioArticulo(idOrdenServicio.getIdOrdenServicio(), usuario);
		statement.executeUpdate(query);

		connection.commit();
		response= new Response<>(false, 200, ODS_CANCELADA,"[]");
		return MensajeResponseUtil.mensajeConsultaResponseObject(response, ODS_CANCELADA);
		
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.BAJA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());

		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
	private DatosRequest encodeQuery(String query, DatosRequest request) {
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	

	public Response<Object> generaReporteTarjetaIdentificacion(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson= new Gson();
		String datosJson= request.getDatos().get(AppConstantes.DATOS).toString();
		OperadorRequest operadorRequest = gson.fromJson(datosJson, OperadorRequest.class);
		UsuarioDto usuario = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		Map<String, Object> envioDatos = new HashMap<>();
		envioDatos.put("idODS", operadorRequest.getIdOrdenServicio());
		envioDatos.put("idOperador", operadorRequest.getIdOperador());
		envioDatos.put("nombreAdmin", "'"+ usuario.getNombre() + "' AS nombreAdmin ");
		envioDatos.put(TIPO_REPORTE, "pdf");
		envioDatos.put(RUTA_NOMBRE_REPORTE, reporteGeneraTarjetaIden);
		try {
			log.info( CU024_NOMBRE + GENERAR_DOCUMENTO + " Reporte Tarjeta Identificacion " );
			logUtil.crearArchivoLog(Level.INFO.toString(), CU024_NOMBRE + GENERAR_DOCUMENTO + " Reporte Tarjeta Identificacion " + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "generaReporteTarjetaIdentificacion", GENERA_DOCUMENTO, authentication);
			response = providerServiceRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
		return   MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			log.error( CU024_NOMBRE + GENERAR_DOCUMENTO);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU024_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"", GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}	
	}
	public Response<Object> generaReporteConsultaODS(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		Map<String, Object> envioDatos = new HashMap<>();
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		String query = rNConsultaODSRepository.generaReporteConsultaODS(reporteDto);
		envioDatos.put("condicion", query);
		envioDatos.put(TIPO_REPORTE, reporteDto.getTipoReporte());
		envioDatos.put(RUTA_NOMBRE_REPORTE, reporteConsultaODS);
		try {
			log.info( CU024_NOMBRE + GENERAR_DOCUMENTO + " Reporte Consulta ODS " );
			logUtil.crearArchivoLog(Level.INFO.toString(), CU024_NOMBRE + GENERAR_DOCUMENTO + " Reporte Consulta ODS  " + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "generarDocumentoOrdenServicio", GENERA_DOCUMENTO, authentication);
			response = providerServiceRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
		return   MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			log.error( CU024_NOMBRE + GENERAR_DOCUMENTO);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU024_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"", GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}	
	}

	public Response<Object> generarDocumentoOrdenServicio(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));

		UsuarioDto usuarioRequest = gson.fromJson(datosJson, UsuarioDto.class);
		
		Map<String, Object> envioDatos = new HashMap<>();
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		envioDatos.put("idOds", reporteDto.getIdOrdenServicio());
		envioDatos.put(TIPO_REPORTE, reporteDto.getTipoReporte());
		envioDatos.put("usuarioSistema", usuarioRequest.getCveMatricula());
		String nombreReporte="";
		if(reporteDto.getEstatus() == 1) {
			nombreReporte = reporteOrdenServicioTemp;
		}else if(reporteDto.getEstatus() == 2) {
			nombreReporte = reporteOrdenServicio;
		}
			envioDatos.put(RUTA_NOMBRE_REPORTE, nombreReporte);
		try {
			log.info( CU024_NOMBRE + GENERAR_DOCUMENTO + " Orden servicio " );
			logUtil.crearArchivoLog(Level.INFO.toString(), CU024_NOMBRE + GENERAR_DOCUMENTO + " Orden servicio " + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "generarDocumentoOrdenServicio", GENERA_DOCUMENTO, authentication);
			response = providerServiceRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
		return   MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			log.error( CU024_NOMBRE + GENERAR_DOCUMENTO);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU024_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),"", GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}	
	}
	
}
