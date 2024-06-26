package com.imss.sivimss.ordservicios.model.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaracteristicasPaquetePresupuestoRequest {

	private Integer idCaracteristicasPresupuesto;
	private Integer idPaquete;
	private String totalPresupuesto;
	private String observaciones;
	private String notasServicio;
	private List<CaracteristicasPaqueteDetallePresupuestoRequest> detallePresupuesto;
}
