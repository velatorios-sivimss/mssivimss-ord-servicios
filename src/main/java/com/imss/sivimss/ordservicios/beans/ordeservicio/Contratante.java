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
import com.imss.sivimss.ordservicios.util.BitacoraUtil;

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
				String persona= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+contratanteRequest.getIdPersona());
				BitacoraUtil.insertarInformacion(connection, "SVC_PERSONA", 1, null, persona, idUsuarioAlta);
				statement.executeUpdate(
						reglasNegocioRepository.insertarDomicilio(contratanteRequest.getCp(), idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					contratanteRequest.getCp().setIdDomicilio(rs.getInt(1));
				}
				String domicilio= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+contratanteRequest.getCp().getIdDomicilio().toString());
				BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 1, null, domicilio, idUsuarioAlta);
				statement.executeUpdate(reglasNegocioRepository.insertarContratante(contratanteRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					Integer idContratante=rs.getInt(1);
					String contratante= BitacoraUtil.consultarInformacion(connection, "SVC_CONTRATANTE", String.valueOf(idContratante));
					BitacoraUtil.insertarInformacion(connection, "SVC_CONTRATANTE", 1, null, contratante, idUsuarioAlta);

					return idContratante;
				}
			} else {
				String personaAnterior= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+contratanteRequest.getIdPersona().toString());
				
				statement.executeUpdate(reglasNegocioRepository.actualizarPersona(contratanteRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				String personaActual= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+contratanteRequest.getIdPersona().toString());

				BitacoraUtil.insertarInformacion(connection, "SVC_PERSONA", 1, personaAnterior, personaActual, idUsuarioAlta);

				if (Objects.nonNull(contratanteRequest.getCp())) {
	    			if (contratanteRequest.getCp().getIdDomicilio()==null) {
	    				statement.executeUpdate(reglasNegocioRepository.insertarDomicilio(contratanteRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
						rs=statement.getGeneratedKeys();
						if (rs.next()) {
							contratanteRequest.getCp().setIdDomicilio(rs.getInt(1));
						}
						String domicilio= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+contratanteRequest.getCp().getIdDomicilio().toString());
						BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 1, null, domicilio, idUsuarioAlta);
					}else {
						String domicilioAnterior= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+contratanteRequest.getCp().getIdDomicilio().toString());

						statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(contratanteRequest.getCp(),idUsuarioAlta), Statement.RETURN_GENERATED_KEYS);
						String domicilioActual= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+contratanteRequest.getCp().getIdDomicilio().toString());
						BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 1, domicilioAnterior, domicilioActual, idUsuarioAlta);

					}		
				}
				String contratanteAnterior= BitacoraUtil.consultarInformacion(connection, "SVC_CONTRATANTE", "ID_CONTRATANTE = "+String.valueOf(contratanteRequest.getIdContratante()));

				statement.executeUpdate(reglasNegocioRepository.actualizarContratante(contratanteRequest, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				String contratanteActual= BitacoraUtil.consultarInformacion(connection, "SVC_CONTRATANTE", "ID_CONTRATANTE = "+String.valueOf(contratanteRequest.getIdContratante()));
				BitacoraUtil.insertarInformacion(connection, "SVC_CONTRATANTE", 1, contratanteAnterior, contratanteActual, idUsuarioAlta);

				
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
			
			String domicilioAnterior= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+contratanteRequest.getCp().getIdDomicilio().toString());
			statement.executeUpdate(reglasNegocioRepository.actualizarDomicilio(contratanteRequest.getCp(), idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			String domicilioActual= BitacoraUtil.consultarInformacion(connection, "SVT_DOMICILIO", "ID_DOMICILIO = "+contratanteRequest.getCp().getIdDomicilio().toString());
			BitacoraUtil.insertarInformacion(connection, "SVT_DOMICILIO", 2, domicilioAnterior, domicilioActual, idUsuarioAlta);

			
			String personaAnterior= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+contratanteRequest.getIdPersona().toString());
			statement.executeUpdate(reglasNegocioRepository.actualizarPersona(contratanteRequest, idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			String personaActual= BitacoraUtil.consultarInformacion(connection, "SVC_PERSONA", "ID_PERSONA="+contratanteRequest.getIdPersona().toString());
			BitacoraUtil.insertarInformacion(connection, "SVC_PERSONA", 2, personaAnterior, personaActual, idUsuarioAlta);

			String contratanteAnterior= BitacoraUtil.consultarInformacion(connection, "SVC_CONTRATANTE", "ID_CONTRATANTE = "+String.valueOf(contratanteRequest.getIdContratante()));
			statement.executeUpdate(reglasNegocioRepository.actualizarContratante(contratanteRequest, idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			String contratanteActual= BitacoraUtil.consultarInformacion(connection, "SVC_CONTRATANTE", "ID_CONTRATANTE = "+String.valueOf(contratanteRequest.getIdContratante()));
			BitacoraUtil.insertarInformacion(connection, "SVC_CONTRATANTE", 2, contratanteAnterior, contratanteActual, idUsuarioAlta);

			
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
