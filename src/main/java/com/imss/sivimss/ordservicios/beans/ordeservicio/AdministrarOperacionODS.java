package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.model.request.SituarServicio;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.model.response.AdministrarOperacionODSResponse;
import com.imss.sivimss.ordservicios.model.response.CatalogoSituarServicioResponse;
import com.imss.sivimss.ordservicios.model.response.HistorialSituarServicioResponse;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioAdministrarOperacionODSRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.Database;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.Response;

import lombok.AllArgsConstructor;


@Service
public class AdministrarOperacionODS {
	
	
	@Autowired
	private ReglasNegocioAdministrarOperacionODSRepository reglasNegocioAdministrarOperacionODSRepository;
	
	@Autowired
	private Database database;
	
	
	private Statement statement;

	private Connection connection;
	
	private ResultSet rs;
	
	@Autowired
	private LogUtil logUtil;
	
	private String MSG105="105"; //Has registrado correctamente el otorgamiento del servicio.
	private String MSG107="107"; //Se ha quitado correctamente el registro del servicio.
	private String MSG085="85"; //El número de folio no existe. Verifica tu información.
	private String MSG045="45"; //No se encontró información relacionada a tu búsqueda.
	
	private Response<Object>response;
	
	private static final Logger log = LoggerFactory.getLogger(AdministrarOperacionODS.class);

	
	public Response<Object>consultarHistorial(DatosRequest request, Authentication authentication)throws IOException, SQLException{
		try {

			connection = database.getConnection();
			statement=connection.createStatement();
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultarHistorial", AppConstantes.CONSULTA,
					authentication);
			Gson gson = new Gson();
			UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

			String datosJson = request.getDatos().get(AppConstantes.DATOS).toString();

			SituarServicio servicio = gson.fromJson(datosJson, SituarServicio.class);
			AdministrarOperacionODSResponse administrarOperacionODSResponse= new AdministrarOperacionODSResponse();
			List<HistorialSituarServicioResponse>historialSituarServicioResponses= new ArrayList<>();

			rs = statement.executeQuery(reglasNegocioAdministrarOperacionODSRepository
					.consultarContratanteFinado(servicio.getFolio(), usuarioDto.getIdVelatorio()));

			if (rs.next()) {
				
				administrarOperacionODSResponse.setContrante(rs.getString(1));
				administrarOperacionODSResponse.setFinado(rs.getString(2));
				rs = statement.executeQuery(reglasNegocioAdministrarOperacionODSRepository
						.consultarHistorialServicios(servicio.getFolio(), usuarioDto.getIdVelatorio()));

			
				while (rs.next()) {
					HistorialSituarServicioResponse historialSituarServicioResponse = new HistorialSituarServicioResponse(
							rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
					historialSituarServicioResponses.add(historialSituarServicioResponse);
				}

				administrarOperacionODSResponse.setHistorialSituarServicioResponses(historialSituarServicioResponses);
				response = new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,
						ConvertirGenerico.convertInstanceOfObject(administrarOperacionODSResponse));
				connection.commit();
			} else {
				response = new Response<>(false, HttpStatus.OK.value(), MSG085);
			}

			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(AppConstantes.ERROR_CONSULTAR));
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA, authentication);
			throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		} finally {
			if (connection != null) {
				connection.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (rs != null) {
				rs.close();
			}
		}
	}
	
	public Response<Object>consultarTipoServicio(DatosRequest request, Authentication authentication)throws IOException, SQLException{
		try {

			connection = database.getConnection();
			statement=connection.createStatement();
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultarTipoServicio", AppConstantes.CONSULTA,
					authentication);
			Gson gson = new Gson();

			String datosJson = request.getDatos().get(AppConstantes.DATOS).toString();

			SituarServicio servicio = gson.fromJson(datosJson, SituarServicio.class);

			List<CatalogoSituarServicioResponse>catalogoSituarServicioResponses=new ArrayList<>();
			
			rs = statement.executeQuery(reglasNegocioAdministrarOperacionODSRepository
						.consultarServiciosPorIdOrdenServicio(servicio.getIdOrdenServicio()));
				
			while (rs.next()) {
				CatalogoSituarServicioResponse catalogoSituarServicioResponse= new CatalogoSituarServicioResponse(rs.getInt(1), rs.getString(2));
				catalogoSituarServicioResponses.add(catalogoSituarServicioResponse);
			
			}

			response = new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO,ConvertirGenerico.convertInstanceOfObject(catalogoSituarServicioResponses));
			connection.commit();
			
			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(AppConstantes.ERROR_CONSULTAR));
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_CONSULTAR, AppConstantes.CONSULTA, authentication);
			throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		} finally {
			if (connection != null) {
				connection.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (rs != null) {
				rs.close();
			}
		}
	}
	
	public Response<Object>guardarSituarServicio(DatosRequest request, Authentication authentication) throws IOException, SQLException{
		try {
			
			connection = database.getConnection();
			statement=connection.createStatement();
			
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "guardarSituarServicio", AppConstantes.ALTA, authentication);
			Gson gson= new Gson();
			UsuarioDto usuarioDto= gson.fromJson((String)authentication.getPrincipal(), UsuarioDto.class);   
			
			String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
			
			SituarServicio servicio= gson.fromJson(datosJson, SituarServicio.class);
			
			rs=statement.executeQuery(reglasNegocioAdministrarOperacionODSRepository.consultarRegistro(servicio.getIdOrdenServicio(), servicio.getIdTipoServicio()));
			Integer existe=0;
			if (rs.next()) {
				existe=rs.getInt(1);
			}
			
			if (existe==0) {
				statement.executeUpdate(reglasNegocioAdministrarOperacionODSRepository.insertarSituarServicio(servicio,usuarioDto.getIdUsuario()), Statement.RETURN_GENERATED_KEYS);
				
				rs=statement.getGeneratedKeys();
				
				if (rs.next()) {
					rs= statement.executeQuery(reglasNegocioAdministrarOperacionODSRepository.consultarTotalServiciosPorIdOrdenServicio(servicio.getIdOrdenServicio()));
					if (rs.next()) {
						Integer servicios=rs.getInt(1);
						if (servicios>=1) {
							statement.executeUpdate(reglasNegocioAdministrarOperacionODSRepository.actualizarOrdenServicio(
									servicio.getIdOrdenServicio(),3,usuarioDto.getIdUsuario()), Statement.RETURN_GENERATED_KEYS);

						}
						if (servicios==0) {
							statement.executeUpdate(reglasNegocioAdministrarOperacionODSRepository.actualizarOrdenServicio(
									servicio.getIdOrdenServicio(),6,usuarioDto.getIdUsuario()), Statement.RETURN_GENERATED_KEYS);
						}
					}
					response= new Response<>(false, HttpStatus.OK.value(), MSG105);
					connection.commit();
				}else {
					response= new Response<>(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstantes.ERROR_GUARDAR);
				}

			}else {
				response= new Response<>(false, HttpStatus.OK.value(), MSG105);
			}
						
			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(AppConstantes.ERROR_GUARDAR));
			log.error(e.getMessage());
		    logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_GUARDAR, AppConstantes.ALTA, authentication);
		    throw new IOException(AppConstantes.ERROR_GUARDAR, e.getCause());
		}finally {
			if (connection!=null) {
				connection.close();
			}
			
			if (statement!=null) {
				statement.close();
			}
			
			if (rs!=null) {
				rs.close();
			}
		}

	}
	
	public Response<Object>desactivarSituarServicio(DatosRequest request, Authentication authentication) throws IOException, SQLException{
		try {

			connection = database.getConnection();
			statement=connection.createStatement();
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "desactivarSituarServicio", AppConstantes.BAJA,
					authentication);
			Gson gson = new Gson();
			UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

			String datosJson = request.getDatos().get(AppConstantes.DATOS).toString();

			SituarServicio servicio = gson.fromJson(datosJson, SituarServicio.class);

			
			statement.executeUpdate(reglasNegocioAdministrarOperacionODSRepository.actualizarHistorialOrdenServicio(servicio.getIdHistorialServicio(),0,
					usuarioDto.getIdUsuario()));
			response = new Response<>(false, HttpStatus.OK.value(), MSG107);
			connection.commit();
			
			return response;
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(AppConstantes.ERROR_GUARDAR));
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(),
					AppConstantes.ERROR_LOG_QUERY + AppConstantes.ERROR_GUARDAR, AppConstantes.BAJA, authentication);
			throw new IOException(AppConstantes.ERROR_GUARDAR, e.getCause());
		} finally {
			if (connection != null) {
				connection.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (rs != null) {
				rs.close();
			}
		}
	}

}
