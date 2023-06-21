package com.imss.sivimss.ordservicios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdenesServicioRequest {

	private Integer idOrdenServicio;
	
	private String folio;

	private Integer idParentesco;

	private Integer idVelatorio;

	private Integer idOperador;

	private Integer idEstatus;

	private ContratanteRequest contratante;

	private FinadoRequest finado;
	
	private CaracteristicasPresupuestoRequest caracteristicasPresupuesto;
	
	private InformacionServicioRequest informacionServicio;
}
