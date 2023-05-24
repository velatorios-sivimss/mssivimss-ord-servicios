package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaracteristicasPaqueteDetallePresupuestoRequest {
	private Integer idPaqueteDetallePresupuesto;
	private Integer idArticulo;
	private Integer idServicio;
	private CaracteristicasPaqueteDetalleTrasladoRequest servicioDetalleTraslado;
	private Integer cantidad;
	private Integer idProveedor;
	private Double importeMonto;
}
