package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.DomicilioRequest;
import com.imss.sivimss.ordservicios.model.request.InformacionServicioRequest;
import com.imss.sivimss.ordservicios.model.request.InformacionServicioVelacionRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.LogUtil;



@Service
public class InformacionServicio {
	
	@Autowired
	private LogUtil logUtil;
	
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
				informacionServicioRequest.setIdInformacionServicio(rs.getInt(1));
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
			statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(informacionServicioVelacionRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
			rs=statement.getGeneratedKeys();
			if (rs.next()) {
				informacionServicioVelacionRequest.setCp(new DomicilioRequest(rs.getInt(1)));
			}

			statement.executeUpdate(reglasNegocioRepository.insertarInformacionServicioVelacion(informacionServicioVelacionRequest, idInformacionServicio, idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
			rs=statement.getGeneratedKeys();
			if (rs.next()) {
				informacionServicioVelacionRequest.setIdInformacionServicioVelacion(rs.getInt(1));
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
}
