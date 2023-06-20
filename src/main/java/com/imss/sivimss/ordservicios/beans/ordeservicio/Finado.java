package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;
import com.imss.sivimss.ordservicios.model.request.FinadoRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.LogUtil;

@Service
public class Finado {
	
	@Autowired
	private LogUtil logUtil;
	
	private ResultSet rs;
	
	private Statement statement;
	
	private static final Logger log = LoggerFactory.getLogger(Finado.class);

	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	public Integer insertarFinado(FinadoRequest finadoRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException {
		
		try {
			statement = connection.createStatement();
			
    		if (finadoRequest.getIdPersona()==null) {
        		statement.executeUpdate(reglasNegocioRepository.insertarPersona(finadoRequest, idUsuarioAlta),Statement.RETURN_GENERATED_KEYS);
    			rs=statement.getGeneratedKeys();
    			if (rs.next()) {
    				finadoRequest.setIdPersona(rs.getInt(1));
    			} 
            }
    		
    		statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
    		rs=statement.getGeneratedKeys();
    		if (rs.next()) {
    			finadoRequest.getCp().setIdDomicilio(rs.getInt(1));
    		}
    		
    		statement.executeUpdate(reglasNegocioRepository.insertarFinado(finadoRequest,idOrdenServicio,idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
    		rs=statement.getGeneratedKeys();
    		if (rs.next()) {
    			return rs.getInt(1);
    		}
		} finally {
			if (statement!=null) {
				statement.close();
			}
			if (rs!= null) {
				rs.close();
			}
		}
		
		return 0;
	}
}
