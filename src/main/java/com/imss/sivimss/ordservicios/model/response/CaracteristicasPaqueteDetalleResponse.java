package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.imss.sivimss.ordservicios.model.request.CaracteristicasPaqueteDetalleTrasladoRequest;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaracteristicasPaqueteDetalleResponse {
	private Integer idPaqueteDetalle;
	private Integer idArticulo;
	private Integer idServicio;
	private Integer idTipoServicio;
	private String grupo;
	private String concepto;	
	private String desmotivo;
	private Integer activo;
	private Integer cantidad;
	private Integer idProveedor;
	private String nombreProveedor;	
	private Double importeMonto;
	private CaracteristicasPaqueteDetalleTrasladoRequest servicioDetalleTraslado;
	private Double totalPaquete;
	private Boolean agregado;
}
