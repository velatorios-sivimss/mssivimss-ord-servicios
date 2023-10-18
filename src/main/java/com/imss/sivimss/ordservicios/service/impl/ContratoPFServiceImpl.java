package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import com.imss.sivimss.ordservicios.beans.ContratoPF;
import com.imss.sivimss.ordservicios.model.request.ContratoPfRequest;
import com.imss.sivimss.ordservicios.model.response.ContratantesBeneficiariosContratoPfResponse;
import com.imss.sivimss.ordservicios.model.response.ContratantesContratoPfResponse;
import com.imss.sivimss.ordservicios.model.response.ContratoPfResponse;
import com.imss.sivimss.ordservicios.model.response.PersonaPfResponse;
import com.imss.sivimss.ordservicios.service.ContratoPFService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.Database;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.MensajeResponseUtil;
import com.imss.sivimss.ordservicios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class ContratoPFServiceImpl implements ContratoPFService{

	@Value("${endpoints.mod-catalogos}")
	private String urlDominio;
	
	private final ProviderServiceRestTemplate providerServiceRestTemplate;
	
	private final ModelMapper modelMapper;
	
	private final LogUtil logUtil;
	
	private final ContratoPF contratoPF = ContratoPF.getInstancia();
	
	private static final String ERROR_SINIESTRO="73";
	
	
	private final Database database;
	
    private ResultSet rs;
	
	private Connection connection; 
	
	private Statement statement;
	
	private static final Logger log = LoggerFactory.getLogger(ContratoPFServiceImpl.class);

	private Response<Object>response;
	
	private static final String VIGENCIA="35"; // El contrato de Previsión Funeraria que deseas utilizar no se encuentra vigente. 
	private static final String BUSQUEDA="45"; // No se encontró información relacionada a tu búsqueda.
	
	public ContratoPFServiceImpl(ProviderServiceRestTemplate providerServiceRestTemplate, ModelMapper modelMapper,
			LogUtil logUtil, Database database) {	
		this.providerServiceRestTemplate = providerServiceRestTemplate;
		this.modelMapper = modelMapper;
		this.logUtil = logUtil;
		this.database=database;
	}



	@SuppressWarnings("unused")
	@Override
	public Response<Object> obtenerContratoPF(DatosRequest request, Authentication authentication) throws IOException, SQLException{
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "obtenerContratoPF", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
			ContratoPfRequest contratoPfRequest= gson.fromJson(datosJson, ContratoPfRequest.class);
			connection = database.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			rs=statement.executeQuery(contratoPF.consultarFolio(contratoPfRequest));
			ContratoPfResponse contratoPfResponse= new ContratoPfResponse();
			List<ContratantesContratoPfResponse>listContratantes= new ArrayList<>();
			if (rs.next()) {
				contratoPfResponse= new ContratoPfResponse();
				contratoPfResponse.setIdContratoPF(rs.getInt(1)); 
				contratoPfResponse.setIdTipoPrevision(rs.getInt(2)); 
				contratoPfResponse.setIdTipoContrato(rs.getInt(3)); 
				contratoPfResponse.setIdVelatorio(rs.getInt(4)); 
				contratoPfResponse.setNombreVelatorio(rs.getString(5)); 
				contratoPfResponse.setVigencia(rs.getString(6)); 
				contratoPfResponse.setEstatus(rs.getInt(7)); 
			}
			
			if (contratoPfResponse.getIdTipoPrevision()!= null && contratoPfResponse.getIdTipoPrevision()==1) {
				if (contratoPfResponse.getVigencia()!=null) {
					LocalDate hoy = LocalDate.now();
					
					LocalDate vigenciaContrato = LocalDate.parse(contratoPfResponse.getVigencia());
					
					if (vigenciaContrato.isBefore(hoy)) {
						response= new Response<>(false, 200, VIGENCIA);
						return response;
					}
					
					response= new Response<>(false, 200, AppConstantes.EXITO,ConvertirGenerico.convertInstanceOfObject(contratoPfResponse));
					return response;
			    }
			}else {
				response= new Response<>(false, 200, AppConstantes.EXITO,ConvertirGenerico.convertInstanceOfObject(contratoPfResponse));
				return response;
			}
			
			response= new Response<>(false, 200, BUSQUEDA);
			return response;
		} catch (Exception e) {
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "obtenerContratoPF", AppConstantes.CONSULTA, authentication);
		    throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}finally {
			if (connection!=null) {
				connection.close();
			}
			if (statement!=null) {
				statement.close();
			}
			if (rs!= null) {
				rs.close();
			}
		}
	}



	@Override
	public Response<Object> obtenerContratantes(DatosRequest request, Authentication authentication)
			throws IOException, SQLException {
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "obtenerContratantes", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
			ContratoPfRequest contratoPfRequest= gson.fromJson(datosJson, ContratoPfRequest.class);
			connection = database.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			rs=statement.executeQuery(contratoPF.consultarContratantesSiniestros(contratoPfRequest.getIdContrato()));
			List<ContratantesContratoPfResponse>listContratantes= new ArrayList<>();
			
			while (rs.next()) {
				listContratantes.add(new ContratantesContratoPfResponse(rs.getInt(1),rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getInt(5)));
			}
				
			response= new Response<>(false, 200, AppConstantes.EXITO,ConvertirGenerico.convertInstanceOfObject(listContratantes));
			return response;
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "obtenerContratantes", AppConstantes.CONSULTA, authentication);
		    throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}finally {
			if (connection!=null) {
				connection.close();
			}
			if (statement!=null) {
				statement.close();
			}
			if (rs!= null) {
				rs.close();
			}
		}
	}



	@Override
	public Response<Object> obtenerContratanteBeneficiarios(DatosRequest request, Authentication authentication)
			throws IOException, SQLException {
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "obtenerContratanteBeneficiarios", AppConstantes.CONSULTA, authentication);

			Gson gson= new Gson();
			String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
			ContratoPfRequest contratoPfRequest= gson.fromJson(datosJson, ContratoPfRequest.class);
			connection = database.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			rs=statement.executeQuery(contratoPF.consultarContratantesBeneficiarios(contratoPfRequest));
			List<ContratantesBeneficiariosContratoPfResponse>listContratantesBeneficiariosContratoPfResponses= new ArrayList<>();
			
			while (rs.next()) {
				listContratantesBeneficiariosContratoPfResponses.add(new ContratantesBeneficiariosContratoPfResponse(rs.getInt(1), rs.getString(2)));
			}
				
			response= new Response<>(false, 200, AppConstantes.EXITO,ConvertirGenerico.convertInstanceOfObject(listContratantesBeneficiariosContratoPfResponses));
			return response;
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "obtenerContratantes", AppConstantes.CONSULTA, authentication);
		    throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}finally {
			if (connection!=null) {
				connection.close();
			}
			if (statement!=null) {
				statement.close();
			}
			if (rs!= null) {
				rs.close();
			}
		}
	}
	
	@Override
	public Response<Object> obtenerPersona(DatosRequest request, Authentication authentication)
			throws IOException, SQLException {
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "obtenerContratanteBeneficiarios", AppConstantes.CONSULTA, authentication);
			
			Gson gson= new Gson();
			String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
			ContratoPfRequest contratoPfRequest= gson.fromJson(datosJson, ContratoPfRequest.class);
			connection = database.getConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			rs=statement.executeQuery(contratoPF.consultarPersona(contratoPfRequest.getIdPersona()));
			List<PersonaPfResponse>listPersonaPfResponses= new ArrayList<>();
			
			while (rs.next()) {
				listPersonaPfResponses.add(new PersonaPfResponse(
						rs.getInt(1), 
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7),
						rs.getString(8),
						rs.getString(9),
						rs.getString(10),
						rs.getString(11),
						rs.getString(12),
						rs.getString(13),
						rs.getString(14),
						rs.getString(15),
						rs.getString(16)
						));
			}
			
			response= new Response<>(false, 200, AppConstantes.EXITO,ConvertirGenerico.convertInstanceOfObject(listPersonaPfResponses));
			return response;
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "obtenerContratantes", AppConstantes.CONSULTA, authentication);
			throw new IOException(AppConstantes.ERROR_CONSULTAR, e.getCause());
		}finally {
			if (connection!=null) {
				connection.close();
			}
			if (statement!=null) {
				statement.close();
			}
			if (rs!= null) {
				rs.close();
			}
		}
	}
	

}
