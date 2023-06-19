package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.ContratanteRequest;
import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.LogUtil;

@Service
public class Contratante {
	
	@Autowired
	private LogUtil logUtil;
	
	private ResultSet rs;

	private Statement statement;

	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	public Integer insertarContratante(ContratanteRequest contratanteRequest,Integer idUsuarioAlta, Connection connection) throws SQLException{
		
	
        try {	
        	statement = connection.createStatement();
        	if (contratanteRequest.getIdContratante()==null) {
        		statement.executeUpdate(reglasNegocioRepository.insertarPersona(contratanteRequest, idUsuarioAlta),Statement.RETURN_GENERATED_KEYS);
    			rs=statement.getGeneratedKeys();
    			if (rs.next()) {
    				contratanteRequest.setIdPersona(rs.getInt(1));
    			} 
            }
        	statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(contratanteRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
    		rs=statement.getGeneratedKeys();
    		if (rs.next()) {
    			contratanteRequest.setCp(new DomicilioRequest(rs.getInt(1)));
    		}
    		
    		statement.executeUpdate(reglasNegocioRepository.insertarContratante(contratanteRequest,idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
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
