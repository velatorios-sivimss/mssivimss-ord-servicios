package com.imss.sivimss.ordservicios.model.response;

import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleTrasladoRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaracteristicasPaqueteDetallePresupuestoResponse {
	private Integer idPaqueteDetallePresupuesto;
	private Integer idCategoria;
	private Integer idArticulo;
	private Integer idInventario;
	private Integer idServicio;
	private Integer idTipoServicio;
	private String grupo;
	private String concepto;
	private Integer cantidad;
	private Integer idProveedor;
	private String nombreProveedor;
	private CaracteristicasPaqueteDetalleTrasladoRequest servicioDetalleTraslado;
	private Integer esDonado;
	private Double importeMonto;
	private String proviene;
}
