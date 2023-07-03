package com.imss.sivimss.ordservicios.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaracteristicasPaqueteResponse {
	private Integer idCaracteristicasPaquete;
	private Integer idPaquete;
	private String otorgamiento;
	private List<CaracteristicasPaqueteDetalleResponse> detallePaquete;
}
