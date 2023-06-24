package com.imss.sivimss.ordservicios.beans.ordeservicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
				reglasNegocioRepository.insertarCaracteristicasPaquete(null, null, idOrdenServicio, idUsuarioAlta);
			}
			// detalle caracteristicas paquete
		
		
			// caracteristicas presupuesto
			// detalle caracteristicas paquete
		} catch (Exception e) {
			// TODO: handle exception
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
