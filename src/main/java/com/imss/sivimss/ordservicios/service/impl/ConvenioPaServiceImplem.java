package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
	
	private ContratanteResponse contratanteResponse;
	
	private DomicilioRequest domicilioRequest;
	
	private LogUtil logUtil;
	
	private static final Logger log = LoggerFactory.getLogger(ConvenioPaServiceImplem.class);
	
	private final String MENSAJE_45="45"; // No se encontró información relacionada a tu búsqueda. 

	private final String MENSAJE_158="158"; // El convenio de Plan de Servicios Funerarios que deseas utilizar cuenta con mensualidades pendientes de pago.

	private String []datosPersona= {"idPersona","idContratante","matricula","curp","nss","nomPersona","primerApellido","segundoApellido","tipo","sexo",
			"otroSexo","fechaNac","nacionalidad","idPais","idEstado","idDomicilio","calle","numExterior","numInterior","cp",
			"colonia","municipio","estado"};
	
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
            StringBuilder whereBeneficiarios=new StringBuilder();
            resultSet= statement.executeQuery(convenioPA.validarConvenio(convenioPARequest.getFolio()));
            
            if (resultSet.next()) {
            
            	if (Objects.nonNull(resultSet.getInt("estatus")) && resultSet.getInt("estatus")!=4) {
					return new Response<>(false, HttpStatus.OK.value(), this.MENSAJE_158);	
				}
            	
            	List<ContratanteResponse>beneficiariosResponse= new ArrayList<>();
            	
            	convenioPAResponse.setIdConvenioPa(resultSet.getInt(1));
            	convenioPAResponse.setFolio(resultSet.getString(2));
				convenioPAResponse.setIdVelatorio(resultSet.getInt(3));
				convenioPAResponse.setNombreVelatorio(resultSet.getString(4));
				
				beneficiariosResponse.add(this.setContratanteResponse(datosPersona));
				convenioPAResponse.setContratante(beneficiariosResponse);
				
				
				if (Objects.nonNull(resultSet.getInt("titularSubstituto")) && resultSet.getInt("titularSubstituto")>0) {
					whereBeneficiarios.append(String.valueOf(resultSet.getInt("titularSubstituto")));
				}
				if (Objects.nonNull(resultSet.getInt("beneficiario1")) && resultSet.getInt("beneficiario1")>0) {
					whereBeneficiarios.append(whereBeneficiarios.length()>0?",".concat(String.valueOf(resultSet.getInt("beneficiario1"))):String.valueOf(resultSet.getInt("beneficiario1")));
				}
				if (Objects.nonNull(resultSet.getInt("beneficiario2")) && resultSet.getInt("beneficiario2")>0) {
					whereBeneficiarios.append(whereBeneficiarios.length()>0?",".concat(String.valueOf(resultSet.getInt("beneficiario2"))):String.valueOf(resultSet.getInt("beneficiario2")));

				}
				resultSet= statement.executeQuery(convenioPA.obtenerBeneficiariosConvenio(whereBeneficiarios.toString()));
				
				while (resultSet.next()) {
					
					beneficiariosResponse.add(this.setContratanteResponse(datosPersona));
				}
				
				
				
				response= new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, ConvertirGenerico.convertInstanceOfObject(convenioPAResponse));
			}else {
				response= new Response<>(false, HttpStatus.OK.value(), this.MENSAJE_45);				
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
	
	private ContratanteResponse setContratanteResponse(String... indices) throws SQLException {
		this.contratanteResponse = new ContratanteResponse();
		this.domicilioRequest= new DomicilioRequest();
		contratanteResponse.setIdPersona(resultSet.getInt(indices[0]));
		contratanteResponse.setIdContratante(resultSet.getInt(indices[1])==0?null:resultSet.getInt(indices[1]));
		contratanteResponse.setMatricula(resultSet.getString(indices[2]));
		contratanteResponse.setCurp(resultSet.getString(indices[3]));
		if (resultSet.getString(indices[4]).equals("null") || resultSet.getString(indices[4]).equals("")) {
			contratanteResponse.setNss(null);
		}else {
			contratanteResponse.setNss(resultSet.getString(indices[4]));
		}
		contratanteResponse.setNomPersona(resultSet.getString(indices[5]));
		contratanteResponse.setPrimerApellido(resultSet.getString(indices[6]));
		contratanteResponse.setSegundoApellido(resultSet.getString(indices[7]));
		contratanteResponse.setTipo(resultSet.getString(indices[8]));
		contratanteResponse.setSexo(resultSet.getString(indices[9]));
		contratanteResponse.setOtroSexo(resultSet.getString(indices[10]));
		contratanteResponse.setFechaNac(resultSet.getString(indices[11]));
		contratanteResponse.setNacionalidad(resultSet.getString(indices[12]));
		contratanteResponse.setIdPais(resultSet.getString(indices[13]));
		contratanteResponse.setIdEstado(resultSet.getString(indices[14]));
		domicilioRequest.setIdDomicilio(resultSet.getInt(indices[15]));
		domicilioRequest.setDesCalle(resultSet.getString(indices[16]));
		domicilioRequest.setNumExterior(resultSet.getString(indices[17]));
		domicilioRequest.setNumInterior(resultSet.getString(indices[18]));
		domicilioRequest.setCodigoPostal(resultSet.getString(indices[19]));
		domicilioRequest.setDesColonia(resultSet.getString(indices[20]));
		domicilioRequest.setDesMunicipio(resultSet.getString(indices[21]));
		domicilioRequest.setDesEstado(resultSet.getString(indices[22]));
		contratanteResponse.setCp(domicilioRequest);
		return contratanteResponse;
	}

}
