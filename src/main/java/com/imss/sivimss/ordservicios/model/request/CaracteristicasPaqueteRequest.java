package com.imss.sivimss.ordservicios.model.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaracteristicasPaqueteRequest {
	private Integer idCaracteristicasPaquete;
	private Integer idPaquete;
	private String otorgamiento;
	private List<CaracteristicasPaqueteDetalleRequest> detallePaquete;
}
