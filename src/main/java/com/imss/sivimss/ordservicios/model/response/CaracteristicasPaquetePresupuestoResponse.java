package com.imss.sivimss.ordservicios.model.response;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaracteristicasPaquetePresupuestoResponse {
	private Integer idCaracteristicasPresupuesto;
	private Integer idPaquete;
	private String totalPresupuesto;
	private String observaciones;
	private String notasServicio;
	private List<CaracteristicasPaqueteDetallePresupuestoResponse> detallePresupuesto;
}
