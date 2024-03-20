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
import com.imss.sivimss.ordservicios.util.BitacoraUtil;

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
    				(finadoRequest.getNomPersona()!=null && !finadoRequest.getNomPersona().equals("")) &&
    				(finadoRequest.getSegundoApellido()!=null && !finadoRequest.getSegundoApellido().equals(""))) {
    			
    			
    			
    			rs=statement.executeQuery(reglasNegocioRepository.consultarPersona(ordenServcio.getFinado().getCurp()));
    			
    			if (rs.next()) {
    				finadoRequest.setIdPersona(rs.getInt("idPersona"));
    			}else {
    				statement.executeUpdate(reglasNegocioRepository.insertarPersona(finadoRequest, idUsuarioAlta),Statement.RETURN_GENERATED_KEYS);
        			rs=statement.getGeneratedKeys();
        			if (rs.next()) {
        				finadoRequest.setIdPersona(rs.getInt(1));
        			}
        			String persona= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+finadoRequest.getIdPersona());
    				BitacoraUtil.insertarInformacion(connection, "SVC_PERSONA", 1, null, persona, idUsuarioAlta);
    			}
    			
        		
    			statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
				rs=statement.getGeneratedKeys();
				if (rs.next()) {
    		    	finadoRequest.getCp().setIdDomicilio(rs.getInt(1));
				}
				String domicilio= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());
				BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 1, null, domicilio, idUsuarioAlta);
            }else {
            	if (finadoRequest.getIdPersona()!=null) {
    				String personaAnterior= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+finadoRequest.getIdPersona().toString());

					statement.executeUpdate(reglasNegocioRepository.actualizarPersona(finadoRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
					String personaActual= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+finadoRequest.getIdPersona().toString());

					BitacoraUtil.insertarInformacion(connection, "SVC_PERSONA", 1, personaAnterior, personaActual, idUsuarioAlta);
				    if (Objects.nonNull(finadoRequest.getCp())) {
		    			if (finadoRequest.getCp().getIdDomicilio()==null) {
		    				statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
							rs=statement.getGeneratedKeys();
							if (rs.next()) {
			    		    	finadoRequest.getCp().setIdDomicilio(rs.getInt(1));
							}
							String domicilio= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());
							BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 1, null, domicilio, idUsuarioAlta);
						}else {
							String domicilioAnterior= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());

							statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
							String domicilioActual= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());
							BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 1, domicilioAnterior, domicilioActual, idUsuarioAlta);

						}		
					}
				}	
            }
    	
    		
			statement.executeUpdate(reglasNegocioRepository.insertarFinado(finadoRequest,ordenServcio.getIdOrdenServicio(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
			rs=statement.getGeneratedKeys();
			if (rs.next()) {
				Integer idFinado=rs.getInt(1);
				String finado= BitacoraUtil.consultarInformacion(connection, "SVC_FINADO", "ID_FINADO = "+idFinado);
				BitacoraUtil.insertarInformacion(connection, "SVC_FINADO", 1, null, finado, idUsuarioAlta);

				return idFinado;
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
    			Integer idFinado=rs.getInt(1);
				String finado= BitacoraUtil.consultarInformacion(connection, "SVC_FINADO", "ID_FINADO = "+idFinado);
				BitacoraUtil.insertarInformacion(connection, "SVC_FINADO", 1, null, finado, idUsuarioAlta);

    			return idFinado;
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
			
			String personaAnterior= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+finadoRequest.getIdPersona().toString());

			statement.executeUpdate(reglasNegocioRepository.actualizarPersona(finadoRequest, idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			String personaActual= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+finadoRequest.getIdPersona().toString());
			BitacoraUtil.insertarInformacion(connection, "SVC_PERSONA", 1, personaAnterior, personaActual, idUsuarioAlta);

			if (Objects.nonNull(finadoRequest.getCp())) {
				String domicilio= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());
				statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
				String domicilioActual= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());
				BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 1, domicilio, domicilioActual, idUsuarioAlta);

			}
		
			statement.executeUpdate(reglasNegocioRepository.insertarFinadoPa(finadoRequest,idOrdenServicio,idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
        		
			rs=statement.getGeneratedKeys();
    		if (rs.next()) {
    			Integer idFinado=rs.getInt(1);
    			String finadoActual= BitacoraUtil.consultarInformacion(connection, "SVC_FINADO", "ID_FINADO = "+idFinado);
    			BitacoraUtil.insertarInformacion(connection, "SVC_FINADO", 2, null, finadoActual, idUsuarioAlta);
    			return idFinado;
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
								String personaActual= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+finadoRequest.getIdPersona().toString());
								BitacoraUtil.insertarInformacion(connection, "SVC_FINADO", 1, null, personaActual, idUsuarioAlta);

							} 
						}
	    				
	    			}
	    			
	        		
	    			statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
					rs=statement.getGeneratedKeys();
					if (rs.next()) {
	    		    	finadoRequest.getCp().setIdDomicilio(rs.getInt(1));
	    		    	String domicilioActual= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());
	    		    	BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 1, null, domicilioActual, idUsuarioAlta);


					}
				}else {
					    String domicilioAnterior= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());
						statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(finadoRequest.getCp(), idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);	
						String domicilioActual= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());
						BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 2, domicilioAnterior, domicilioActual, idUsuarioAlta);

						String personaAnterior= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+finadoRequest.getIdPersona().toString());
						statement.executeUpdate(reglasNegocioRepository.actualizarPersona(finadoRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
						String personaActual= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+finadoRequest.getIdPersona().toString());
						BitacoraUtil.insertarInformacion(connection, "SVC_PERSONA", 2, personaAnterior, personaActual, idUsuarioAlta);

				  }
				
			    }
	
			    String finado= BitacoraUtil.consultarInformacion(connection, "SVC_FINADO", "ID_FINADO = "+finadoRequest.getIdFinado());
				statement.executeUpdate(reglasNegocioRepository.actualizarFinado(finadoRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				String finadoActual= BitacoraUtil.consultarInformacion(connection, "SVC_FINADO", "ID_FINADO = "+finadoRequest.getIdFinado());
    			BitacoraUtil.insertarInformacion(connection, "SVC_FINADO", 2, finado, finadoActual, idUsuarioAlta);
			
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
			String personaAnterior= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+finadoRequest.getIdPersona().toString());

			statement.executeUpdate(reglasNegocioRepository.actualizarPersona(finadoRequest, idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			String personaActual= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+finadoRequest.getIdPersona().toString());
			BitacoraUtil.insertarInformacion(connection, "SVC_PERSONA", 2, personaAnterior, personaActual, idUsuarioAlta);

			if (Objects.nonNull(finadoRequest.getCp())) {
				String domicilioAnterior= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());

				statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(finadoRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
				String domicilioActual= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+finadoRequest.getCp().getIdDomicilio().toString());
				BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 2, domicilioAnterior, domicilioActual, idUsuarioAlta);


			}
			String finado= BitacoraUtil.consultarInformacion(connection, "SVC_FINADO", "ID_FINADO = "+finadoRequest.getIdFinado());
			statement.executeUpdate(reglasNegocioRepository.actualizarFinado(finadoRequest,idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
        		
			rs=statement.getGeneratedKeys();
    		if (rs.next()) {
    			Integer idFinado=rs.getInt(1);
    			String finadoActual= BitacoraUtil.consultarInformacion(connection, "SVC_FINADO", "ID_FINADO = "+idFinado);
    			BitacoraUtil.insertarInformacion(connection, "SVC_FINADO", 2, finado, finadoActual, idUsuarioAlta);
    			return idFinado;
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
