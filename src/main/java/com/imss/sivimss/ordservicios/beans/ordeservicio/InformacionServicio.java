package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.InformacionServicioRequest;
import com.imss.sivimss.ordservicios.model.request.InformacionServicioVelacionRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.BitacoraUtil;



@Service
public class InformacionServicio {
	
	private ResultSet rs;

	private Statement statement;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	
	public void insertarInformacionServicio(InformacionServicioRequest informacionServicioRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException {
		try {
			statement=connection.createStatement();
			statement.executeUpdate(reglasNegocioRepository.insertarInformacionServicio(informacionServicioRequest, idOrdenServicio, idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
			rs=statement.getGeneratedKeys();
			if (rs.next()) {
				Integer id=rs.getInt(1);
				String contratante= BitacoraUtil.consultarInformacion(connection, "SVC_INFORMACION_SERVICIO", "ID_INFORMACION_SERVICIO = " + id);
				BitacoraUtil.insertarInformacion(connection, "SVC_INFORMACION_SERVICIO", 1, null, contratante, idUsuarioAlta);
				informacionServicioRequest.setIdInformacionServicio(id);
			}
			
			if (informacionServicioRequest.getInformacionServicioVelacion()!=null) {
				insertarInformacionVelacion(informacionServicioRequest.getInformacionServicioVelacion(), informacionServicioRequest.getIdInformacionServicio(), idUsuarioAlta, connection);
			}
			
		} finally {
			if (statement!=null) {
				statement.close();
			}
			
			if (rs!=null) {
				rs.close();
			}
		}
	}
	
	private void insertarInformacionVelacion(InformacionServicioVelacionRequest informacionServicioVelacionRequest,Integer idInformacionServicio, Integer idUsuarioAlta, Connection connection) throws SQLException {
		try {
			statement=connection.createStatement();
			if (informacionServicioVelacionRequest.getCp()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(informacionServicioVelacionRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
				rs=statement.getGeneratedKeys();
				if (rs.next()) {
					Integer id=rs.getInt(1);
					String contratante= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = " + id);
					BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 1, null, contratante, idUsuarioAlta);
				    informacionServicioVelacionRequest.getCp().setIdDomicilio(id);
				}
			}
			

			statement.executeUpdate(reglasNegocioRepository.insertarInformacionServicioVelacion(informacionServicioVelacionRequest, idInformacionServicio, idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
			rs=statement.getGeneratedKeys();
			if (rs.next()) {
				Integer idInformacionSer=rs.getInt(1);
				String contratante= BitacoraUtil.consultarInformacion(connection, "SVC_INF_SERVICIO_VELACION", "ID_INF_SERVICIO_VELACION = " + idInformacionSer);
				BitacoraUtil.insertarInformacion(connection, "SVC_INF_SERVICIO_VELACION", 1, null, contratante, idUsuarioAlta);
				informacionServicioVelacionRequest.setIdInformacionServicioVelacion(idInformacionSer);
			}
		} finally {
			if (statement!=null) {
				statement.close();
			}
			
			if (rs!=null) {
				rs.close();
			}
		}
	}
	
	public void actualizarInformacionServicio(InformacionServicioRequest informacionServicioRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException {
		try {
			statement=connection.createStatement();
			String informacion= BitacoraUtil.consultarInformacion(connection, "SVC_INFORMACION_SERVICIO", "ID_INFORMACION_SERVICIO = "+informacionServicioRequest.getIdInformacionServicio());
			statement.executeUpdate(reglasNegocioRepository.desactivarInformacionServicio(idOrdenServicio, idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
			String informacionActual= BitacoraUtil.consultarInformacion(connection, "SVC_INFORMACION_SERVICIO", "ID_INFORMACION_SERVICIO = "+informacionServicioRequest.getIdInformacionServicio());
			BitacoraUtil.insertarInformacion(connection, "SVC_INFORMACION_SERVICIO", 2, informacion, informacionActual, idUsuarioAlta);

			insertarInformacionServicio(informacionServicioRequest,idOrdenServicio,idUsuarioAlta,connection);
			
		} finally {
			if (statement!=null) {
				statement.close();
			}
			
			if (rs!=null) {
				rs.close();
			}
		}
	}
	
}
