package com.imss.sivimss.ordservicios.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.beans.ConvenioPA;
import com.imss.sivimss.ordservicios.service.ConvenioPaService;
import com.imss.sivimss.ordservicios.util.AppConstantes;
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
	
	private Statement st;
	
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
            logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "consultarConvenio", AppConstantes.CONSULTA, authentication);

            
            
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
		}
	}

}
