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
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;

@Service
public class CaracteristicasPresupuesto {

	private ResultSet rs;

	private Statement statement;
	
	private Integer idDonacion;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	public void insertarCaracteristicasPresupuestoTemp(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException{
		try {
			statement=connection.createStatement();
			
			// caracteristicas paquete temp
			if (caracteristicasPresupuestoRequest.getCaracteristicasPaquete()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarCaracteristicasPaquete("SVC_CARACTERISTICAS_PAQUETE_TEMP",caracteristicasPresupuestoRequest.getCaracteristicasPaquete(), idOrdenServicio, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					caracteristicasPresupuestoRequest.getCaracteristicasPaquete().setIdCaracteristicasPaquete(rs.getInt(1));
				}
				// detalle caracteristicas paquete temp
				detalleCaracteristicasPaqueteTemp(caracteristicasPresupuestoRequest, idUsuarioAlta);
				
			}
			// caracteristicas presupuesto temp
			if (caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarCaracteristicasPresupuesto("SVC_CARACTERISTICAS_PRESUPUESTO_TEMP", caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto(), idOrdenServicio, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().setIdCaracteristicasPresupuesto(rs.getInt(1));
				}
				// detalle caracteristicas presupuesto temp
				detalleCaracteristicasPresupuestoTemp(caracteristicasPresupuestoRequest, idOrdenServicio, idUsuarioAlta);
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
	
	public void insertarCaracteristicasPresupuesto(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException{
		try {
			statement=connection.createStatement();
			
			// caracteristicas paquete
			if (caracteristicasPresupuestoRequest.getCaracteristicasPaquete()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarCaracteristicasPaquete("SVC_CARACTERISTICAS_PAQUETE",caracteristicasPresupuestoRequest.getCaracteristicasPaquete(), idOrdenServicio, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					caracteristicasPresupuestoRequest.getCaracteristicasPaquete().setIdCaracteristicasPaquete(rs.getInt(1));
				}
				// detalle caracteristicas paquete
				detalleCaracteristicasPaquete(caracteristicasPresupuestoRequest, idOrdenServicio, idUsuarioAlta);
				
			}
			// caracteristicas presupuesto
			if (caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarCaracteristicasPresupuesto(
						"SVC_CARACTERISTICAS_PRESUPUESTO", 
						caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto(), 
						idOrdenServicio, 
						idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().setIdCaracteristicasPresupuesto(rs.getInt(1));
				}
				// detalle caracteristicas presupuesto
				detalleCaracteristicasPresupuesto(caracteristicasPresupuestoRequest, idOrdenServicio, idUsuarioAlta);
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
						"SVC_DETALLE_CARACTERISTICAS_PAQUETE_TEMP", 
						detalleRequest, 
						caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getIdCaracteristicasPaquete(), 
						idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					detalleRequest.setIdPaqueteDetalle(rs.getInt(1));
				}
				if ((detalleRequest.getIdTipoServicio()!=null && detalleRequest.getIdTipoServicio()==4) && detalleRequest.getServicioDetalleTraslado()!=null) {
					statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPaqueteTraslado("SVC_CARACTERISTICA_PAQUETE_TRASLADO_TEMP", detalleRequest.getServicioDetalleTraslado(), detalleRequest.getIdPaqueteDetalle(), idUsuarioAlta),
							Statement.RETURN_GENERATED_KEYS);
					rs = statement.getGeneratedKeys();
					if (rs.next()) {
						detalleRequest.getServicioDetalleTraslado().setIdCaracteristicasPaqueteDetalleTraslado(rs.getInt(1));
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
						"SVC_DETALLE_CARACTERISTICAS_PAQUETE", 
						detalleRequest, 
						caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getIdCaracteristicasPaquete(), 
						idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					detalleRequest.setIdPaqueteDetalle(rs.getInt(1));
				}
				if ((detalleRequest.getIdTipoServicio()!=null && detalleRequest.getIdTipoServicio()==4) && 
						detalleRequest.getServicioDetalleTraslado()!=null) {
					statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPaqueteTraslado(
							"SVC_CARACTERISTICA_PAQUETE_TRASLADO", 
							detalleRequest.getServicioDetalleTraslado(), 
							detalleRequest.getIdPaqueteDetalle(), idUsuarioAlta),
							Statement.RETURN_GENERATED_KEYS);
					rs = statement.getGeneratedKeys();
					if (rs.next()) {
						detalleRequest.getServicioDetalleTraslado().setIdCaracteristicasPaqueteDetalleTraslado(rs.getInt(1));
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
						"SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP",
						detallePresupuestoRequest, 
						caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getIdCaracteristicasPresupuesto(), 
						idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					detallePresupuestoRequest.setIdPaqueteDetallePresupuesto(rs.getInt(1));
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
					"SVC_CARACTERISTICA_PRESUPUESTO_TRASLADO_TEMP", 
					detallePresupuestoRequest.getServicioDetalleTraslado(), 
					detallePresupuestoRequest.getIdPaqueteDetallePresupuesto(), 
					idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				detallePresupuestoRequest.getServicioDetalleTraslado().setIdCaracteristicasPaqueteDetalleTraslado(rs.getInt(1));
				detallePresupuestoRequest.getServicioDetalleTraslado().setIdDetalleCaracteristicas(detallePresupuestoRequest.getIdPaqueteDetallePresupuesto());
			}
		}
	}
	
	private void detalleCaracteristicasPresupuesto(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, Integer idOrdenServicio, Integer idUsuarioAlta) throws SQLException{
		// detalle caracteristicas presupuesto
		if (caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getDetallePresupuesto()!=null) {
			
			for (CaracteristicasPaqueteDetallePresupuestoRequest detallePresupuestoRequest : caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getDetallePresupuesto()) {
				statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPresupuesto(
						"SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO", 
						detallePresupuestoRequest, 
						caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getIdCaracteristicasPresupuesto(), 
						idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					detallePresupuestoRequest.setIdPaqueteDetallePresupuesto(rs.getInt(1));
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
							idUsuarioAlta,1),
							Statement.RETURN_GENERATED_KEYS);
					
				}
				
				if(detallePresupuestoRequest.getIdInventario()!=null && (detallePresupuestoRequest.getIdCategoria()==1 || detallePresupuestoRequest.getIdCategoria()==2 || detallePresupuestoRequest.getIdCategoria()==4)) {
					statement.executeUpdate(reglasNegocioRepository.actualizarEstatusAtaud(
							detallePresupuestoRequest.getIdInventario(),
							idUsuarioAlta,1),
							Statement.RETURN_GENERATED_KEYS);
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
					"SVC_CARACTERISTICA_PRESUPUESTO_TRASLADO", 
					detallePresupuestoRequest.getServicioDetalleTraslado(), 
					detallePresupuestoRequest.getIdPaqueteDetallePresupuesto(), 
					idUsuarioAlta),
					Statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				detallePresupuestoRequest.getServicioDetalleTraslado().setIdCaracteristicasPaqueteDetalleTraslado(rs.getInt(1));
				detallePresupuestoRequest.getServicioDetalleTraslado().setIdDetalleCaracteristicas(detallePresupuestoRequest.getIdPaqueteDetallePresupuesto());
			}
		}
	}
}
