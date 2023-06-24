package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.Panteon;
import com.imss.sivimss.ordservicios.model.request.PanteonRequest;
import com.imss.sivimss.ordservicios.model.request.UsuarioDto;
import com.imss.sivimss.ordservicios.model.response.PanteonResponse;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.service.PanteonService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.Database;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;


@Service
public class PanteonServiceImpl implements PanteonService{
	
	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;

	private Panteon panteon= new Panteon();
	
	private final ModelMapper modelMapper;
	
	private static final String AGREGADO_CORRECTAMENTE = "99";
	
	private final LogUtil logUtil;
	
	private ResultSet rs;

	private Statement statement;
	
	@Autowired
	private Database database;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	private static final Logger log = LoggerFactory.getLogger(PanteonServiceImpl.class);

	
	public PanteonServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper, LogUtil logUtil) {
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper=modelMapper;
		this.logUtil=logUtil;
	}

	@Override
	public Response<Object> buscarPanteon(DatosRequest request, Authentication authentication) throws IOException {
		PanteonRequest panteonRequest= new PanteonRequest();
		try {
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "buscarPanteon", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
			List<PanteonResponse>nombrePanteones;
			panteonRequest=gson.fromJson(datosJson, PanteonRequest.class);

			Response<Object>response=providerServiceRestTemplate.consumirServicio(panteon.consultarPanteones(panteonRequest).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			if (response.getCodigo()==200) {
				nombrePanteones=Arrays.asList(modelMapper.map(response.getDatos(), PanteonResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(nombrePanteones));
			}
			
			return MensajeResponseUtil.mensajeConsultaResponseObject(response, AppConstantes.ERROR_CONSULTAR);
		} catch (Exception e) {
			String consulta = panteon.consultarPanteones(panteonRequest).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.CONSULTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}
		
	}

	@Override
	public Response<Object> guardarPanteon(DatosRequest request, Authentication authentication) throws IOException {
		PanteonRequest panteonRequest= new PanteonRequest();
		UsuarioDto usuarioDto= new UsuarioDto();
		Connection connection = database.getConnection();
		try {
			
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "guardarPanteon", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
			String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
			panteonRequest= gson.fromJson(datosJson, PanteonRequest.class);
			
			Response<Object>response=providerServiceRestTemplate.consumirServicio(panteon.buscarPanteon(panteonRequest).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR), authentication);
			if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
				return MensajeResponseUtil.mensajeResponse(response, AGREGADO_CORRECTAMENTE);
			}
			statement.executeUpdate(
					reglasNegocioRepository.insertarDomicilio(panteonRequest.getDomicilio(), usuarioDto.getIdUsuario()),
					Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				panteonRequest.getDomicilio().setIdDomicilio(rs.getInt(1));
			}
			
			statement.executeUpdate(panteon.insertar(panteonRequest, usuarioDto),	Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				panteonRequest.setIdPanteon((rs.getInt(1)));
			}
			connection.commit();
			return MensajeResponseUtil.mensajeResponse( providerServiceRestTemplate.consumirServicio(panteon.buscarPanteonPorId(panteonRequest).getDatos(), urlDominio.concat(AppConstantes.CATALOGO_CONSULTAR),
				authentication),AGREGADO_CORRECTAMENTE);
		} catch (Exception e) {
			
			String consulta = panteon.buscarPanteon(panteonRequest).getDatos().get(AppConstantes.QUERY).toString();
	        String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
	        log.error(AppConstantes.ERROR_QUERY.concat(decoded));
	        logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), AppConstantes.ERROR_LOG_QUERY + decoded, AppConstantes.ALTA, authentication);
	        throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
