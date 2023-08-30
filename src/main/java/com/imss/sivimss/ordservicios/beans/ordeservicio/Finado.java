package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.exception.BadRequestException;
import com.imss.sivimss.ordservicios.model.request.FinadoRequest;
import com.imss.sivimss.ordservicios.model.request.OrdenesServicioRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.AppConstantes;

@Service
public class Finado {
	
	private ResultSet rs;
	
	private Statement statement;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	
	private static final Logger log = LoggerFactory.getLogger(Finado.class);

	
	public Integer insertarFinado(FinadoRequest finadoRequest, OrdenesServicioRequest ordenServcio, Integer idUsuarioAlta, Connection connection) throws SQLException {
		
		try {
			statement = connection.createStatement();
			
    		if (finadoRequest.getIdPersona()==null && 
    				(finadoRequest.getNomPersona()!=null &&
    				finadoRequest.getSegundoApellido()!=null)) {
    			
    			
    			
    			rs=statement.executeQuery(reglasNegocioRepository.consultarPersona(ordenServcio.getFinado().getCurp()));
    			
    			if (rs.next()) {
    				finadoRequest.setIdPersona(rs.getInt("idPersona"));
    			}else {
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
            }else {
            	if (finadoRequest.getIdPersona()!=null) {
					statement.executeUpdate(reglasNegocioRepository.actualizarPersona(finadoRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				    if (Objects.nonNull(finadoRequest.getCp())) {
		    			if (finadoRequest.getCp().getIdDomicilio()==null) {
		    				statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
							rs=statement.getGeneratedKeys();
							if (rs.next()) {
			    		    	finadoRequest.getCp().setIdDomicilio(rs.getInt(1));
							}
		    				
						}else {
							statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
						}		
					}
				}	
            }
    	
    		
    		
    	
			statement.executeUpdate(reglasNegocioRepository.insertarFinado(finadoRequest,ordenServcio.getIdOrdenServicio(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
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
    
    public Integer insertarFinadoPagosAnticipado(FinadoRequest finadoRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException {
		
		try {
			statement = connection.createStatement();
			log.info("insertarFinadoPagosAnticipado");
						
			if (Objects.isNull(finadoRequest.getIdPersona())) {
				
				throw new BadRequestException(HttpStatus.BAD_REQUEST,AppConstantes.ERROR_GUARDAR);
				
			}
			
			
			statement.executeUpdate(reglasNegocioRepository.actualizarPersona(finadoRequest, idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			if (Objects.nonNull(finadoRequest.getCp())) {
				statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
			}
    		
			statement.executeUpdate(reglasNegocioRepository.insertarFinadoPa(finadoRequest,idOrdenServicio,idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
        		
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
    
    public Integer actualizarFinado(FinadoRequest finadoRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException {
		
		try {	
			statement = connection.createStatement();
			if(!finadoRequest.getExtremidad().equalsIgnoreCase("true")){
				if (Objects.isNull(finadoRequest.getIdPersona())) {
	                rs=statement.executeQuery(reglasNegocioRepository.consultarPersona(finadoRequest.getCurp()));
	    			
	    			if (rs.next()) {
	    				finadoRequest.setIdPersona(rs.getInt("idPersona"));
	    			}else {
	    				if (Objects.nonNull(finadoRequest.getNomPersona()) && !finadoRequest.getNomPersona().equals("")) {
							statement.executeUpdate(reglasNegocioRepository.insertarPersona(finadoRequest, idUsuarioAlta),Statement.RETURN_GENERATED_KEYS);
							rs=statement.getGeneratedKeys();
							if (rs.next()) {
								finadoRequest.setIdPersona(rs.getInt(1));
							} 
						}
	    				
	    			}
	    			
	        		
	    			statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
					rs=statement.getGeneratedKeys();
					if (rs.next()) {
	    		    	finadoRequest.getCp().setIdDomicilio(rs.getInt(1));
					}
				}else {
					
						statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(finadoRequest.getCp(), idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);	
						statement.executeUpdate(reglasNegocioRepository.actualizarPersona(finadoRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				  }
				
			    }
	
				statement.executeUpdate(reglasNegocioRepository.actualizarFinado(finadoRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
			
			
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
    
    public Integer actualizarFinadoPa(FinadoRequest finadoRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException {
		
    	try {
			statement = connection.createStatement();
			log.info("actualizarFinadoPagosAnticipado");
			if (Objects.isNull(finadoRequest.getIdPersona())) {
				
				throw new BadRequestException(HttpStatus.BAD_REQUEST,AppConstantes.ERROR_GUARDAR);
				
			}
			
			statement.executeUpdate(reglasNegocioRepository.actualizarPersona(finadoRequest, idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			if (Objects.nonNull(finadoRequest.getCp())) {
				statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
			}
    		
			statement.executeUpdate(reglasNegocioRepository.actualizarFinado(finadoRequest,idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
        		
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
