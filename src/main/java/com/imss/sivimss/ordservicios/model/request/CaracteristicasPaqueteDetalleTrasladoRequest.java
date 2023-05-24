package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaracteristicasPaqueteDetalleTrasladoRequest {
	
	private Integer idCaracteristicasPaqueteDetalleTraslado;
	private String origen;
	private String destino;
	private String totalKilometros;

}
