package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.ordservicios.beans.ConvenioPA;
import com.imss.sivimss.ordservicios.model.request.ConvenioPARequest;
import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;
import com.imss.sivimss.ordservicios.model.response.ContratanteResponse;
import com.imss.sivimss.ordservicios.model.response.ConvenioPAResponse;
import com.imss.sivimss.ordservicios.service.ConvenioPaService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
import com.imss.sivimss.ordservicios.util.ConvertirGenerico;
import com.imss.sivimss.ordservicios.util.Database;
import com.imss.sivimss.ordservicios.util.DatosRequest;
import com.imss.sivimss.ordservicios.util.LogUtil;
import com.imss.sivimss.ordservicios.util.Response;

@Service
public class ConvenioPaServiceImplem implements ConvenioPaService {

	private ConvenioPA convenioPA=ConvenioPA.getInstance();
	
	private Database database;
	
	private Connection connection;
	
	private ResultSet resultSet;
	
	private Statement statement;
	
	private LogUtil logUtil;
	
	private static final Logger log = LoggerFactory.getLogger(ConvenioPaServiceImplem.class);

	
	public ConvenioPaServiceImplem(Database database, LogUtil logUtil) {
		super();
		this.database = database;
		this.logUtil = logUtil;
	}

	private Response<Object>response;
	
	@Override
	public Response<Object> consultarConvenio(DatosRequest request, Authentication authentication)
			throws IOException, SQLException {
		try {
			connection=database.getConnection(); 
			connection.setAutoCommit(false);
			statement=connection.createStatement();
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarConvenio", AppConstantes.CONSULTA, authentication);
            Gson gson= new Gson();
            String datosJson=request.getDatos().get(AppConstantes.DATOS).toString();
            ConvenioPARequest convenioPARequest= gson.fromJson(datosJson, ConvenioPARequest.class);
            ConvenioPAResponse convenioPAResponse= new ConvenioPAResponse();
            
            resultSet= statement.executeQuery(convenioPA.obtenerTitularConvenio(convenioPARequest.getFolio()));
            
            if (resultSet.next()) {
            	ContratanteResponse contratanteResponse= new ContratanteResponse();
				DomicilioRequest domicilioRequest= new DomicilioRequest();
            	convenioPAResponse.setIdConvenioPa(resultSet.getInt(1));
            	convenioPAResponse.setFolio(resultSet.getString(2));
				convenioPAResponse.setIdVelatorio(resultSet.getInt(3));
				convenioPAResponse.setNombreVelatorio(resultSet.getString(4));
				contratanteResponse.setIdPersona(resultSet.getInt(5));
				contratanteResponse.setIdContratante(resultSet.getInt(6));
				contratanteResponse.setMatricula(resultSet.getString(7));
				contratanteResponse.setCurp(resultSet.getString(8));
				if (resultSet.getString(9).equals("null") || resultSet.getString(9).equals("")) {
					contratanteResponse.setNss(null);
				}else {
					contratanteResponse.setNss(resultSet.getString(9));
				}
				contratanteResponse.setNomPersona(resultSet.getString(10));
				contratanteResponse.setPrimerApellido(resultSet.getString(11));
				contratanteResponse.setSegundoApellido(resultSet.getString(12));
				contratanteResponse.setSexo(resultSet.getString(13));
				contratanteResponse.setOtroSexo(resultSet.getString(14));
				contratanteResponse.setFechaNac(resultSet.getString(15));
				contratanteResponse.setNacionalidad(resultSet.getString(16));
				contratanteResponse.setIdPais(resultSet.getString(17));
				contratanteResponse.setIdEstado(resultSet.getString(18));
				domicilioRequest.setIdDomicilio(resultSet.getInt(19));
				domicilioRequest.setDesCalle(resultSet.getString(20));
				domicilioRequest.setNumExterior(resultSet.getString(21));
				domicilioRequest.setNumInterior(resultSet.getString(22));
				domicilioRequest.setCodigoPostal(resultSet.getString(23));
				domicilioRequest.setDesColonia(resultSet.getString(24));
				domicilioRequest.setDesMunicipio(resultSet.getString(25));
				domicilioRequest.setDesEstado(resultSet.getString(26));
				contratanteResponse.setCp(domicilioRequest);
				convenioPAResponse.setContratante(contratanteResponse);
				
				
				
				response= new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, ConvertirGenerico.convertInstanceOfObject(convenioPAResponse));
			}else {
				response= new Response<>(false, HttpStatus.OK.value(), "158");				
			}
            
            connection.commit();
            
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
			if (statement!=null) {
				statement.close();
			}
		}
	}

}
