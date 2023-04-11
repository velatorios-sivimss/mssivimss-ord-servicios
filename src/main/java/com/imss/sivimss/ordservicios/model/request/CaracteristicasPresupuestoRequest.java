package com.imss.sivimss.ordservicios.model.request;

import java.util.List;

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
public class CaracteristicasPresupuestoRequest {

	private Integer idCaracteristicasPresupuesto;
	private Double cantidadPresupuesto;
	private Integer idPaquete;
	private List<DetalleCaracteristicasPresupuestoRequest> detallesPresupuesto;
}
