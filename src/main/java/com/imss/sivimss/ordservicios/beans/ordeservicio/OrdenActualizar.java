package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
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
import com.imss.sivimss.ordservicios.model.request.OrdenesServicioRequest;
import com.imss.sivimss.ordservicios.model.request.TareasDTO;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.model.response.OrdenServicioResponse;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.Database;
import com.imss.sivimss.ordservicios.util.DatosRequest;
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
	private OrdenGuardar ordenGuardar;

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

	private Connection connection;

	private ResultSet rs;

	private Statement statement;

	@Autowired
	private Database database;

	private Response<Object> response;

	private String cveTarea;

	private static final String EXITO = "47"; // La Orden de Servicio se ha generado exitosamente.

	private Integer idEstatusTipoOrden;

	private Boolean desactivado = false;

	private static final Logger log = LoggerFactory.getLogger(OrdenActualizar.class);

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

				response = ventaArticulos(ordenesServicioRequest, datosJson, usuario, authentication);
				break;
			case 4:
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
						this.getClass().getPackage().toString(), "convenioPF", AppConstantes.ALTA, authentication);

				query = convenioPF(ordenesServicioRequest);
				break;
			default:
				throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
			}

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
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

	private Response<Object> guardarOrdenServicio(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,
			Authentication authentication) throws IOException, SQLException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		// desactiva registros si tipo de orden es diferente
		desactivarRegistros(ordenesServicioRequest, usuario, authentication);

		if (Boolean.TRUE.equals(desactivado)) {
			Integer idOrden = ordenesServicioRequest.getIdOrdenServicio();

			response = insertarOrdenServicios(ordenesServicioRequest, usuario);

			connection.commit();

			// mandar a llamar el job con el id de la orden para cancelarlo

			// mandar a llamar el job con la clave tarea
			if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
				Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
				TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
						"INSERT", datos);
				Response<Object> respuestaProceso = resTemplateProviderServiceRestTemplate
						.consumirServicioProceso(tareas, urlProceso.concat(AppConstantes.PROCESO), authentication);

				return response;

			}
		} else {
			// actualizar registro actual
			actualizarOrdenServicios(ordenesServicioRequest, usuario,authentication);
			connection.commit();
		}
		return response;
	}

	private Response<Object> ventaArticulos(OrdenesServicioRequest ordenesServicioRequest, String datosJson,
			UsuarioDto usuario, Authentication authentication) throws IOException, SQLException {
		connection = database.getConnection();
		connection.setAutoCommit(false);
		// desactiva registros si tipo de orden es diferente
		desactivarRegistros(ordenesServicioRequest, usuario, authentication);
		if (Boolean.TRUE.equals(desactivado)) {
			response = insertarVentaArticulo(ordenesServicioRequest, usuario);
			connection.commit();

			// mandar a llamar el job de desactivar

			// mandar a llamar el job con la clave tarea
			if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
				Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
				TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
						"INSERT", datos);
				Response<Object> respuestaProceso = resTemplateProviderServiceRestTemplate
						.consumirServicioProceso(tareas, urlProceso.concat(AppConstantes.PROCESO), authentication);
				return response;

			}
		} else {
			// actualizar registro actual
			actualizarVentaArticulo(ordenesServicioRequest, usuario,authentication);
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
			finado.insertarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);
		}

		// caracteristicas presupuesto
		if (ordenesServicioRequest.getIdEstatus() == 1) {
			// temporales
			caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);

		} else {
			// buenas buenas
			caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);

		}

		// informacion servicio
		if (ordenesServicioRequest.getInformacionServicio() != null) {
			informacionServicio.insertarInformacionServicio(ordenesServicioRequest.getInformacionServicio(),
					ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}

		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2) {
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
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);

		} else {
			// buenas buenas
			caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
					ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(),
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
			finado.actualizarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);
		}
		
		// caracteristicas presupuesto 
		if (ordenesServicioRequest.getIdEstatus() ==1) { // temporales
		  desactivarRegistrosTemp(ordenesServicioRequest, usuario, authentication);
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(),
		  connection);
		  
		  } else { // buenas buenas
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(),
		  connection);
		  
		  }
		 
		// informacion servicio
		if (ordenesServicioRequest.getInformacionServicio() != null) {
			informacionServicio.actualizarInformacionServicio(ordenesServicioRequest.getInformacionServicio(),
					ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}

		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			insertarPagoBitacora(ordenesServicioRequest, usuario.getIdUsuario(), connection);

		}

		response = consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);

		
		
		// mandar a llamar el job con la clave tarea
		if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
			
			// mandar a llamar el job de desactivar
			
			// mandar a llamar el job con la clave tarea
			Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
			TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"INSERT", datos);
			Response<Object> respuestaProceso = resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareas,
					urlProceso.concat(AppConstantes.PROCESO), authentication);
			return response;

		}

		return response;
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

		// finado
		if (ordenesServicioRequest.getFinado() != null) {
			finado.actualizarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(),
					usuario.getIdUsuario(), connection);
		}
		
		// caracteristicas presupuesto 
		if (ordenesServicioRequest.getIdEstatus() ==1) { // temporales
		  desactivarRegistrosTemp(ordenesServicioRequest, usuario, authentication);
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(),
		  connection);
		  
		  } else { // buenas buenas
		  caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(
		  ordenesServicioRequest.getCaracteristicasPresupuesto(),
		  ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(),
		  connection);
		  
		  }
		 
		// pago bitacora
		if (ordenesServicioRequest.getIdEstatus() == 2) {
			insertarPagoBitacora(ordenesServicioRequest, usuario.getIdUsuario(), connection);

		}

		response = consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);

		
		
		// mandar a llamar el job con la clave tarea
		if (ordenesServicioRequest.getIdEstatus() == 1 && ordenesServicioRequest.getIdOrdenServicio() != null) {
			
			// mandar a llamar el job de desactivar
			
			// mandar a llamar el job con la clave tarea
			Object datos = "{\"idODS\":" + ordenesServicioRequest.getIdOrdenServicio() + "}";
			TareasDTO tareas = new TareasDTO(tipoHoraMinuto, cveTarea, Integer.parseInt(totalHoraMinuto), "ODS",
					"INSERT", datos);
			Response<Object> respuestaProceso = resTemplateProviderServiceRestTemplate.consumirServicioProceso(tareas,
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
				desactivarRegistrosTemp(ordenesServicioRequest, usuario, authentication);
				statement.executeUpdate(
						reglasNegocioRepository.actualizarEstatusOrden(ordenesServicioRequest.getIdOrdenServicio()));
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
	
	private void desactivarRegistrosTemp(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario,
			Authentication authentication) throws SQLException {
		consultarEstatusOrden(ordenesServicioRequest.getIdOrdenServicio());
		try {
			statement = connection.createStatement();
			statement.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPaqueteTemporal(ordenesServicioRequest.getIdOrdenServicio()));
			statement.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPaqueteDetalleTemp(ordenesServicioRequest.getIdOrdenServicio()));
			statement.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPresupuestoTemporal(ordenesServicioRequest.getIdOrdenServicio()));
			statement.executeUpdate(reglasNegocioRepository
					.actualizarCaracteristicasPresuestoDetalleTemp(ordenesServicioRequest.getIdOrdenServicio()));
			statement.executeUpdate(reglasNegocioRepository
					.actualizarDonacionTemporal(ordenesServicioRequest.getIdOrdenServicio()));
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}

	}

	public void consultarEstatusOrden(Integer idOrdenServicio) throws SQLException {
		try {

			statement = connection.createStatement();
			rs = statement.executeQuery(reglasNegocioRepository.consultarOrdenServicio(idOrdenServicio));

			if (rs.next()) {
				idEstatusTipoOrden = rs.getInt("idEstatusOrdenServicio");
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
			statement.executeUpdate(reglasNegocioRepository.actualizarOrdenServicio(
					ordenesServicioRequest.getIdOrdenServicio(), ordenesServicioRequest.getFolio(),
					ordenesServicioRequest.getIdParentesco(), ordenesServicioRequest.getIdContratantePf(),
					ordenesServicioRequest.getIdEstatus(), idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
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

		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
	}

	public Response<Object> consultarOrden(Integer idOrdenServicio, Connection con) throws SQLException {
		try {
			OrdenServicioResponse ordenServicioResponse;
			statement = con.createStatement();
			rs = statement.executeQuery(reglasNegocioRepository.consultarOrdenServicio(idOrdenServicio));

			if (rs.next()) {
				ordenServicioResponse = new OrdenServicioResponse(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4));
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
