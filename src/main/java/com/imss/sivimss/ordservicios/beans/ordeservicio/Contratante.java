package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.ContratanteRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;

@Service
public class Contratante {

	private ResultSet rs;

	private Statement statement;

	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;

	public Integer insertarContratante(ContratanteRequest contratanteRequest, Integer idUsuarioAlta,
			Connection connection) throws SQLException {

		try {
			statement = connection.createStatement();
			if (contratanteRequest.getIdContratante() == null) {
				statement.executeUpdate(reglasNegocioRepository.insertarPersona(contratanteRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					contratanteRequest.setIdPersona(rs.getInt(1));
				}

				statement.executeUpdate(
						reglasNegocioRepository.insertarDomicilio(contratanteRequest.getCp(), idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					contratanteRequest.getCp().setIdDomicilio(rs.getInt(1));
				}

				statement.executeUpdate(reglasNegocioRepository.insertarContratante(contratanteRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
			} else {
				
				statement.executeUpdate(reglasNegocioRepository.actualizarPersona(contratanteRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				
				if (Objects.nonNull(contratanteRequest.getCp())) {
	    			if (contratanteRequest.getCp().getIdDomicilio()==null) {
	    				statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(contratanteRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
						rs=statement.getGeneratedKeys();
						if (rs.next()) {
							contratanteRequest.getCp().setIdDomicilio(rs.getInt(1));
						}
	    				
					}else {
						statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(contratanteRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
					}		
				}
				
				return contratanteRequest.getIdContratante();
			}

		} finally {
			if (statement != null) {
				statement.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return 0;
	}
	
	public Integer actualizarContacto(ContratanteRequest contratanteRequest, Integer idUsuarioAlta,
			Connection connection) throws SQLException {
		try {
			
			statement = connection.createStatement();
			
			
			statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(contratanteRequest.getCp(), idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);

			statement.executeUpdate(reglasNegocioRepository.actualizarPersona(contratanteRequest, idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			
			return contratanteRequest.getIdContratante();
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
