package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;
import com.imss.sivimss.ordservicios.model.request.OrdenesServicioRequest;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.Database;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class OrdenGuardar {

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;
	
	@Autowired
	private ProviderServiceRestTemplate providerServiceRestTemplate;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	@Autowired
	private Database database;
	
	@Autowired
	private LogUtil logUtil;
	
	private ResultSet rs;
	
	private Connection connection; 
	
	private static final Logger log = LoggerFactory.getLogger(OrdenGuardar.class);

	private Response<Object>response;
	
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

				query=contratoPF(ordenesServicioRequest);
				break;
			case 3:
	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "articulosComplementarios", AppConstantes.ALTA, authentication);

				query=articulosComplementarios(ordenesServicioRequest);
				break;
			case 4:
	            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "convenioPF", AppConstantes.ALTA, authentication);

				query= convenioPF(ordenesServicioRequest);
				break;
			default:
				throw new BadRequestException(HttpStatus.BAD_REQUEST, query);
			}
			DatosRequest request= new DatosRequest();
			Map<String, Object>parametros= new HashMap<>();
			String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			parametros.put(AppConstantes.QUERY, encoded);
			request.setDatos(parametros);
			//return response=providerServiceRestTemplate.consumirServicio(request.getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CREAR_MULTIPLE), authentication);
			return new Response<>(false, 200, AppConstantes.DATOS+usuario.getNombre());
		} catch (Exception e) {
			log.error(AppConstantes.ERROR_QUERY.concat(query));
		    logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + query, AppConstantes.ALTA, authentication);
		    throw new IOException(AppConstantes.ERROR_GUARDAR, e.getCause());
		}
		
	}
	
	private Response<Object> servicioInmediato(OrdenesServicioRequest ordenesServicioRequest, UsuarioDto usuario) throws SQLException{
		Response<Object>response;
		connection = database.getConnection();
		connection.setAutoCommit(false);
        List<PreparedStatement> stmt = new ArrayList<>();
		//contratante
        if (ordenesServicioRequest.getContratante().getIdContratante()==null) {
			stmt.add(connection.prepareStatement(reglasNegocioRepository.insertarPersona(ordenesServicioRequest.getContratante(), usuario.getIdUsuario()), Statement.RETURN_GENERATED_KEYS));
			stmt.get(0).executeUpdate();
			rs=stmt.get(0).getGeneratedKeys();
			if (rs.next()) {
				ordenesServicioRequest.getContratante().setIdPersona(rs.getInt(1));
			} 
			
        }
        stmt.add(connection.prepareStatement(reglasNegocioRepository.insertarDomicilio(ordenesServicioRequest.getContratante().getCp(),usuario.getIdUsuario()), Statement.RETURN_GENERATED_KEYS));
		stmt.get(1).executeUpdate();
		rs=stmt.get(1).getGeneratedKeys();
		if (rs.next()) {
			ordenesServicioRequest.getContratante().setCp(new DomicilioRequest(rs.getInt(1)));
		}
		
		stmt.add(connection.prepareStatement(reglasNegocioRepository.insertarContratante(ordenesServicioRequest.getContratante(),usuario.getIdUsuario()), Statement.RETURN_GENERATED_KEYS));
		stmt.get(2).executeUpdate();
		rs=stmt.get(2).getGeneratedKeys();
		if (rs.next()) {
			ordenesServicioRequest.getContratante().setIdContratante(rs.getInt(1));
		}
		
		// orden de servicio
		//String folio, Integer idContratante, Integer idParentesco, Integer idVelatorio,	Integer idEstatus,Integer idUsuarioAlt
		// generar folio
		if (ordenesServicioRequest.getIdEstatus()!=0 || ordenesServicioRequest.getIdEstatus()!=1) {
			ordenesServicioRequest.setFolio(generarFolio(ordenesServicioRequest.getIdVelatorio(),connection));
		}
		
		stmt.add(connection.prepareStatement(reglasNegocioRepository.insertarOrdenServicio(ordenesServicioRequest.getFolio(),ordenesServicioRequest.getContratante().getIdContratante(),ordenesServicioRequest.getIdParentesco(),ordenesServicioRequest.getIdVelatorio(),ordenesServicioRequest.getIdEstatus(),usuario.getIdUsuario()), Statement.RETURN_GENERATED_KEYS));
		stmt.get(3).executeUpdate();
		rs=stmt.get(3).getGeneratedKeys();
		if (rs.next()) {
			ordenesServicioRequest.setIdOrdenServicio(rs.getInt(1));
		}
		
		//finado
		
		stmt.add(connection.prepareStatement(reglasNegocioRepository.insertarDomicilio(ordenesServicioRequest.getFinado().getCp(),usuario.getIdUsuario()), Statement.RETURN_GENERATED_KEYS));
		stmt.get(4).executeUpdate();
		rs=stmt.get(4).getGeneratedKeys();
		if (rs.next()) {
				ordenesServicioRequest.getFinado().setCp(new DomicilioRequest(rs.getInt(1)));
		}
		
		if (ordenesServicioRequest.getFinado().getIdPersona()==null) {
			stmt.add(connection.prepareStatement(reglasNegocioRepository.insertarPersona(ordenesServicioRequest.getFinado(), usuario.getIdUsuario()), Statement.RETURN_GENERATED_KEYS));
			stmt.get(5).executeUpdate();
			rs=stmt.get(5).getGeneratedKeys();
			if (rs.next()) {
				ordenesServicioRequest.getFinado().setIdPersona(rs.getInt(1));
			} 
		}
		stmt.add(connection.prepareStatement(reglasNegocioRepository.insertarFinado(ordenesServicioRequest.getFinado(), ordenesServicioRequest.getIdOrdenServicio(), usuario.getIdUsuario()), Statement.RETURN_GENERATED_KEYS));
		stmt.get(6).executeUpdate();
		rs=stmt.get(6).getGeneratedKeys();
		if (rs.next()) {
			ordenesServicioRequest.getFinado().setIdFinado(rs.getInt(1));
		}
		//caracteristicas paquete
		//informacion servicio
		connection.commit();
        connection.close();
        rs.close();
        connection.close();
		return new Response<>(false, 200, ordenesServicioRequest.getContratante().toString());
	}
	
	private String contratoPF(OrdenesServicioRequest ordenesServicioRequest){
		//contratante
		//finado
		//caracteristicas paquete
		//informacion servicio
		return "";
	}
	
	private String articulosComplementarios(OrdenesServicioRequest ordenesServicioRequest){
		//contratante
		//finado
		//caracteristicas paquete

		
		return "";
	}
	
	private String convenioPF(OrdenesServicioRequest ordenesServicioRequest){
		return "";
	}
	
	private String generarFolio(Integer idVelatorio, Connection con) throws SQLException {
		Statement statement = null;
		String folio=null;
		try {
				
			statement = con.createStatement();
			
			rs=statement.executeQuery(reglasNegocioRepository.obtenerFolio(idVelatorio));
			
			if (rs.next()) {
				//ordenesServicioRequest.setFolio(String.valueOf(rs.getString("folio")));
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
	
}
