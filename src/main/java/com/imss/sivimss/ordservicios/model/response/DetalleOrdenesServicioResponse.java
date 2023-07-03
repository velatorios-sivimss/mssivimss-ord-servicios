package com.imss.sivimss.ordservicios.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalleOrdenesServicioResponse {

	private Integer idOrdenServicio;
	
	private String folio;

	private Integer idParentesco;

	private Integer idVelatorio;

	private Integer idOperador;

	private Integer idEstatus;
	
	private Integer idContratantePf;

	private ContratanteResponse contratante;

	private FinadoResponse finado;
	
	private CaracteristicasPresupuestoResponse caracteristicasPresupuesto;
	
	private InformacionServicioResponse informacionServicio;
}
