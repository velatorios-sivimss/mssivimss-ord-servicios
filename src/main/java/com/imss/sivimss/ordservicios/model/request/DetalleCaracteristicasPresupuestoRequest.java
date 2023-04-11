package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalleCaracteristicasPresupuestoRequest {

	private Integer idDetalleCaracteristicas;
	private Integer idArticulo;
	private Integer idServicio;
	private String desmotivo;
	private Integer cantidad;
	private Double importe;

}
