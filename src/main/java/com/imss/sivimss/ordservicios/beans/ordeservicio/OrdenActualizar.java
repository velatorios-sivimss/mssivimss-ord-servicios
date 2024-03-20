package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleTrasladoRequest;
import com.imss.sivimss.ordservicios.model.request.ContratanteRequest;
import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;
import com.imss.sivimss.ordservicios.model.request.OrdenesServicioRequest;
import com.imss.sivimss.ordservicios.model.request.TareasDTO;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.model.response.CaracteristicasPaqueteDetallePresupuestoResponse;
import com.imss.sivimss.ordservicios.model.response.CaracteristicasPaqueteDetalleResponse;
import com.imss.sivimss.ordservicios.model.response.CaracteristicasPaquetePresupuestoResponse;
import com.imss.sivimss.ordservicios.model.response.CaracteristicasPaqueteResponse;
import com.imss.sivimss.ordservicios.model.response.CaracteristicasPresupuestoResponse;
import com.imss.sivimss.ordservicios.model.response.ContratanteResponse;
import com.imss.sivimss.ordservicios.model.response.DetalleOrdenesServicioResponse;
import com.imss.sivimss.ordservicios.model.response.FinadoResponse;
import com.imss.sivimss.ordservicios.model.response.InformacionServicioResponse;
import com.imss.sivimss.ordservicios.model.response.InformacionServicioVelacionResponse;
import com.imss.sivimss.ordservicios.model.response.OrdenServicioResponse;
import com.imss.sivimss.ordservicios.model.response.PanteonResponse;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.BitacoraUtil;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.Database;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.GeneraCredencialesUtil;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class OrdenActualizar {

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;

	@Value("${endpoints.ms-procesos}")
	private String urlProceso;

	@Value("${tipoHoraMinuto}")
	private String tipoHoraMinuto;

	@Value("${totalHoraMinuto}")
	private String totalHoraMinuto;

	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;

	@Autowired
	private ProviderServiceRestTemplate resTemplateProviderServiceRestTemplate;

	@Autowired
	private Contratante contratante;

	@Autowired
	private Finado finado;

	@Autowired
	private CaracteristicasPresupuesto caracteristicasPresupuesto;

	@Autowired
	private InformacionServicio informacionServicio;

	@Autowired
	private LogUtil logUtil;

	private Integer idOrden;
	
	private Connection connection;

	private ResultSet rs;

	private Statement statement;

	@Autowired
	private Database database;

	private Response<Object> response;

	private String cveTarea;

	private static final String EXITO = "47"; // La Orden de Servicio se ha generado exitosamente.
	private static final String BUSQUEDA = "45"; // No se encontró información relacionada a tu búsqueda.

	private Integer idEstatusTipoOrden;

	private Boolean desactivado = false;
	
	

	
	private DomicilioRequest domicilioRequest;
	
    private String user;
	
	private String contrasenia;
	
	private static final Logger log = LoggerFactory.getLogger(OrdenActualizar.class);
	
	@Autowired
	private GeneraCredencialesUtil generaCredencialesUtil;

	public Response<Object> actualizar(DatosRequest datosRequest, Authentication authentication)
			throws IOException, SQLException {
		String query = "El tipo orden de servicio es incorrecto.";

		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "actualizarOrden", AppConstantes.ALTA, authentication);

			Gson gson = new Gson();
			UsuarioDto usuario = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
			String datosJson = datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			OrdenesServicioRequest ordenesServicioRequest = gson.fromJson(datosJson, OrdenesServicioRequest.class);
			ordenesServicioRequest.setIdVelatorio(usuario.getIdVelatorio());

			switch (ordenesServicioRequest.getFinado().getIdTipoOrden()) {
			case 1:
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(), "servicioInmediato", AppConstantes.ALTA,
						authentication);
				response = guardarOrdenServicio(ordenesServicioRequest, usuario, authentication);
				break;
			case 2:
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(), "contratoPF", AppConstantes.ALTA, authentication);

				response = guardarOrdenServicio(ordenesServicioRequest, usuario, authentication);
				break;
			case 3:
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(), "articulosComplementarios", AppConstantes.ALTA,
						authentication);

				response = guardarVentaArticulos(ordenesServicioRequest, datosJson, usuario, authentication);
				break;
			case 4:
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(), "convenioPF", AppConstantes.ALTA, authentication);

				response = guardarOrdenServicio(ordenesServicioRequest, usuario, authentication);
				break;
			default:
				throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
			}

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(AppConstantes.ERROR_GUARDAR.concat(" "+e.getMessage())));
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.ALTA,
					authentication);
			throw new IOException(AppConstantes.ERROR_GUARDAR, e.getCause());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
	
    
	

	public Response<Object> consultarDetallePreOrden(DatosRequest datosRequest,
			Authentication authentication) throws SQLException, IOException {
	
		try {
			connection = database.getConnection();
			connection.setAutoCommit(false);
			Gson gson = new Gson();
			String datosJson = datosRequest.getDatos().get(AppConstantes.DATOS).toString();
			OrdenesServicioRequest ordenesServicioRequest=gson.fromJson(datosJson, OrdenesServicioRequest.class);
			DetalleOrdenesServicioResponse servicioResponse= new DetalleOrdenesServicioResponse();
			ContratanteResponse contratanteResponse= null;
			FinadoResponse finadoResponse=null;
			CaracteristicasPresupuestoResponse caracteristicasPresupuestoResponse=null;
			CaracteristicasPaqueteResponse caracteristicasPaqueteResponse=null;
			CaracteristicasPaquetePresupuestoResponse caracteristicasPaquetePresupuestoResponse=null;
			InformacionServicioResponse informacionServicioResponse=null;

			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultarDetallePreOrden", AppConstantes.CONSULTA, authentication);
			statement = connection.createStatement();
			rs = statement.executeQuery(reglasNegocioRepository.consultarOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));
		
			// orden de servicio
			
		
			if (rs.next()) {
				servicioResponse.setIdOrdenServicio(rs.getInt(1));
				servicioResponse.setFolio(rs.getString(2));
				servicioResponse.setIdParentesco(OrdenesServicioUtil.setValor(rs.getInt(3)));
				servicioResponse.setIdVelatorio(rs.getInt(4));
				servicioResponse.setIdOperador(OrdenesServicioUtil.setValor(rs.getInt(5)));
				servicioResponse.setIdEstatus(rs.getInt(6));
				servicioResponse.setIdContratantePf(OrdenesServicioUtil.setValor(rs.getInt(7)));
				
				// contratante
				contratanteResponse=consultarContratante(ordenesServicioRequest,connection);
				servicioResponse.setContratante(contratanteResponse);
				//finado
				finadoResponse=consultarFinado(ordenesServicioRequest, connection);
				servicioResponse.setFinado(finadoResponse);
				
				// caracteristicas presupuesto paquete
				caracteristicasPresupuestoResponse= new CaracteristicasPresupuestoResponse();
				caracteristicasPaqueteResponse=consultarCaracteristicasPaqueteResponse(ordenesServicioRequest, connection);
				caracteristicasPresupuestoResponse.setCaracteristicasPaqueteResponse(Objects.isNull(caracteristicasPaqueteResponse)?null:caracteristicasPaqueteResponse);

				
			   // caracteristicas presupuesto presupuesto
				caracteristicasPaquetePresupuestoResponse=consultarCaracteristicasPaquetePresupuestoResponse(ordenesServicioRequest, connection);
				caracteristicasPresupuestoResponse.setCaracteristicasDelPresupuesto(Objects.isNull(caracteristicasPaquetePresupuestoResponse)?null:caracteristicasPaquetePresupuestoResponse);
				
				// informacion de servicio
				informacionServicioResponse=consultarinformacionServicioResponse(ordenesServicioRequest, connection);
				servicioResponse.setContratante(contratanteResponse);
				servicioResponse.setFinado(Objects.isNull(finadoResponse)?null:finadoResponse);
				servicioResponse.setCaracteristicasPresupuesto(Objects.isNull(caracteristicasPresupuestoResponse)?null:caracteristicasPresupuestoResponse);
				servicioResponse.setInformacionServicio(Objects.isNull(informacionServicioResponse)?null:informacionServicioResponse);
				response= new Response<>(false, 200, AppConstantes.EXITO, ConvertirGenerico.convertInstanceOfObject(servicioResponse));
			    return response;
			}
			response= new Response<>(false, 500, BUSQUEDA);

			return response;
		}catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(AppConstantes.ERROR_CONSULTAR));
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA,
					authentication);
			throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}finally {
			if (connection != null) {
				connection.close();
			}
			
			if (statement != null) {
				statement.close();
			}
			if (rs != null ) {
				rs.close();
			}
			
			
		}
	}
	
	public ContratanteResponse consultarContratante(OrdenesServicioRequest ordenesServicioRequest, Connection conn) throws SQLException {
		
		try (Statement	statementc = conn.createStatement();ResultSet rsc = statementc.executeQuery(reglasNegocioRepository.consultarContratanteOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));){
			ContratanteResponse contratanteResponse = null;
			log.info("consultarContratante");
		
			// contratante
			if (rsc.next()) {
				contratanteResponse= new ContratanteResponse();
				domicilioRequest= new DomicilioRequest();
				contratanteResponse.setIdPersona(rsc.getInt(1));
				contratanteResponse.setIdContratante(rsc.getInt(2));
				contratanteResponse.setMatricula(rsc.getString(3));
				contratanteResponse.setRfc(rsc.getString(4));
				contratanteResponse.setCurp(rsc.getString(5));
				contratanteResponse.setNomPersona(rsc.getString(6));
				contratanteResponse.setPrimerApellido(rsc.getString(7));
				contratanteResponse.setSegundoApellido(rsc.getString(8));
				contratanteResponse.setSexo(rsc.getString(9));
				contratanteResponse.setOtroSexo(rsc.getString(10));
				contratanteResponse.setFechaNac(rsc.getString(11));
				contratanteResponse.setNacionalidad(rsc.getString(12));
				contratanteResponse.setIdPais(rsc.getString(13));
				contratanteResponse.setIdEstado(rsc.getString(14));
				contratanteResponse.setTelefono(rsc.getString(15));
				contratanteResponse.setCorreo(rsc.getString(16));
				domicilioRequest.setIdDomicilio(rsc.getInt(17));
				domicilioRequest.setDesCalle(rsc.getString(18));
				domicilioRequest.setNumExterior(rsc.getString(19));
				domicilioRequest.setNumInterior(rsc.getString(20));
				domicilioRequest.setCodigoPostal(rsc.getString(21));
				domicilioRequest.setDesColonia(rsc.getString(22));
				domicilioRequest.setDesMunicipio(rsc.getString(23));
				domicilioRequest.setDesEstado(rsc.getString(24));
				
				contratanteResponse.setCp(domicilioRequest.getIdDomicilio()==0 || domicilioRequest.getIdDomicilio()==null?null:domicilioRequest);
				
				
			}
	
			return contratanteResponse;
		}
	}
	
	public FinadoResponse consultarFinado(OrdenesServicioRequest ordenesServicioRequest, Connection conn) throws SQLException {
		try(Statement statementc = conn.createStatement(); ResultSet rsc= statementc.executeQuery(reglasNegocioRepository.consultarFinadoOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));) {
			FinadoResponse finadoResponse = null;
			log.info("consultarFinado");
			// finado
			
				if (rsc.next()) {
					
					finadoResponse= new FinadoResponse();
					domicilioRequest= new DomicilioRequest();
					finadoResponse.setIdFinado(rsc.getInt(1));
					finadoResponse.setIdPersona(rsc.getInt(2)==0?null:rsc.getInt(2));
					finadoResponse.setIdTipoOrden(rsc.getInt(3));
					finadoResponse.setExtremidad(rsc.getString(4));;
					finadoResponse.setEsobito(rsc.getString(5));
					finadoResponse.setMatricula(rsc.getString(6));
					finadoResponse.setRfc(rsc.getString(7));
					finadoResponse.setCurp(rsc.getString(8));
					if (rsc.getString(9).equals("null") || rsc.getString(9).equals("")) {
						finadoResponse.setNss(null);
					}else {
						finadoResponse.setNss(rsc.getString(9));
					}
					finadoResponse.setNomPersona(rsc.getString(10));
					finadoResponse.setPrimerApellido(rsc.getString(11));
					finadoResponse.setSegundoApellido(rsc.getString(12));
					finadoResponse.setSexo(rsc.getString(13));
					finadoResponse.setOtroSexo(rsc.getString(14));
					finadoResponse.setFechaNac(rsc.getString(15));
					finadoResponse.setNacionalidad(rsc.getString(16));
					finadoResponse.setIdPais(rsc.getString(17));
					finadoResponse.setIdEstado(rsc.getString(18));
					finadoResponse.setTelefono(rsc.getString(19));
					finadoResponse.setCorreo(rsc.getString(20));
					domicilioRequest.setIdDomicilio(rsc.getInt(21));
					domicilioRequest.setDesCalle(rsc.getString(22));
					domicilioRequest.setNumExterior(rsc.getString(23));
					domicilioRequest.setNumInterior(rsc.getString(24));
					domicilioRequest.setCodigoPostal(rsc.getString(25));
					domicilioRequest.setDesColonia(rsc.getString(26));
					domicilioRequest.setDesMunicipio(rsc.getString(27));
					domicilioRequest.setDesEstado(rsc.getString(28));
					finadoResponse.setCp(domicilioRequest.getIdDomicilio()==0 || domicilioRequest.getIdDomicilio()==null?null:domicilioRequest);
					finadoResponse.setFechaDeceso(rsc.getString(29));
					finadoResponse.setCausaDeceso(rsc.getString(30));
					finadoResponse.setLugarDeceso(rsc.getString(31));
					finadoResponse.setHora(rsc.getString(32));
					finadoResponse.setIdClinicaAdscripcion(rsc.getString(33)==null?null:rsc.getString(33));
					finadoResponse.setIdUnidadProcedencia(rsc.getString(34)==null?null:rsc.getString(34));
					finadoResponse.setProcedenciaFinado(rsc.getString(35));
					finadoResponse.setIdTipoPension(rsc.getInt(36)==0?null:rsc.getInt(36));
					finadoResponse.setIdContratoPrevision(OrdenesServicioUtil.setValor(rsc.getInt(37)));
					finadoResponse.setNombreVelatorio(rsc.getString(38));					
					finadoResponse.setIdVelatorioContratoPrevision(OrdenesServicioUtil.setValor(rsc.getInt(39)));
					finadoResponse.setIdConvenioPrevision(OrdenesServicioUtil.setValor(rsc.getInt(40)));
					finadoResponse.setFolioContrato(rsc.getString(41));
					finadoResponse.setFolioConvenioPa(rsc.getString(42));
	
					
			}
			return finadoResponse;
		}
	}
	
	public CaracteristicasPaqueteResponse consultarCaracteristicasPaqueteResponse(OrdenesServicioRequest ordenesServicioRequest, Connection conn) throws SQLException {
	
		try(Statement statementc= conn.createStatement();ResultSet rsc = statementc.executeQuery(reglasNegocioRepository.consultarCaracteristicasPresupuestoPaqueteTempOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));) {
			CaracteristicasPaqueteResponse caracteristicasPaqueteResponse=null;
			log.info("consultarCaracteristicasPaqueteResponse");
			
			if (rsc.next()) {
				
				caracteristicasPaqueteResponse= new CaracteristicasPaqueteResponse();
				caracteristicasPaqueteResponse.setIdCaracteristicasPaquete(rsc.getInt(1));
				caracteristicasPaqueteResponse.setIdPaquete(rsc.getInt(2));
				caracteristicasPaqueteResponse.setOtorgamiento(rsc.getString(3));
				
				
				caracteristicasPaqueteResponse.setDetallePaquete(consultarCaracteristicasPaqueteDetalleResponse(caracteristicasPaqueteResponse, conn));
			}
			
			return caracteristicasPaqueteResponse;
		
		}
	}
	
	public List<CaracteristicasPaqueteDetalleResponse> consultarCaracteristicasPaqueteDetalleResponse(CaracteristicasPaqueteResponse caracteristicasPaqueteResponse, Connection conn) throws SQLException {
	
		try(Statement statementc= conn.createStatement();ResultSet resultSetDetalle = statementc.executeQuery(reglasNegocioRepository.
				consultarCaracteristicasPresupuestoDetallePaqueteTempOrdenServicios(caracteristicasPaqueteResponse.getIdCaracteristicasPaquete()));) {
			
			List<CaracteristicasPaqueteDetalleResponse> caracteristicasPaqueteDetalleResponse=null;
			CaracteristicasPaqueteDetalleTrasladoRequest caracteristicasPaqueteDetalleTrasladoRequest=null;
			log.info("consultarCaracteristicasPaqueteResponse");

			caracteristicasPaqueteDetalleResponse= new ArrayList<>();
			CaracteristicasPaqueteDetalleResponse detalleResponse=null;
				
				while (resultSetDetalle.next()) {
					detalleResponse= new CaracteristicasPaqueteDetalleResponse();
					detalleResponse.setIdPaqueteDetalle(resultSetDetalle.getInt(1));
					detalleResponse.setIdArticulo(resultSetDetalle.getInt(2)==0?null:resultSetDetalle.getInt(2));
					detalleResponse.setIdServicio(resultSetDetalle.getInt(3)==0?null:resultSetDetalle.getInt(3));
					detalleResponse.setIdTipoServicio(resultSetDetalle.getInt(4)==0?null:resultSetDetalle.getInt(4));
					detalleResponse.setGrupo(resultSetDetalle.getString(5));
					detalleResponse.setConcepto(resultSetDetalle.getString(6));
					detalleResponse.setDesmotivo(resultSetDetalle.getString(7));
					detalleResponse.setActivo(resultSetDetalle.getInt(8));
					detalleResponse.setCantidad(resultSetDetalle.getInt(9));
					detalleResponse.setIdProveedor(resultSetDetalle.getInt(10)==0?null:resultSetDetalle.getInt(10));
					detalleResponse.setNombreProveedor(resultSetDetalle.getString(11));
					detalleResponse.setImporteMonto(resultSetDetalle.getDouble(12));
					detalleResponse.setTotalPaquete(resultSetDetalle.getDouble(13));
					detalleResponse.setAgregado(resultSetDetalle.getBoolean(14));
					detalleResponse.setIdCategoriaPaquete(resultSetDetalle.getInt(15)==0?null:resultSetDetalle.getInt(15));
					
					try(ResultSet resultSetDetalleTraslado = statementc.executeQuery(reglasNegocioRepository.consultarCaracteristicasPresupuestoDetallePaqueteTrasladoTempOrdenServicios(detalleResponse.getIdPaqueteDetalle()));){
					caracteristicasPaqueteDetalleTrasladoRequest= null;
						if (resultSetDetalleTraslado.next()) {
							caracteristicasPaqueteDetalleTrasladoRequest= new CaracteristicasPaqueteDetalleTrasladoRequest();
							caracteristicasPaqueteDetalleTrasladoRequest.setIdCaracteristicasPaqueteDetalleTraslado(resultSetDetalleTraslado.getInt(1));
							caracteristicasPaqueteDetalleTrasladoRequest.setOrigen(resultSetDetalleTraslado.getString(2));
							caracteristicasPaqueteDetalleTrasladoRequest.setDestino(resultSetDetalleTraslado.getString(3));
							caracteristicasPaqueteDetalleTrasladoRequest.setTotalKilometros(resultSetDetalleTraslado.getString(4));
							caracteristicasPaqueteDetalleTrasladoRequest.setLatitudInicial(resultSetDetalleTraslado.getDouble(5));
							caracteristicasPaqueteDetalleTrasladoRequest.setLatitudFinal(resultSetDetalleTraslado.getDouble(6));
							caracteristicasPaqueteDetalleTrasladoRequest.setLongitudInicial(resultSetDetalleTraslado.getDouble(7));
							caracteristicasPaqueteDetalleTrasladoRequest.setLongitudFinal(resultSetDetalleTraslado.getDouble(8));
							caracteristicasPaqueteDetalleTrasladoRequest.setIdDetalleCaracteristicas(detalleResponse.getIdPaqueteDetalle());
						}
					}
					detalleResponse.setServicioDetalleTraslado(Objects.isNull(caracteristicasPaqueteDetalleTrasladoRequest)?null:caracteristicasPaqueteDetalleTrasladoRequest);
					caracteristicasPaqueteDetalleResponse.add(detalleResponse);
				}
			
			return caracteristicasPaqueteDetalleResponse;
		
		}
	}
	
	public CaracteristicasPaquetePresupuestoResponse consultarCaracteristicasPaquetePresupuestoResponse(OrdenesServicioRequest ordenesServicioRequest, Connection conn) throws SQLException {
			
		try(Statement statementc = conn.createStatement();ResultSet rsc = statementc.executeQuery(reglasNegocioRepository.consultarCaracteristicasPresupuestoPresupuestoTempOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));) {
			CaracteristicasPaquetePresupuestoResponse caracteristicasPaquetePresupuestoResponse=null;
		
			log.info("consultarCaracteristicasPaquetePresupuestoResponse");

			if (rsc.next()) {
				caracteristicasPaquetePresupuestoResponse= new CaracteristicasPaquetePresupuestoResponse();
				caracteristicasPaquetePresupuestoResponse.setIdCaracteristicasPresupuesto(rsc.getInt(1));
				caracteristicasPaquetePresupuestoResponse.setIdPaquete(rsc.getInt(2));
				caracteristicasPaquetePresupuestoResponse.setTotalPresupuesto(rsc.getString(3));
				caracteristicasPaquetePresupuestoResponse.setObservaciones(rsc.getString(4));
				caracteristicasPaquetePresupuestoResponse.setNotasServicio(rsc.getString(5));
				caracteristicasPaquetePresupuestoResponse.setDetallePresupuesto(consultarCaracteristicasPaqueteDetallePresupuestoResponse(caracteristicasPaquetePresupuestoResponse, ordenesServicioRequest, conn));
			}
			return caracteristicasPaquetePresupuestoResponse;
		
		}
	}
	
	public List<CaracteristicasPaqueteDetallePresupuestoResponse> consultarCaracteristicasPaqueteDetallePresupuestoResponse(CaracteristicasPaquetePresupuestoResponse caracteristicasPaquetePresupuestoResponse,OrdenesServicioRequest ordenesServicioRequest, Connection conn) throws SQLException {
		
		try(Statement statementc = conn.createStatement();ResultSet resultSetDetallePresupuesto = statementc.executeQuery(reglasNegocioRepository.consultarCaracteristicasPresupuestoDetallePresupuestoTempOrdenServicios(caracteristicasPaquetePresupuestoResponse.getIdCaracteristicasPresupuesto(),ordenesServicioRequest.getIdOrdenServicio()));) {
			List<CaracteristicasPaqueteDetallePresupuestoResponse> caracteristicasPaqueteDetallePresupuestoResponse= new ArrayList<>();
			CaracteristicasPaqueteDetallePresupuestoResponse paqueteDetallePresupuesto;
			CaracteristicasPaqueteDetalleTrasladoRequest caracteristicasPresupuestoDetalleTrasladoRequest=null;
			log.info("consultarCaracteristicasPaquetePresupuestoResponse");

			
			while (resultSetDetallePresupuesto.next()) {
					paqueteDetallePresupuesto= new CaracteristicasPaqueteDetallePresupuestoResponse();
					paqueteDetallePresupuesto.setIdPaqueteDetallePresupuesto(resultSetDetallePresupuesto.getInt(1));
					paqueteDetallePresupuesto.setIdCategoria(resultSetDetallePresupuesto.getInt(2)==0?null:resultSetDetallePresupuesto.getInt(2));
					paqueteDetallePresupuesto.setIdArticulo(resultSetDetallePresupuesto.getInt(3)==0?null:resultSetDetallePresupuesto.getInt(3));
					paqueteDetallePresupuesto.setIdInventario(resultSetDetallePresupuesto.getInt(4)==0?null:resultSetDetallePresupuesto.getInt(4));
					paqueteDetallePresupuesto.setIdServicio(resultSetDetallePresupuesto.getInt(5)==0?null:resultSetDetallePresupuesto.getInt(5));
					paqueteDetallePresupuesto.setIdTipoServicio(resultSetDetallePresupuesto.getInt(6)==0?null:resultSetDetallePresupuesto.getInt(6));
					paqueteDetallePresupuesto.setGrupo(resultSetDetallePresupuesto.getString(7));
					paqueteDetallePresupuesto.setConcepto(resultSetDetallePresupuesto.getString(8));
					paqueteDetallePresupuesto.setCantidad(resultSetDetallePresupuesto.getInt(9));
					paqueteDetallePresupuesto.setIdProveedor(resultSetDetallePresupuesto.getInt(10)==0?null:resultSetDetallePresupuesto.getInt(10));
					paqueteDetallePresupuesto.setNombreProveedor(resultSetDetallePresupuesto.getString(11));
					paqueteDetallePresupuesto.setEsDonado(resultSetDetallePresupuesto.getInt(12));
					paqueteDetallePresupuesto.setImporteMonto(resultSetDetallePresupuesto.getDouble(13));
					paqueteDetallePresupuesto.setProviene(resultSetDetallePresupuesto.getString(14));
					
					try(ResultSet resultSetDetallePresupuestoTraslado = statementc.executeQuery(
							reglasNegocioRepository.consultarCaracteristicasPresupuestoDetallePresupuestoTrasladoTempOrdenServicios(paqueteDetallePresupuesto.getIdPaqueteDetallePresupuesto()));){
					caracteristicasPresupuestoDetalleTrasladoRequest=null;
						if (resultSetDetallePresupuestoTraslado.next()) {
							caracteristicasPresupuestoDetalleTrasladoRequest= new CaracteristicasPaqueteDetalleTrasladoRequest();
							caracteristicasPresupuestoDetalleTrasladoRequest.setIdCaracteristicasPaqueteDetalleTraslado(resultSetDetallePresupuestoTraslado.getInt(1));
							caracteristicasPresupuestoDetalleTrasladoRequest.setOrigen(resultSetDetallePresupuestoTraslado.getString(2));
							caracteristicasPresupuestoDetalleTrasladoRequest.setDestino(resultSetDetallePresupuestoTraslado.getString(3));
							caracteristicasPresupuestoDetalleTrasladoRequest.setTotalKilometros(resultSetDetallePresupuestoTraslado.getString(4));
							caracteristicasPresupuestoDetalleTrasladoRequest.setLatitudInicial(resultSetDetallePresupuestoTraslado.getDouble(5));
							caracteristicasPresupuestoDetalleTrasladoRequest.setLatitudFinal(resultSetDetallePresupuestoTraslado.getDouble(6));
							caracteristicasPresupuestoDetalleTrasladoRequest.setLongitudInicial(resultSetDetallePresupuestoTraslado.getDouble(7));
							caracteristicasPresupuestoDetalleTrasladoRequest.setLongitudFinal(resultSetDetallePresupuestoTraslado.getDouble(8));
							caracteristicasPresupuestoDetalleTrasladoRequest.setIdDetalleCaracteristicas(paqueteDetallePresupuesto.getIdPaqueteDetallePresupuesto());
						}
					}
					paqueteDetallePresupuesto.setServicioDetalleTraslado(Objects.isNull(caracteristicasPresupuestoDetalleTrasladoRequest)?null:caracteristicasPresupuestoDetalleTrasladoRequest);
					caracteristicasPaqueteDetallePresupuestoResponse.add(paqueteDetallePresupuesto);
			}	
			
			return caracteristicasPaqueteDetallePresupuestoResponse;
		
		} 
	}

	public InformacionServicioResponse consultarinformacionServicioResponse(OrdenesServicioRequest ordenesServicioRequest, Connection conn) throws SQLException {
		
		
		ResultSet rscs=null;
		ResultSet result=null;
		try (Statement statementc= conn.createStatement();ResultSet rsc = statementc.executeQuery(reglasNegocioRepository.consultarInformacionServicioOrdenServicios(ordenesServicioRequest.getIdOrdenServicio()));) {
			InformacionServicioResponse informacionServicioResponse=null;
			log.info("consultarinformacionServicioResponse");

		
			// informacion de servicio
			
			if (rsc.next()) {
				informacionServicioResponse= new InformacionServicioResponse();
				informacionServicioResponse.setIdInformacionServicio(rsc.getInt(1));
				informacionServicioResponse.setFechaCortejo(rsc.getString(2));
				informacionServicioResponse.setHoraCortejo(rsc.getString(3));
				informacionServicioResponse.setFechaRecoger(rsc.getString(4));
				informacionServicioResponse.setHoraRecoger(rsc.getString(5));
				
				
				
				informacionServicioResponse.setIdSala(OrdenesServicioUtil.setValor(rsc.getInt(7)));
				informacionServicioResponse.setFechaCremacion(rsc.getString(8));
				informacionServicioResponse.setHoraCremacion(rsc.getString(9));
				informacionServicioResponse.setIdPromotor(OrdenesServicioUtil.setValor(rsc.getInt(10)));
				
				InformacionServicioVelacionResponse informacionServicioVelacionResponse=null;
				if (Objects.nonNull(OrdenesServicioUtil.setValor(rsc.getInt(6)))) {
					Integer idPanteon=rsc.getInt(6);
					result = statementc.executeQuery(reglasNegocioRepository.consultarPanteon(idPanteon));
					PanteonResponse panteonResponse= new PanteonResponse();
					if (result.next()) {
						panteonResponse.setIdPanteon(result.getInt("idPanteon"));
						panteonResponse.setNombrePanteon(result.getString("nombrePanteon"));
						panteonResponse.setDesCalle(result.getString("desCalle"));
						panteonResponse.setNumExterior(result.getString("numExterior"));
						panteonResponse.setNumInterior(result.getString("numInterior"));
						panteonResponse.setDesColonia(result.getString("desColonia"));
						panteonResponse.setCodigoPostal(result.getInt("codigoPostal"));
						panteonResponse.setDesEstado(result.getString("desEstado"));
						panteonResponse.setDesMunicipio(result.getString("desMunicipio"));
						panteonResponse.setNombreContacto(result.getString("nombreContacto"));
						panteonResponse.setNumTelefono(result.getString("numTelefono"));
					}
					informacionServicioResponse.setPanteon(panteonResponse);
				}
				
				// informacion de servicio velacion 
				rscs = statementc.executeQuery(reglasNegocioRepository.consultarInformacionServicioVelacionOrdenServicios(informacionServicioResponse.getIdInformacionServicio()));
				if (rscs.next()) {
					domicilioRequest= new DomicilioRequest();
					informacionServicioVelacionResponse= new InformacionServicioVelacionResponse();
					informacionServicioVelacionResponse.setIdInformacionServicioVelacion(rscs.getInt(1));
					informacionServicioVelacionResponse.setFechaInstalacion(rscs.getString(2));
					informacionServicioVelacionResponse.setHoraInstalacion(rscs.getString(3));
					informacionServicioVelacionResponse.setFechaVelacion(rscs.getString(4));
					informacionServicioVelacionResponse.setHoraVelacion(rscs.getString(5));
					informacionServicioVelacionResponse.setIdCapilla(OrdenesServicioUtil.setValor(rscs.getInt(6)));
					domicilioRequest.setIdDomicilio(rscs.getInt(7));
					domicilioRequest.setDesCalle(rscs.getString(8));
					domicilioRequest.setNumExterior(rscs.getString(9));
					domicilioRequest.setNumInterior(rscs.getString(10));
					domicilioRequest.setCodigoPostal(rscs.getString(11));
					domicilioRequest.setDesColonia(rscs.getString(12));
					domicilioRequest.setDesMunicipio(rscs.getString(13));
					domicilioRequest.setDesEstado(rscs.getString(14));
					informacionServicioVelacionResponse.setCp(domicilioRequest.getIdDomicilio()==0 || domicilioRequest.getIdDomicilio()==null?null:domicilioRequest);
				}
				informacionServicioResponse.setInformacionServicioVelacion(Objects.isNull(informacionServicioVelacionResponse)?null:informacionServicioVelacionResponse);
			  }
			

			return informacionServicioResponse;
		
		} finally {
			
			if (rscs != null) {
				rscs.close();
			}
			
			if (result != null) {
				result.close();
			}
			
		}
	}

	private Response<Object> guardarOrdenServicio(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,
			Authentication authentication) throws IOException, SQLException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		// desactiva registros si tipo de orden es diferente
		desactivarRegistros(ordenesServicioRequest, usuario, authentication);

		if (Boolean.TRUE.equals(desactivado)) {
			idOrden = ordenesServicioRequest.getIdOrdenServicio();
	        desactivarRegistrosTemp(ordenesServicioRequest, usuario);	  
			response = insertarOrdenServicios(ordenesServicioRequest, usuario);

			if (ordenesServicioRequest.getIdEstatus()==2 && ordenesServicioRequest.getIdOrdenServicio()!=null) {
				enviarCuenta(ordenesServicioRequest.getContratante(),connection);
				if (Objects.nonNull(user)) {
					generaCredencialesUtil.enviarCorreo(user, ordenesServicioRequest.getContratante().getCorreo(), ordenesServicioRequest.getContratante().getNomPersona(), ordenesServicioRequest.getContratante().getPrimerApellido(), ordenesServicioRequest.getContratante().getSegundoApellido(), contrasenia);
					
				}
				
			}
			
			connection.commit();

			// mandar a llamar el job con la clave tarea
			if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
				Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
				TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
						"INSERT", datos);
				resTemplateProviderServiceRestTemplate
						.consumirServicioProceso(tareas, urlProceso.concat(AppConstantes.PROCESO), authentication);

			
				return response;

			}
		} else {
			// actualizar registro actual
			actualizarOrdenServicios(ordenesServicioRequest, usuario,authentication);
			
			if (ordenesServicioRequest.getIdEstatus()==2 && ordenesServicioRequest.getIdOrdenServicio()!=null) {
				enviarCuenta(ordenesServicioRequest.getContratante(),connection);
				if (Objects.nonNull(user)) {
					generaCredencialesUtil.enviarCorreo(user, ordenesServicioRequest.getContratante().getCorreo(), ordenesServicioRequest.getContratante().getNomPersona(), ordenesServicioRequest.getContratante().getPrimerApellido(), ordenesServicioRequest.getContratante().getSegundoApellido(), contrasenia);
					
				}
				
			}
			
			connection.commit();
		}
		return response;
	}

	private Response<Object> guardarVentaArticulos(OrdenesServicioRequest ordenesServicioRequest, String datosJson,
			UsuarioDto usuario, Authentication authentication) throws IOException, SQLException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		// desactiva registros si tipo de orden es diferente
		desactivarRegistros(ordenesServicioRequest, usuario, authentication);
		if (Boolean.TRUE.equals(desactivado)) {
			Integer idOrden = ordenesServicioRequest.getIdOrdenServicio();
			response = insertarVentaArticulo(ordenesServicioRequest, usuario);
			
			if (ordenesServicioRequest.getIdEstatus()==2 && ordenesServicioRequest.getIdOrdenServicio()!=null) {
				enviarCuenta(ordenesServicioRequest.getContratante(),connection);
				if (Objects.nonNull(user)) {
					generaCredencialesUtil.enviarCorreo(user, ordenesServicioRequest.getContratante().getCorreo(), ordenesServicioRequest.getContratante().getNomPersona(), ordenesServicioRequest.getContratante().getPrimerApellido(), ordenesServicioRequest.getContratante().getSegundoApellido(), contrasenia);
					
				}
				
			}
			
			connection.commit();

			
			// mandar a llamar el job con la clave tarea
			if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
				Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
				TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
						"INSERT", datos);
				resTemplateProviderServiceRestTemplate
						.consumirServicioProceso(tareas, urlProceso.concat(AppConstantes.PROCESO), authentication);
				return response;

			}
			
		} else {
			// actualizar registro actual
			actualizarVentaArticulo(ordenesServicioRequest, usuario,authentication);
			if (ordenesServicioRequest.getIdEstatus()==2 && ordenesServicioRequest.getIdOrdenServicio()!=null) {
				enviarCuenta(ordenesServicioRequest.getContratante(),connection);
				if (Objects.nonNull(user)) {
					generaCredencialesUtil.enviarCorreo(user, ordenesServicioRequest.getContratante().getCorreo(), ordenesServicioRequest.getContratante().getNomPersona(), ordenesServicioRequest.getContratante().getPrimerApellido(), ordenesServicioRequest.getContratante().getSegundoApellido(), contrasenia);
					
				}
				
			}
			connection.commit();
		}
		return response;
	}

	private String convenioPF(OrdenesServicioRequest ordenesServicioRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	////////////////////// insertar/////////////////////////////////////////////////

	private Response<Object> insertarOrdenServicios(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario)
			throws SQLException {
		// contratante
		if (ordenesServicioRequest.getContratante() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
		}
		ordenesServicioRequest.getContratante().setIdContratante(contratante
				.insertarContratante(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));

		// orden de servicio
		// generar folio
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(), connection));
		}
		insertarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);

		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// cve tarea
			cveTarea = generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}

		// finado
		if (ordenesServicioRequest.getFinado() != null) {
        	if (Objects.nonNull(ordenesServicioRequest.getFinado().getIdTipoOrden()) && ordenesServicioRequest.getFinado().getIdTipoOrden()==4) {
				
        		finado.insertarFinadoPagosAnticipado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
        		
				
			}else {
				finado.insertarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest,
					usuario.getIdUsuario(), connection);
			}
		}

		// caracteristicas presupuesto
		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// temporales
			caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest,
					usuario.getIdUsuario(), connection);

		} else {
			// buenas buenas
			
			caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest,
					usuario.getIdUsuario(), connection);

		}

		// informacion servicio
		if (ordenesServicioRequest.getInformacionServicio() != null) {
			informacionServicio.insertarInformacionServicio(ordenesServicioRequest.getInformacionServicio(),
					ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}

		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2 && ordenesServicioRequest.getFinado().getIdTipoOrden() == 1) {
			insertarPagoBitacora(ordenesServicioRequest, usuario.getIdUsuario(), connection);

		}

		response = consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);

		return response;
	}

	public Response<Object> insertarVentaArticulo(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario)
			throws SQLException {
		// contratante
		if (ordenesServicioRequest.getContratante() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
		}

		ordenesServicioRequest.getContratante().setIdContratante(contratante
				.insertarContratante(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));

		// orden de servicio
		// generar folio
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(), connection));
		}
		insertarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);

		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// cve tarea
			cveTarea = generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}

		// finado
		if (ordenesServicioRequest.getFinado() != null) {
			finado.insertarFinadoVentaArticulo(ordenesServicioRequest.getFinado(),
					ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}

		// caracteristicas presupuesto
		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// temporales
			caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest,
					usuario.getIdUsuario(), connection);

		} else {
			// buenas buenas
			caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest,
					usuario.getIdUsuario(), connection);

		}

		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			insertarPagoBitacora(ordenesServicioRequest, usuario.getIdUsuario(), connection);

		}

		response = consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);
		return response;

	}

	////////////////////////////// actualizar////////////////////////////////////

	private Response<Object> actualizarOrdenServicios(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,Authentication authentication)
			throws SQLException, IOException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		// contratante
		if (ordenesServicioRequest.getContratante() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
		}
		ordenesServicioRequest.getContratante().setIdContratante(contratante
				.actualizarContacto(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));

		// orden de servicio
		// generar folio
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(), connection));
		}

		actualizarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);

		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// cve tarea
			cveTarea = generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}

		// finado
		if (ordenesServicioRequest.getFinado() != null) {
			if (Objects.nonNull(ordenesServicioRequest.getFinado().getIdTipoOrden()) && ordenesServicioRequest.getFinado().getIdTipoOrden()==4) {
				// realizar actualizar
        		finado.actualizarFinadoPa(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
        		
				
			}else {
			    finado.actualizarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);
			}
		}
		
		// caracteristicas presupuesto 
		if (ordenesServicioRequest.getIdEstatus() ==1) { // temporales
		  desactivarRegistrosTemp(ordenesServicioRequest, usuario);
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest, usuario.getIdUsuario(),
		  connection);
		  
		  } else { // buenas buenas
		  desactivarRegistrosTemp(ordenesServicioRequest, usuario);	  
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest, usuario.getIdUsuario(),
		  connection);
		  
		  }
		 
		// informacion servicio
		if (ordenesServicioRequest.getInformacionServicio() != null) {
			informacionServicio.actualizarInformacionServicio(ordenesServicioRequest.getInformacionServicio(),
					ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}

		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2 && ordenesServicioRequest.getFinado().getIdTipoOrden() == 1) {
			insertarPagoBitacora(ordenesServicioRequest, usuario.getIdUsuario(), connection);

		}

		response = consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);

		// mandar a llamar el job de desactivar
		Object datosDesactivar = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
		TareasDTO tareasDesactivar = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"CANCELAR", datosDesactivar);
			resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareasDesactivar,
		urlProceso.concat(AppConstantes.PROCESO), authentication);
		
		
		// mandar a llamar el job con la clave tarea
		if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
			
			// mandar a llamar el job con la clave tarea
			Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
			TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"ACTUALIZAR", datos);
			resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareas,
					urlProceso.concat(AppConstantes.PROCESO), authentication);
			return response;

		}

		return response;
	}
	
	private void enviarCuenta(ContratanteRequest contratanteRequest,Connection conn) throws SQLException, IOException {
		try(Statement statementc= conn.createStatement();ResultSet resultSet=statementc.executeQuery(reglasNegocioRepository.consultarUsuario(contratanteRequest.getIdPersona()))) {
			// validar usuario no existe
			user=null;
			if (!resultSet.next()) {
				log.info("{}",resultSet.getRow());
				// insertar el usuario
			    contrasenia= generaCredencialesUtil.generarContrasenia(contratanteRequest.getNomPersona(),
						contratanteRequest.getPrimerApellido());
				user = generaCredencialesUtil.insertarUser(contratanteRequest.getIdPersona(),
						contratanteRequest.getNomPersona(), contratanteRequest.getPrimerApellido(), contrasenia, contratanteRequest.getIdPersona(), statementc);
				
				
									
			}
		} 
		
	}
	
	private Response<Object> actualizarVentaArticulo(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,Authentication authentication)
			throws SQLException, IOException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		// contratante
		if (ordenesServicioRequest.getContratante() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
		}
		ordenesServicioRequest.getContratante().setIdContratante(contratante
				.actualizarContacto(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));

		// orden de servicio
		// generar folio
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(), connection));
		}

		actualizarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);

		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// cve tarea
			cveTarea = generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}

		// caracteristicas presupuesto 
		if (ordenesServicioRequest.getIdEstatus() ==1) { // temporales
		  desactivarRegistrosTemp(ordenesServicioRequest, usuario);
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest, usuario.getIdUsuario(),
		  connection);
		  
		  } else { // buenas buenas
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest, usuario.getIdUsuario(),
		  connection);  
		}
		 
		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			insertarPagoBitacora(ordenesServicioRequest, usuario.getIdUsuario(), connection);

		}

		response = consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);

		// mandar a llamar el job de desactivar
		Object datosDesactivar = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
		TareasDTO tareasDesactivar = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"CANCELAR", datosDesactivar);
			resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareasDesactivar,
		urlProceso.concat(AppConstantes.PROCESO), authentication);
		
		// mandar a llamar el job con la clave tarea
		if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
			
		
			// mandar a llamar el job con la clave tarea
			Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
			TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"ACTUALIZAR", datos);
			resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareas,
					urlProceso.concat(AppConstantes.PROCESO), authentication);
			return response;

		}

		return response;
	}

	private void desactivarRegistros(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,
			Authentication authentication) throws SQLException {
		consultarEstatusOrden(ordenesServicioRequest.getIdOrdenServicio());
		try {
			if (!(Objects.equals(ordenesServicioRequest.getFinado().getIdTipoOrden(), idEstatusTipoOrden))) {
				statement = connection.createStatement();
				
				desactivarRegistrosTemp(ordenesServicioRequest, usuario);
				
				String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_ORDEN_SERVICIO", "ID_ORDEN_SERVICIO = " + ordenesServicioRequest.getIdOrdenServicio());
				
				statement.executeUpdate(
						reglasNegocioRepository.actualizarEstatusOrden(ordenesServicioRequest.getIdOrdenServicio()));
				String detalleOrden =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_ORDEN_SERVICIO", "ID_ORDEN_SERVICIO = " + ordenesServicioRequest.getIdOrdenServicio());
				BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_ORDEN_SERVICIO", 1, detalle, detalleOrden, usuario.getIdUsuario());
				desactivado = true;
				
				

			}
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}

	}
	
	private void desactivarRegistrosTemp(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario) throws SQLException {
		consultarEstatusOrden(ordenesServicioRequest.getIdOrdenServicio());
		Statement statementc=null;
		try {
			// actualizarCaracteristicasPaqueteTemporal
			String carcatPaqueteTemp= BitacoraUtil.consultarInformacion(connection, "SVC_CARAC_PAQUETE_TEMP", 
					"ID_ORDEN_SERVICIO="+ordenesServicioRequest.getIdOrdenServicio().toString()+" AND IND_ACTIVO = 1");
			BitacoraUtil.insertarInformacion(connection, "SVC_CARAC_PAQUETE_TEMP", 2, null, carcatPaqueteTemp, usuario.getIdUsuario());
			
			// actualizarCaracteristicasPaqueteDetalleTemp
			String carcatDetallePaqueteTemp= BitacoraUtil.consultarInformacion(connection, "SVC_DETALLE_CARAC_PAQ_TEMP", 
					"ID_CARAC_PAQUETE="+" (SELECT DISTINCT ID_CARAC_PAQUETE "
							+ " FROM SVC_CARAC_PAQUETE_TEMP" + " WHERE ID_ORDEN_SERVICIO =" + ordenesServicioRequest.getIdOrdenServicio() + ")"+" AND IND_ACTIVO = 1");
			BitacoraUtil.insertarInformacion(connection, "SVC_DETALLE_CARAC_PAQ_TEMP", 2, null, carcatDetallePaqueteTemp, usuario.getIdUsuario());
			
			// actualizarCaracteristicasPresupuestoTemporal
			String carcatPresupuestoTemp= BitacoraUtil.consultarInformacion(connection, "SVC_CARAC_PRESUP_TEMP", 
					"ID_ORDEN_SERVICIO="+ordenesServicioRequest.getIdOrdenServicio().toString()+" AND IND_ACTIVO = 1");
			BitacoraUtil.insertarInformacion(connection, "SVC_CARAC_PRESUP_TEMP", 2, null, carcatPresupuestoTemp, usuario.getIdUsuario());
			
			// actualizarCaracteristicasPresuestoDetalleTemp
			String carcatDetallePresupuestoTemp= BitacoraUtil.consultarInformacion(connection, "SVC_DETALLE_CARAC_PRESUP_TEMP", 
					"ID_CARAC_PRESUPUESTO="+" (SELECT DISTINCT ID_CARAC_PRESUPUESTO "
							+ " FROM SVC_CARAC_PRESUP_TEMP" + " WHERE ID_ORDEN_SERVICIO =" + ordenesServicioRequest.getIdOrdenServicio() + ")"+" AND IND_ACTIVO = 1");
			BitacoraUtil.insertarInformacion(connection, "SVC_DETALLE_CARAC_PRESUP_TEMP", 2, null, carcatDetallePresupuestoTemp, usuario.getIdUsuario());
			
			// actualizarDonacionTemporal
			String donacion= BitacoraUtil.consultarInformacion(connection, "SVC_DONACION_ORDEN_SERV_TEMP", 
					"ID_ORDEN_SERVICIO = "+ ordenesServicioRequest.getIdOrdenServicio() +" AND IND_ACTIVO = 1");
			BitacoraUtil.insertarInformacion(connection, "SVC_DONACION_ORDEN_SERV_TEMP", 2, null, donacion, usuario.getIdUsuario());
			
			// desactivarInformacionServicio
			String informacion= BitacoraUtil.consultarInformacion(connection, "SVC_INFORMACION_SERVICIO", "ID_INFORMACION_SERVICIO = " + ordenesServicioRequest.getIdOrdenServicio());
			BitacoraUtil.insertarInformacion(connection, "SVC_INFORMACION_SERVICIO", 2, null, informacion, usuario.getIdUsuario());
			
			// desactivarSalidaDonacionTemp
			String salidaDonacion= BitacoraUtil.consultarInformacion(connection, "SVC_SALIDA_DONACION_TEMP", "ID_ORDEN_SERVICIO = " + ordenesServicioRequest.getIdOrdenServicio());
			BitacoraUtil.insertarInformacion(connection, "SVC_SALIDA_DONACION_TEMP", 2, null, salidaDonacion, usuario.getIdUsuario());
			
			statementc = connection.createStatement();
			statementc.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPaqueteTemporal(ordenesServicioRequest.getIdOrdenServicio()));
			statementc.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPaqueteDetalleTemp(ordenesServicioRequest.getIdOrdenServicio()));
			statementc.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPresupuestoTemporal(ordenesServicioRequest.getIdOrdenServicio()));
			statementc.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPresuestoDetalleTemp(ordenesServicioRequest.getIdOrdenServicio()));
			statementc.executeUpdate(reglasNegocioRepository
					.actualizarDonacionTemporal(ordenesServicioRequest.getIdOrdenServicio()));
			statementc.executeUpdate(reglasNegocioRepository
					.desactivarInformacionServicio(ordenesServicioRequest.getIdOrdenServicio(),usuario.getIdUsuario()));
			statementc.executeUpdate(reglasNegocioRepository
					.desactivarSalidaDonacionTemp(ordenesServicioRequest.getIdOrdenServicio(),usuario.getIdUsuario()));
		} finally {
			if (statementc != null) {
				statementc.close();
			}
			
		}

	}

	public void consultarEstatusOrden(Integer idOrdenServicio) throws SQLException {
		
		try(Statement statementc = connection.createStatement(); ResultSet rsc = statementc.executeQuery(reglasNegocioRepository.consultarOrdenServicio(idOrdenServicio));) {

			if (rsc.next()) {
				idEstatusTipoOrden = rsc.getInt("idTipoOrdenServicio");
			}
		
		} 
	}

	////////////////////// insertar
	private String generarFolio(Integer idVelatorio, Connection con) throws SQLException {
		String folio = null;
		try {
			statement = con.createStatement();
			rs = statement.executeQuery(reglasNegocioRepository.obtenerFolio(idVelatorio));

			if (rs.next()) {
				folio = rs.getString("folio");
			}
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return folio;
	}

	private String generarCveTarea(Integer ordenServicio, Connection con) throws SQLException {
		try {

			// generar una funcion de 10 caracteres random
			String cveTarea = "OD" + ordenServicio + OrdenesServicioUtil.cadenaAleatoria(8);
			// concatenar el id de la orden mas los 10 caracteres

			// hacer update en la tabla orden de servicio al campo cve_tarea
			statement = con.createStatement();
			statement.executeQuery(reglasNegocioRepository.actualizarCveTarea(ordenServicio, cveTarea));
			return cveTarea;
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}

	}

	private void insertarOrdenServicio(OrdenesServicioRequest ordenesServicioRequest, Integer idUsuarioAlta,
			Connection con) throws SQLException {
		try {
			statement = con.createStatement();
			statement.executeUpdate(reglasNegocioRepository.insertarOrdenServicio(ordenesServicioRequest.getFolio(),
					ordenesServicioRequest.getContratante().getIdContratante(),
					ordenesServicioRequest.getIdParentesco(), ordenesServicioRequest.getIdVelatorio(),
					ordenesServicioRequest.getIdContratantePf(), ordenesServicioRequest.getIdEstatus(), idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				ordenesServicioRequest.setIdOrdenServicio(rs.getInt(1));
			}
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void actualizarOrdenServicio(OrdenesServicioRequest ordenesServicioRequest, Integer idUsuarioAlta,
			Connection con) throws SQLException {
		try {
			statement = con.createStatement();
			String orden= BitacoraUtil.consultarInformacion(connection, "SVC_ORDEN_SERVICIO", "ID_ORDEN_SERVICIO = "+ordenesServicioRequest.getIdOrdenServicio());

			statement.executeUpdate(reglasNegocioRepository.actualizarOrdenServicio(
					ordenesServicioRequest.getIdOrdenServicio(), ordenesServicioRequest.getFolio(),
					ordenesServicioRequest.getIdParentesco(), ordenesServicioRequest.getIdContratantePf(),
					ordenesServicioRequest.getIdEstatus(), idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
			String ordenActual= BitacoraUtil.consultarInformacion(connection, "SVC_ORDEN_SERVICIO", "ID_ORDEN_SERVICIO = "+ordenesServicioRequest.getIdOrdenServicio());
			BitacoraUtil.insertarInformacion(connection, "SVC_ORDEN_SERVICIO", 2, orden, ordenActual, idUsuarioAlta);

		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void insertarPagoBitacora(OrdenesServicioRequest ordenesServicioRequest, Integer idUsuarioAlta,
			Connection con) throws SQLException {
		try {
			statement = con.createStatement();
			statement.executeUpdate(
					reglasNegocioRepository.insertarBitacoraPago(ordenesServicioRequest.getIdOrdenServicio(),
							ordenesServicioRequest.getIdVelatorio(),
							ordenesServicioRequest.getContratante().getNomPersona() + " "
									+ ordenesServicioRequest.getContratante().getPrimerApellido() + " "
									+ ordenesServicioRequest.getContratante().getSegundoApellido(),
							ordenesServicioRequest.getFolio(),
							ordenesServicioRequest.getCaracteristicasPresupuesto().getCaracteristicasDelPresupuesto()
									.getTotalPresupuesto(),
							ordenesServicioRequest.getIdEstatus(), idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				Integer id=rs.getInt(1);
				String orden= BitacoraUtil.consultarInformacion(connection, "SVT_PAGO_BITACORA", "ID_PAGO_BITACORA = "+id);
				BitacoraUtil.insertarInformacion(connection, "SVT_PAGO_BITACORA", 1, null, orden, idUsuarioAlta);
			}

		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	private Response<Object> consultarOrden(Integer idOrdenServicio, Connection con) throws SQLException {
		try {
			OrdenServicioResponse ordenServicioResponse;
			statement = con.createStatement();
			rs = statement.executeQuery(reglasNegocioRepository.consultarOrdenServicio(idOrdenServicio));

			if (rs.next()) {
				ordenServicioResponse = new OrdenServicioResponse(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4),rs.getInt(5));
				response = new Response<>(false, 200, EXITO,
						ConvertirGenerico.convertInstanceOfObject(ordenServicioResponse));

			}
			return response;
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}
	

	
	

}
