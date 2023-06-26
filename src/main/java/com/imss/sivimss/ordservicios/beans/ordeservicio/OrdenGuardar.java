package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.model.response.OrdenServicioResponse;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.Database;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class OrdenGuardar {

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	@Autowired
	private Database database;
	
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
	
	private ResultSet rs;
	
	private Connection connection; 
	
	private Statement statement;
	
	private static final Logger log = LoggerFactory.getLogger(OrdenGuardar.class);

	private Response<Object>response;
	
	private String cveTarea;
	
	public Response<Object> agregarOrden(DatosRequest datosRequest, Authentication authentication) throws IOException, SQLException{
		
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
	            response=servicioInmediato(ordenesServicioRequest, usuario);
				break;
			case 2:
	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "contratoPF", AppConstantes.ALTA, authentication);

				response=contratoPF(ordenesServicioRequest, usuario);
				break;
			case 3:
	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "articulosComplementarios", AppConstantes.ALTA, authentication);

				response=ventaArticulos(ordenesServicioRequest, usuario);
				break;
			case 4:
	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "convenioPF", AppConstantes.ALTA, authentication);

				query= convenioPF(ordenesServicioRequest);
				break;
			default:
				throw new BadRequestException(HttpStatus.BAD_REQUEST, AppConstantes.ERROR_GUARDAR);
			}
			
			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
			log.error(e.getMessage());
		    logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.ALTA, authentication);
		    throw new IOException(AppConstantes.ERROR_GUARDAR, e.getCause());
		}finally {
			if (connection!=null) {
				connection.close();
			}
		}
		
	}
	
	private Response<Object> servicioInmediato(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario) throws SQLException{
		connection = database.getConnection();
		connection.setAutoCommit(false);
		//contratante
		if (ordenesServicioRequest.getContratante()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST,AppConstantes.ERROR_GUARDAR);
		}
        ordenesServicioRequest.getContratante().setIdContratante(contratante.insertarContratante(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));
		
        
		// orden de servicio
		// generar folio
        if (ordenesServicioRequest.getIdEstatus()==2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(),connection));
		}		
        insertarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);
        
        if (ordenesServicioRequest.getIdEstatus() == 1) {
        	// cve tarea
        	cveTarea=generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}
        
        //finado
        if (ordenesServicioRequest.getFinado()!=null) {
			finado.insertarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}
        
        //caracteristicas presupuesto
        if (ordenesServicioRequest.getIdEstatus()==1) {
			// temporales
        	caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
        	
		}else {
			// buenas buenas
        	caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);

		}
        
        
		//informacion servicio
        if (ordenesServicioRequest.getInformacionServicio()!=null) {
			informacionServicio.insertarInformacionServicio(ordenesServicioRequest.getInformacionServicio(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}
        
       
		response=consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);
		
		connection.commit();
		
		// mandar a llamar el job con la clave tarea
		if (ordenesServicioRequest.getIdEstatus()==1) {
			
		}
		return response;
	}
	
	private Response<Object> contratoPF(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario) throws SQLException{
		connection = database.getConnection();
		connection.setAutoCommit(false);
		//contratante
		if (ordenesServicioRequest.getContratante()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST,AppConstantes.ERROR_GUARDAR);
		}
        ordenesServicioRequest.getContratante().setIdContratante(contratante.insertarContratante(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));
		
        
		// orden de servicio
		// generar folio
        if (ordenesServicioRequest.getIdEstatus()==2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(),connection));
		}		
        insertarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);
        
        if (ordenesServicioRequest.getIdEstatus() == 1) {
        	// cve tarea
        	cveTarea=generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}
        
        //finado
        if (ordenesServicioRequest.getFinado()!=null) {
			finado.insertarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}
        
        //caracteristicas presupuesto
        if (ordenesServicioRequest.getIdEstatus()==1) {
			// temporales
        	caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
        	
		}else {
			// buenas buenas
        	caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);

		}
        
        
		//informacion servicio
        if (ordenesServicioRequest.getInformacionServicio()!=null) {
			informacionServicio.insertarInformacionServicio(ordenesServicioRequest.getInformacionServicio(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}
        
       
		response=consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);
		
		connection.commit();
		
		// mandar a llamar el job con la clave tarea
		if (ordenesServicioRequest.getIdEstatus()==1) {
			
		}
		return response;
	}
	
	private Response<Object> ventaArticulos(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario) throws SQLException{
		connection = database.getConnection();
		connection.setAutoCommit(false);
		
		//contratante
		if (ordenesServicioRequest.getContratante()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST,AppConstantes.ERROR_GUARDAR);
		}
        
		ordenesServicioRequest.getContratante().setIdContratante(contratante.insertarContratante(ordenesServicioRequest.getContratante(), usuario.getIdUsuario(), connection));
		
		// orden de servicio
		// generar folio
        if (ordenesServicioRequest.getIdEstatus()==2) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(),connection));
		}		
        insertarOrdenServicio(ordenesServicioRequest, usuario.getIdRol(), connection);
        
        if (ordenesServicioRequest.getIdEstatus() == 1) {
        	// cve tarea
        	cveTarea=generarCveTarea(ordenesServicioRequest.getIdOrdenServicio(), connection);
		}
        
        //finado
        if (ordenesServicioRequest.getFinado()!=null) {
			finado.insertarFinadoVentaArticulo(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
		}
        
        //caracteristicas presupuesto
        if (ordenesServicioRequest.getIdEstatus()==1) {
			// temporales
        	caracteristicasPresupuesto.insertarCaracteristicasPresupuestoTemp(ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);
        	
		}else {
			// buenas buenas
        	caracteristicasPresupuesto.insertarCaracteristicasPresupuesto(ordenesServicioRequest.getCaracteristicasPresupuesto(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario(), connection);

		}

	    response=consultarOrden(ordenesServicioRequest.getIdOrdenServicio(), connection);
		
		connection.commit();
		
		// mandar a llamar el job con la clave tarea
		if (ordenesServicioRequest.getIdOrdenServicio()==1) {
			
		}
		return response;
	}
	
	private String convenioPF(OrdenesServicioRequest ordenesServicioRequest){
		return "";
	}
	
	private String generarFolio(Integer idVelatorio, Connection con) throws SQLException {
		String folio=null;
		try {
			statement = con.createStatement();
			rs=statement.executeQuery(reglasNegocioRepository.obtenerFolio(idVelatorio));
			
			if (rs.next()) {
				folio=rs.getString("folio");
			}
		} finally {
			if (statement!=null) {
				statement.close();
			}
			if (rs!= null) {
				rs.close();
			}
		}
		return folio;
	}
	
	private String generarCveTarea(Integer ordenServicio, Connection con) throws SQLException {
		try {
			
			// generar una funcion de 10 caracteres random
        	String cveTarea="OD"+ordenServicio+OrdenesServicioUtil.cadenaAleatoria(8);
        	// concatenar el id de la orden mas los 10 caracteres
        	
			// hacer update en la tabla orden de servicio al campo cve_tarea
        	statement = con.createStatement();
			statement.executeQuery(reglasNegocioRepository.actualizarCveTarea(ordenServicio,cveTarea));
	        return cveTarea;
		} finally {
			if (statement!=null) {
				statement.close();
			}
			if (rs!= null) {
				rs.close();
			}
		}
		
		
		
	}
	
	private void insertarOrdenServicio(OrdenesServicioRequest ordenesServicioRequest, Integer idUsuarioAlta, Connection con) throws SQLException{
		try {
			statement = con.createStatement();
			statement.executeUpdate(reglasNegocioRepository.insertarOrdenServicio(
					ordenesServicioRequest.getFolio(),ordenesServicioRequest.getContratante().getIdContratante(),ordenesServicioRequest.getIdParentesco(),ordenesServicioRequest.getIdVelatorio(),
					ordenesServicioRequest.getIdEstatus(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
			rs=statement.getGeneratedKeys();
			if (rs.next()) {
				ordenesServicioRequest.setIdOrdenServicio(rs.getInt(1));
			}
		} finally {
			if (statement!=null) {
				statement.close();
			}
			if (rs!= null) {
				rs.close();
			}
		}
	}
	
	public Response<Object> consultarOrden(Integer idOrdenServicio, Connection con)throws SQLException{
		try {
			OrdenServicioResponse ordenServicioResponse;
        	statement = con.createStatement();
			rs=statement.executeQuery(reglasNegocioRepository.consultarOrdenServicio(idOrdenServicio));
	
			if (rs.next()) {
				ordenServicioResponse= new OrdenServicioResponse(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
				response= new Response<>(false, 200, AppConstantes.EXITO, ConvertirGenerico.convertInstanceOfObject(ordenServicioResponse));
				
			}
			return response;
		}finally {
			if (statement!=null) {
				statement.close();
			}
			if (rs!= null) {
				rs.close();
			}
		}
	}
	
}
