package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaracteristicasPaqueteDetalleRequest {
	private Integer idPaqueteDetalle;
	private Integer idArticulo;
	private Integer idTipoArticulo;
	private Integer idServicio;
	private Integer idTipoServicio;
	private CaracteristicasPaqueteDetalleTrasladoRequest servicioDetalleTraslado;
	private String desmotivo;
	private Integer cantidad;
	private Integer esDonado;
	private Integer idProveedor;
	private Double importeMonto;
	private Double totalPaquete;
}
