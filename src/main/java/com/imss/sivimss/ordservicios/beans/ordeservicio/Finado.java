package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.FinadoRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;

@Service
public class Finado {
	
	private ResultSet rs;
	
	private Statement statement;
	
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
            }else {
            	statement.executeUpdate(reglasNegocioRepository.actualizarPersona(finadoRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
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
	
    public Integer insertarFinadoVentaArticulo(FinadoRequest finadoRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException {
		
		try {
			statement = connection.createStatement();
		
    		statement.executeUpdate(reglasNegocioRepository.insertarFinadoVentaArticulo(finadoRequest,idOrdenServicio,idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
        		
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
