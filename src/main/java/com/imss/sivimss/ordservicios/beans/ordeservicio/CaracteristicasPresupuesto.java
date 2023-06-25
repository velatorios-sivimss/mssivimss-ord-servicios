package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetallePresupuestoRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleTrasladoRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteRequest;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPresupuestoRequest;
import com.imss.sivimss.ordservicios.repository.ReglasNegocioRepository;
import com.imss.sivimss.ordservicios.util.LogUtil;

@Service
public class CaracteristicasPresupuesto {

	@Autowired
	private LogUtil logUtil;
	
	private ResultSet rs;

	private Statement statement;
	
	@Autowired
	private ReglasNegocioRepository reglasNegocioRepository;
	
	public void insertarCaracteristicasPresupuestoTemp(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException{
		try {
			statement=connection.createStatement();
			
			// caracteristicas paquete
			if (caracteristicasPresupuestoRequest.getCaracteristicasPaquete()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarCaracteristicasPaquete("SVC_CARACTERISTICAS_PAQUETE_TEMP",caracteristicasPresupuestoRequest.getCaracteristicasPaquete(), idOrdenServicio, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					caracteristicasPresupuestoRequest.getCaracteristicasPaquete().setIdCaracteristicasPaquete(rs.getInt(1));
				}
				// detalle caracteristicas paquete
				if (caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getDetallePaquete()!=null) {
					for(CaracteristicasPaqueteDetalleRequest detalleRequest: caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getDetallePaquete()) {
						statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPaquete("SVC_DETALLE_CARACTERISTICAS_PAQUETE_TEMP", detalleRequest, caracteristicasPresupuestoRequest.getCaracteristicasPaquete().getIdCaracteristicasPaquete(), idUsuarioAlta),
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
			// caracteristicas presupuesto
			if (caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto()!=null) {
				statement.executeUpdate(reglasNegocioRepository.insertarCaracteristicasPresupuesto("SVC_CARACTERISTICAS_PRESUPUESTO_TEMP", caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto(), idOrdenServicio, idUsuarioAlta),
						Statement.RETURN_GENERATED_KEYS);
				rs = statement.getGeneratedKeys();
				if (rs.next()) {
					caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().setIdCaracteristicasPaquetePresupuesto(rs.getInt(1));
				}
				if (caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getDetallePresupuesto()!=null) {
					// detalle caracteristicas paquete
					for (CaracteristicasPaqueteDetallePresupuestoRequest detallePresupuestoRequest : caracteristicasPresupuestoRequest.getCaracteristicasDelPresupuesto().getDetallePresupuesto()) {
						statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPresupuesto("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP", detallePresupuestoRequest, idOrdenServicio, idUsuarioAlta),
								Statement.RETURN_GENERATED_KEYS);
						rs = statement.getGeneratedKeys();
						if (rs.next()) {
							detallePresupuestoRequest.setIdPaqueteDetallePresupuesto(rs.getInt(1));
						}
						if ((detallePresupuestoRequest.getEsDonado()!=null && detallePresupuestoRequest.getEsDonado()==1) && detallePresupuestoRequest.getIdCategoria()==1) {
							statement.executeUpdate(reglasNegocioRepository.insertarAtaudDonadoTemp(idOrdenServicio, detallePresupuestoRequest.getIdInventario(), idUsuarioAlta),
									Statement.RETURN_GENERATED_KEYS);
							rs = statement.getGeneratedKeys();
						}
						if ((detallePresupuestoRequest.getIdTipoServicio()!=null && detallePresupuestoRequest.getIdTipoServicio()==4) && detallePresupuestoRequest.getServicioDetalleTraslado()!=null) {
							statement.executeUpdate(reglasNegocioRepository.insertarDetalleCaracteristicasPaqueteTraslado("SVC_DETALLE_CARACTERISTICAS_PRESUPUESTO_TEMP", detallePresupuestoRequest.getServicioDetalleTraslado(), detallePresupuestoRequest.getIdPaqueteDetallePresupuesto(), idUsuarioAlta),
									Statement.RETURN_GENERATED_KEYS);
							rs = statement.getGeneratedKeys();
							if (rs.next()) {
								detallePresupuestoRequest.getServicioDetalleTraslado().setIdCaracteristicasPaqueteDetalleTraslado(rs.getInt(1));
								detallePresupuestoRequest.getServicioDetalleTraslado().setIdDetalleCaracteristicas(detallePresupuestoRequest.getIdPaqueteDetallePresupuesto());
							}
						}
						
					}
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
	
	public void insertarCaracteristicasPresupuesto(CaracteristicasPresupuestoRequest caracteristicasPresupuestoRequest, Integer idOrdenServicio, Integer idUsuarioAlta, Connection connection) throws SQLException{
		
	}
	
	private Integer caractersiticasPaqueteTemp(CaracteristicasPaqueteRequest caracteristicasPaqueteRequest,Integer idOrdenServicio, Integer idUsuarioAlta) throws SQLException{
		return 0;
	}

	private Integer caractersiticasPaquete(CaracteristicasPaqueteRequest caracteristicasPaqueteRequest,Integer idOrdenServicio, Integer idUsuarioAlta) throws SQLException{
		return 0;
	}

	private Integer detalleCaracteristicasPaqueteTemp(CaracteristicasPaqueteDetalleRequest  detalleCaracteristicasPaqueteRequest,Integer idOrdenServicio, Integer idUsuarioAlta) throws SQLException{
		return 0;
	}
	private Integer detalleCaracteristicasPaquete(CaracteristicasPaqueteDetalleRequest  detalleCaracteristicasPaqueteRequest,Integer idOrdenServicio, Integer idUsuarioAlta) throws SQLException{
		return 0;
	}

	private Integer caracteristicasPaqueteDetalleTrasladoTemp(CaracteristicasPaqueteDetalleTrasladoRequest  caracteristicasPaqueteDetalleTrasladoRequest,Integer idOrdenServicio, Integer idUsuarioAlta) throws SQLException{
		return 0;
	}

	private Integer caracteristicasPaqueteDetalleTraslado(CaracteristicasPaqueteDetalleTrasladoRequest  caracteristicasPaqueteDetalleTrasladoRequest,Integer idOrdenServicio, Integer idUsuarioAlta) throws SQLException{
		return 0;
	}
}
