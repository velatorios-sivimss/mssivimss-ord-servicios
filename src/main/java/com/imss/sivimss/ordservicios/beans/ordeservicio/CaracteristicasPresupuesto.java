package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetallePresupuestoRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPresupuestoRequest;
import com.imss.sivimss.ordservicios.model.request.OrdenesServicioRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.BitacoraUtil;

@Service
public class CaracteristicasPresupuesto {

	private ResultSet rs;

	private Statement statement;
	
	private Integer idDonacion;

	private Integer idInventario;

	private Boolean salidaDonacion=false;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	public void insertarCaracteristicasPresupuestoTemp(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, OrdenesServicioRequest idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException{
		try {
			statement=connection.createStatement();
			
			// caracteristicas paquete temp
			if (caracteristicasPresupuestoRequest.getCaracteristicasPaquete()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarCaracteristicasPaquete("SVC_CARAC_PAQUETE_TEMP",caracteristicasPresupuestoRequest.getCaracteristicasPaquete(), idOrdenServicio.getIdOrdenServicio(), idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					Integer id=rs.getInt(1);
					String contratante= BitacoraUtil.consultarInformacion(connection, "SVC_CARAC_PAQUETE_TEMP", "ID_CARAC_PAQUETE = "+ id);
					BitacoraUtil.insertarInformacion(connection, "SVC_CARAC_PAQUETE_TEMP", 1, null, contratante, idUsuarioAlta);
					caracteristicasPresupuestoRequest.getCaracteristicasPaquete().setIdCaracteristicasPaquete(id);
				}
				// detalle caracteristicas paquete temp
				detalleCaracteristicasPaqueteTemp(caracteristicasPresupuestoRequest, idUsuarioAlta);
				
			}
			// caracteristicas presupuesto temp
			if (caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarCaracteristicasPresupuesto("SVC_CARAC_PRESUP_TEMP", caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto(), idOrdenServicio.getIdOrdenServicio(), idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					Integer id=rs.getInt(1);
					String contratante= BitacoraUtil.consultarInformacion(connection, "SVC_CARAC_PRESUP_TEMP", "ID_CARAC_PRESUPUESTO = " + id);
					BitacoraUtil.insertarInformacion(connection, "SVC_CARAC_PRESUP_TEMP", 1, null, contratante, idUsuarioAlta);
					caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().setIdCaracteristicasPresupuesto(id);
				}
				// detalle caracteristicas presupuesto temp
				detalleCaracteristicasPresupuestoTemp(caracteristicasPresupuestoRequest, idOrdenServicio.getIdOrdenServicio(), idUsuarioAlta);
				// salida donacion
				if ((idOrdenServicio.getFinado().getIdTipoOrden()==1 || idOrdenServicio.getFinado().getIdTipoOrden()==2) && Boolean.TRUE.equals(salidaDonacion)) {
					insertarSalidaDonacionTemp(idOrdenServicio,idUsuarioAlta);
				}
				
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
	
	
	public void insertarCaracteristicasPresupuesto(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, OrdenesServicioRequest idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException{
		try {
			statement=connection.createStatement();
			
			// caracteristicas paquete
			if (caracteristicasPresupuestoRequest.getCaracteristicasPaquete()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarCaracteristicasPaquete("SVC_CARACTERISTICAS_PAQUETE",caracteristicasPresupuestoRequest.getCaracteristicasPaquete(), idOrdenServicio.getIdOrdenServicio(), idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					Integer id=rs.getInt(1);
					String contratante= BitacoraUtil.consultarInformacion(connection, "SVC_CARACTERISTICAS_PAQUETE", "ID_CARAC_PAQUETE = "+ id);
					BitacoraUtil.insertarInformacion(connection, "SVC_CARACTERISTICAS_PAQUETE", 1, null, contratante, idUsuarioAlta);

					caracteristicasPresupuestoRequest.getCaracteristicasPaquete().setIdCaracteristicasPaquete(id);
				}
				// detalle caracteristicas paquete
				detalleCaracteristicasPaquete(caracteristicasPresupuestoRequest, idOrdenServicio.getIdOrdenServicio(), idUsuarioAlta);
				
			}
			// caracteristicas presupuesto
			if (caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarCaracteristicasPresupuesto(
						"SVC_CARAC_PRESUPUESTO", 
						caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto(), 
						idOrdenServicio.getIdOrdenServicio(), 
						idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					Integer id=rs.getInt(1);
					String contratante= BitacoraUtil.consultarInformacion(connection, "SVC_CARAC_PRESUPUESTO", "ID_CARAC_PRESUPUESTO = " + id);
					BitacoraUtil.insertarInformacion(connection, "SVC_CARAC_PRESUPUESTO", 1, null, contratante, idUsuarioAlta);

					caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().setIdCaracteristicasPresupuesto(id);
				}
				// detalle caracteristicas presupuesto
				detalleCaracteristicasPresupuesto(caracteristicasPresupuestoRequest, idOrdenServicio.getIdOrdenServicio(), idUsuarioAlta);
				// salida donacion
				if ((idOrdenServicio.getFinado().getIdTipoOrden()==1 || idOrdenServicio.getFinado().getIdTipoOrden()==2) && Boolean.TRUE.equals(salidaDonacion)) {
					insertarSalidaDonacion(idOrdenServicio,idUsuarioAlta);
				}
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
	

	private void detalleCaracteristicasPaqueteTemp(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, Integer idUsuarioAlta) throws SQLException{
		// detalle caracteristicas paquete temp
		if (caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getDetallePaquete()!=null) {
			for(CaracteristicasPaqueteDetalleRequest detalleRequest: caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getDetallePaquete()) {
				statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPaquete(
						"SVC_DETALLE_CARAC_PAQ_TEMP", 
						detalleRequest, 
						caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getIdCaracteristicasPaquete(), 
						idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					Integer id=rs.getInt(1);
					String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_DETALLE_CARAC_PAQ_TEMP", "ID_DETALLE_CARAC = " + id);
					BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_DETALLE_CARAC_PAQ_TEMP", 1, null, detalle, idUsuarioAlta);
					detalleRequest.setIdPaqueteDetalle(id);
				}
				if ((detalleRequest.getIdTipoServicio()!=null && detalleRequest.getIdTipoServicio()==4) && detalleRequest.getServicioDetalleTraslado()!=null) {
					statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPaqueteTraslado("SVC_CARAC_PAQ_TRAS_TEMP", detalleRequest.getServicioDetalleTraslado(), detalleRequest.getIdPaqueteDetalle(), idUsuarioAlta),
							Statement.RETURN_GENERATED_KEYS);
					rs = statement.getGeneratedKeys();
					if (rs.next()) {
						Integer idTraslado=rs.getInt(1);
						String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_CARAC_PAQ_TRAS_TEMP", "ID_CARAC_PRESU_TRASLADO = "+ idTraslado);
						BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_CARAC_PAQ_TRAS_TEMP", 1, null, detalle, idUsuarioAlta);

						detalleRequest.getServicioDetalleTraslado().setIdCaracteristicasPaqueteDetalleTraslado(idTraslado);
						detalleRequest.getServicioDetalleTraslado().setIdDetalleCaracteristicas(detalleRequest.getIdPaqueteDetalle());
					}
				}
			}
		}
	}
	
	private void detalleCaracteristicasPaquete(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, Integer idOrdenServicio, Integer idUsuarioAlta) throws SQLException{
		// detalle caracteristicas paquete
		if (caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getDetallePaquete()!=null) {
			for(CaracteristicasPaqueteDetalleRequest detalleRequest: caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getDetallePaquete()) {
				statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPaquete(
						"SVC_DETALLE_CARAC_PAQ", 
						detalleRequest, 
						caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getIdCaracteristicasPaquete(), 
						idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					Integer id=rs.getInt(1);
					String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_DETALLE_CARAC_PAQ", "ID_DETALLE_CARAC = " + id);
					BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_DETALLE_CARAC_PAQ", 1, null, detalle, idUsuarioAlta);

					detalleRequest.setIdPaqueteDetalle(id);
				}
				if ((detalleRequest.getIdTipoServicio()!=null && detalleRequest.getIdTipoServicio()==4) && 
						detalleRequest.getServicioDetalleTraslado()!=null) {
					statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPaqueteTraslado(
							"SVC_CARAC_PAQ_TRAS", 
							detalleRequest.getServicioDetalleTraslado(), 
							detalleRequest.getIdPaqueteDetalle(), idUsuarioAlta),
							Statement.RETURN_GENERATED_KEYS);
					rs = statement.getGeneratedKeys();
					if (rs.next()) {
						Integer idTraslado=rs.getInt(1);
						String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_CARAC_PAQ_TRAS", "ID_CARAC_PRESUP_TRASLADO = "+ idTraslado);
						BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_CARAC_PAQ_TRAS", 1, null, detalle, idUsuarioAlta);

						detalleRequest.getServicioDetalleTraslado().setIdCaracteristicasPaqueteDetalleTraslado(idTraslado);
						detalleRequest.getServicioDetalleTraslado().setIdDetalleCaracteristicas(detalleRequest.getIdPaqueteDetalle());
					}
				}
			}
		}
	}

	private void detalleCaracteristicasPresupuestoTemp(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, Integer idOrdenServicio, Integer idUsuarioAlta) throws SQLException{
		// detalle caracteristicas presupuesto temp
		if (caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getDetallePresupuesto()!=null) {
			
			for (CaracteristicasPaqueteDetallePresupuestoRequest detallePresupuestoRequest : caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getDetallePresupuesto()) {
				statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPresupuesto(
						"SVC_DETALLE_CARAC_PRESUP_TEMP",
						detallePresupuestoRequest, 
						caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getIdCaracteristicasPresupuesto(), 
						idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					Integer id=rs.getInt(1);
					String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_DETALLE_CARAC_PRESUP_TEMP", "ID_DETALLE_CARACTERISTICAS = " + id);
					BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_DETALLE_CARAC_PRESUP_TEMP", 1, null, detalle, idUsuarioAlta);

					detallePresupuestoRequest.setIdPaqueteDetallePresupuesto(id);
				}
				if ((detallePresupuestoRequest.getEsDonado()!=null && detallePresupuestoRequest.getEsDonado()==1) 
						&& detallePresupuestoRequest.getIdCategoria()==1) {
					statement.executeUpdate(reglasNegocioRepository.insertarAtaudDonadoTemp(
							idOrdenServicio, 
							detallePresupuestoRequest.getIdInventario(), 
							idUsuarioAlta),
							Statement.RETURN_GENERATED_KEYS);
					rs = statement.getGeneratedKeys();
				}
				
				if (detallePresupuestoRequest.getIdCategoria()!=null && detallePresupuestoRequest.getIdCategoria()==1 && detallePresupuestoRequest.getProviene().equalsIgnoreCase("paquete") && detallePresupuestoRequest.getEsDonado()==0) {
					idInventario=detallePresupuestoRequest.getIdInventario();
					salidaDonacion=true;
				}
				// traslado temp
				caracteristicasPresupuestoDetalleTrasladoTemp(detallePresupuestoRequest, idUsuarioAlta);
				
			}
		}
	}
	
	private void caracteristicasPresupuestoDetalleTrasladoTemp(CaracteristicasPaqueteDetallePresupuestoRequest detallePresupuestoRequest, Integer idUsuarioAlta) throws SQLException{
		// traslado temp
		if ((detallePresupuestoRequest.getIdTipoServicio()!=null && detallePresupuestoRequest.getIdTipoServicio()==4) 
				&& detallePresupuestoRequest.getServicioDetalleTraslado()!=null) {
			statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPresupuestoTraslado(
					"SVC_CARAC_PRESUP_TRAS_TEMP", 
					detallePresupuestoRequest.getServicioDetalleTraslado(), 
					detallePresupuestoRequest.getIdPaqueteDetallePresupuesto(), 
					idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				Integer idTraslado=rs.getInt(1);
				String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_CARAC_PRESUP_TRAS_TEMP", "ID_CARAC_PRESU_TRASLADO = "+ idTraslado);
				BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_CARAC_PRESUP_TRAS_TEMP", 1, null, detalle, idUsuarioAlta);
				detallePresupuestoRequest.getServicioDetalleTraslado().setIdCaracteristicasPaqueteDetalleTraslado(idTraslado);
				detallePresupuestoRequest.getServicioDetalleTraslado().setIdDetalleCaracteristicas(detallePresupuestoRequest.getIdPaqueteDetallePresupuesto());
			}
		}
	}
	
	private void detalleCaracteristicasPresupuesto(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, Integer idOrdenServicio, Integer idUsuarioAlta) throws SQLException{
		// detalle caracteristicas presupuesto
		if (caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getDetallePresupuesto()!=null) {
			
			for (CaracteristicasPaqueteDetallePresupuestoRequest detallePresupuestoRequest : caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getDetallePresupuesto()) {
				statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPresupuesto(
						"SVC_DETALLE_CARAC_PRESUP", 
						detallePresupuestoRequest, 
						caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getIdCaracteristicasPresupuesto(), 
						idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					Integer id=rs.getInt(1);
					String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_DETALLE_CARAC_PRESUP", " ID_DETALLE_CARACTERISTICAS = " + String.valueOf(id));
					BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_DETALLE_CARAC_PRESUP", 1, null, detalle, idUsuarioAlta);

					detallePresupuestoRequest.setIdPaqueteDetallePresupuesto(id);
				}
				if ((detallePresupuestoRequest.getEsDonado()!=null && detallePresupuestoRequest.getEsDonado()==1) 
						&& detallePresupuestoRequest.getIdCategoria()==1) {
					statement.executeUpdate(reglasNegocioRepository.insertarDonacion(
							idOrdenServicio, 
							idUsuarioAlta),
							Statement.RETURN_GENERATED_KEYS);
					rs = statement.getGeneratedKeys();
					
					if (rs.next()) {
						idDonacion=rs.getInt(1);
					}
					
					statement.executeUpdate(reglasNegocioRepository.insertarAtaudDonado(
							idDonacion, 
							detallePresupuestoRequest.getIdInventario(),
							idUsuarioAlta),
							Statement.RETURN_GENERATED_KEYS);
					
					statement.executeUpdate(reglasNegocioRepository.actualizarAtaudTipoAsignacion(
							detallePresupuestoRequest.getIdInventario(),
							3,
							idUsuarioAlta,0),
							Statement.RETURN_GENERATED_KEYS);
					
				}
				
				if(detallePresupuestoRequest.getIdInventario()!=null && (detallePresupuestoRequest.getIdCategoria()==1 || detallePresupuestoRequest.getIdCategoria()==2 || detallePresupuestoRequest.getIdCategoria()==4)) {
					if (detallePresupuestoRequest.getIdCategoria()==1 && detallePresupuestoRequest.getEsDonado()==1) {
						statement.executeUpdate(reglasNegocioRepository.actualizarEstatusAtaud(
								detallePresupuestoRequest.getIdInventario(),
								idUsuarioAlta,0),
								Statement.RETURN_GENERATED_KEYS);
					}else {
						statement.executeUpdate(reglasNegocioRepository.actualizarEstatusAtaud(
							detallePresupuestoRequest.getIdInventario(),
							idUsuarioAlta,1),
							Statement.RETURN_GENERATED_KEYS);
					}
					
				}
				if (detallePresupuestoRequest.getIdCategoria()!=null && detallePresupuestoRequest.getIdCategoria()==1 
						&& detallePresupuestoRequest.getProviene().equalsIgnoreCase("paquete") 
						&& detallePresupuestoRequest.getEsDonado()==0) {
					idInventario=detallePresupuestoRequest.getIdInventario();
					salidaDonacion=true;
				}
				// traslado
				caracteristicasPresupuestoDetalleTraslado(detallePresupuestoRequest, idUsuarioAlta);
				
			}
		}
	}
	
	private void caracteristicasPresupuestoDetalleTraslado(CaracteristicasPaqueteDetallePresupuestoRequest detallePresupuestoRequest, Integer idUsuarioAlta) throws SQLException{
		// traslado
		if ((detallePresupuestoRequest.getIdTipoServicio()!=null && detallePresupuestoRequest.getIdTipoServicio()==4) 
				&& detallePresupuestoRequest.getServicioDetalleTraslado()!=null) {
			statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPresupuestoTraslado(
					"SVC_CARAC_PRESUP_TRASLADO", 
					detallePresupuestoRequest.getServicioDetalleTraslado(), 
					detallePresupuestoRequest.getIdPaqueteDetallePresupuesto(), 
					idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				Integer id=rs.getInt(1);
				String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_CARAC_PRESUP_TRASLADO", " ID_CARAC_PRESUP_TRASLADO = " + id);
				BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_CARAC_PRESUP_TRASLADO", 1, null, detalle, idUsuarioAlta);
				detallePresupuestoRequest.getServicioDetalleTraslado().setIdCaracteristicasPaqueteDetalleTraslado(id);
				detallePresupuestoRequest.getServicioDetalleTraslado().setIdDetalleCaracteristicas(detallePresupuestoRequest.getIdPaqueteDetallePresupuesto());
			}
		}
	}

	private void insertarSalidaDonacionTemp(OrdenesServicioRequest ordenesServicioRequest,Integer idUsuarioAlta) throws SQLException {
		ResultSet consultaAsignacion=null;
		try {
			consultaAsignacion = statement
				.executeQuery(reglasNegocioRepository.consultarAsignacionInventario(idInventario));
		
		if (consultaAsignacion.next()) {
			Integer idTipoAsignacion = consultaAsignacion.getInt("idAsignacion");
			if (idTipoAsignacion == 3) {

				statement
						.executeUpdate(
								reglasNegocioRepository.insertarSalidaDonacion("SVC_SALIDA_DONACION_TEMP",
										ordenesServicioRequest.getIdOrdenServicio(), 1,
										ordenesServicioRequest.getCaracteristicasPresupuesto()
												.getCaracteristicasPaquete().getOtorgamiento(),
										ordenesServicioRequest.getContratante().getIdContratante(), idUsuarioAlta),
								Statement.RETURN_GENERATED_KEYS);
				

				rs = statement.getGeneratedKeys();

				if (rs.next()) {
					Integer idSalidaDonacion = rs.getInt(1);
					String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_SALIDA_DONACION_TEMP", "ID_SALIDA_DONACION = "+ idSalidaDonacion);
					BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_SALIDA_DONACION_TEMP", 1, null, detalle, idUsuarioAlta);

					statement.executeUpdate(
							reglasNegocioRepository.insertarSalidaDonacionAtaud("SVC_SALIDA_DON_ATAUDES_TEMP",
									idSalidaDonacion, idInventario, idUsuarioAlta),
							Statement.RETURN_GENERATED_KEYS);	
					
					String detalleDonacion =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_SALIDA_DON_ATAUDES_TEMP", "ID_SALIDA_DONACION = "+ idSalidaDonacion);
				    BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_SALIDA_DON_ATAUDES_TEMP", 1, null, detalleDonacion, idUsuarioAlta);

					rs = statement.getGeneratedKeys();
					if (rs.next()) {
						statement.executeUpdate(reglasNegocioRepository.insertarSalidaDonacionFinado(
								"SVC_SALIDA_DON_FINADOS_TEMP", ordenesServicioRequest.getFinado().getNomPersona(),
								ordenesServicioRequest.getFinado().getPrimerApellido(),
								ordenesServicioRequest.getFinado().getSegundoApellido(), idSalidaDonacion,
								idUsuarioAlta));
						rs = statement.getGeneratedKeys();
						if (rs.next()) {
						     Integer idSalidaDonacionFinados = rs.getInt(1);
						     String detalleDonacionFinados =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_SALIDA_DON_FINADOS_TEMP", "ID_SALIDA_DONACION_FINADOS = "+ idSalidaDonacionFinados);
						     BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_SALIDA_DON_FINADOS_TEMP", 1, null, detalleDonacionFinados, idUsuarioAlta);
						}
					}
				}
			}
		}
		} finally {
			if (consultaAsignacion!=null) {
				consultaAsignacion.close();
			}
		}
		
	}
	
	private void insertarSalidaDonacion(OrdenesServicioRequest ordenesServicioRequest,Integer idUsuarioAlta) throws SQLException {
		ResultSet consultaAsignacion=null;
		try {
			consultaAsignacion = statement
				.executeQuery(reglasNegocioRepository.consultarAsignacionInventario(idInventario));
		if (consultaAsignacion.next()) {
			Integer idTipoAsignacion = consultaAsignacion.getInt("idAsignacion");
			if (idTipoAsignacion == 3) {
				statement
						.executeUpdate(
								reglasNegocioRepository.insertarSalidaDonacion("SVC_SALIDA_DONACION",
										ordenesServicioRequest.getIdOrdenServicio(), 1,
										ordenesServicioRequest.getCaracteristicasPresupuesto()
												.getCaracteristicasPaquete().getOtorgamiento(),
										ordenesServicioRequest.getContratante().getIdContratante(), idUsuarioAlta),
								Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();

				if (rs.next()) {
					Integer idSalidaDonacion = rs.getInt(1);
					String detalle =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_SALIDA_DONACION", "ID_SALIDA_DONACION = "+ idSalidaDonacion);
					BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_SALIDA_DONACION", 1, null, detalle, idUsuarioAlta);
					statement
							.executeUpdate(
									reglasNegocioRepository.insertarSalidaDonacionAtaud("SVC_SALIDA_DONACION_ATAUDES",
											idSalidaDonacion, idInventario, idUsuarioAlta),
									Statement.RETURN_GENERATED_KEYS);
					
					String detalleDonacion =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_SALIDA_DONACION_ATAUDES", "ID_SALIDA_DONACION = "+ idSalidaDonacion);
					BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_SALIDA_DONACION_ATAUDES", 1, null, detalleDonacion, idUsuarioAlta);

					rs = statement.getGeneratedKeys();
					if (rs.next()) {
						
						statement.executeUpdate(reglasNegocioRepository.insertarSalidaDonacionFinado(
								"SVC_SALIDA_DONACION_FINADOS", ordenesServicioRequest.getFinado().getNomPersona(),
								ordenesServicioRequest.getFinado().getPrimerApellido(),
								ordenesServicioRequest.getFinado().getSegundoApellido(), idSalidaDonacion,
								idUsuarioAlta));
						rs = statement.getGeneratedKeys();
						if (rs.next()) {
						     Integer idSalidaDonacionFinados = rs.getInt(1);
						     String detalleDonacionFinados =BitacoraUtil.consultarInformacion(statement.getConnection(), "SVC_SALIDA_DONACION_FINADOS", "ID_SALIDA_DONACION_FINADOS = "+ idSalidaDonacionFinados);
						     BitacoraUtil.insertarInformacion(statement.getConnection(), "SVC_SALIDA_DONACION_FINADOS", 1, null, detalleDonacionFinados, idUsuarioAlta);
						}
					}
				}

				statement.executeUpdate(
						reglasNegocioRepository.actualizarAtaudTipoAsignacion(idInventario, 4, idUsuarioAlta, null),
						Statement.RETURN_GENERATED_KEYS);

			}
		}
		} finally {
			if (consultaAsignacion!=null) {
				consultaAsignacion.close();
			}
		}
		
	}
	
}
